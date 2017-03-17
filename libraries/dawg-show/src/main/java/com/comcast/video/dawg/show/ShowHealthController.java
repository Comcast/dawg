package com.comcast.video.dawg.show;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.common.HealthService;

@Controller
public class ShowHealthController {
    public static final String COMPONENT_VALUE = "show";

    @Autowired
    private DawgShowConfiguration config;
    private HealthService service;
    
    @PostConstruct
    public void init() {
        this.service = new HealthService(this.config, COMPONENT_VALUE);
    }

    @RequestMapping("/health")
    @ResponseBody
    public Map<String, Object> health() {
        return this.service.health();
    }

    @RequestMapping("/health/components")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> componentHealths(HttpServletRequest request, HttpServletRequest response) throws InterruptedException {
        return this.service.componentHealths();
    }
}
