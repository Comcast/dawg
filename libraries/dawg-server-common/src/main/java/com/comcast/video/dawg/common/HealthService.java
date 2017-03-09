package com.comcast.video.dawg.common;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.drivethru.RestClient;
import com.comcast.drivethru.client.DefaultRestClient;
import com.comcast.drivethru.exception.HttpException;

public class HealthService {
    public static final String APP = "app";
    public static final String COMPONENT = "component";
    public static final String VERSION = "version";
    public static final String UPTIME = "uptime";
    public static final String STATUS = "status";
    public static final String APP_VAL = "dawg";
    public static final String STATUS_VAL = "up";
    public static final String[] COMPONENTS = new String[] { "house", "show", "pound" }; 
    public static final long HEALTH_CHECK_TIMEOUT = 5;

    private DawgConfiguration config;
    private String componentName;
    
    public HealthService(DawgConfiguration config, String componentName) {
        this.config = config;
        this.componentName = componentName;
    }

    @RequestMapping("/health")
    @ResponseBody
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(APP, APP_VAL);
        result.put(COMPONENT, this.componentName);
        result.put(VERSION, config.getDawgVersion());
        result.put(UPTIME, ManagementFactory.getRuntimeMXBean().getUptime());
        result.put(STATUS, STATUS_VAL);
        return result;
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> componentHealths() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(COMPONENTS.length);
        final Map<String, String> healthUrls = new HashMap<String, String>();
        healthUrls.put("house", this.config.getDawgHouseHealthUrl());
        healthUrls.put("show", this.config.getDawgShowHealthUrl());
        healthUrls.put("pound", this.config.getDawgPoundHealthUrl());
        final Map<String, Object> health = new HashMap<String, Object>();
        for (final String comp : COMPONENTS) {
            exec.submit(new Runnable() {

                @SuppressWarnings("rawtypes")
                @Override
                public void run() {
                    RestClient client = new DefaultRestClient(healthUrls.get(comp));
                    try {
                        Map compHealth = client.get("", Map.class);
                        health.put(comp, compHealth);
                    } catch (HttpException e) {
                        Map<String, Object> compHealth = new HashMap<String, Object>();
                        compHealth.put("status", "down");
                        health.put(comp, compHealth);
                    } finally {
                        IOUtils.closeQuietly(client);
                    }
                }
                
            });
        }
        exec.shutdown();
        exec.awaitTermination(HEALTH_CHECK_TIMEOUT, TimeUnit.SECONDS);
        HttpStatus returnStatus = HttpStatus.OK;
        for (final String comp : COMPONENTS) {
            Object compHealth = health.get(comp);
            Object status = (compHealth != null) && (compHealth instanceof Map) ? ((Map<String, Object>) compHealth).get("status") : "down";
            if (!"up".equals(status)) {
                returnStatus = HttpStatus.GATEWAY_TIMEOUT;
                break;
            }
        }
        
        return new ResponseEntity<Map<String, Object>>(health, returnStatus);
    }
}
