/**
 * Copyright 2010 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comcast.video.dawg.service.pound;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.controller.pound.DawgPoundConfiguration;

import com.mongodb.Mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class DawgPoundMongoService implements IDawgPoundService {

    private static final String COLLECTION_NAME = "DawgDevices";

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgPoundMongoService.class);

    @Autowired
    protected DawgPoundConfiguration config;
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() throws UnknownHostException {
        mongoTemplate = new MongoTemplate(new Mongo(config.getDawgDbHost()), "test");
    }

    @Override
    public synchronized String unreserve(String id) {
        PersistableDevice device = getById(id);
        if (null == device) {
            LOGGER.info("Attempted to unreserve a non-existing device.  deviceId="+id);
            return null;
        } else {
            String previous = device.unreserve();
            mongoTemplate.save(device,COLLECTION_NAME);
            LOGGER.info("Device unreserved.  deviceId=" + id + ", previous reserver="+previous);
            return previous;
        }
    }

    @Override
    public synchronized boolean overrideReserve(String id, String token, long expiration) {
        long currentTime = System.currentTimeMillis();
        if (expiration < currentTime) {
            LOGGER.warn("Reservation time invalid.  reserver="+token+", deviceId="+id+", expiration="+expiration);
            return false;
        }

        PersistableDevice device = getById(id);
        if (null == device) {
            LOGGER.warn("reserver="+token+" attempted to reserve a non-existing device.  deviceId="+id);
            return false;
        } else {
            device.unreserve();
            device.reserve(token);
            device.setExpiration(expiration);
            mongoTemplate.save(device, COLLECTION_NAME);
            LOGGER.info("Device reserved: deviceId="+id+", reserver="+token);
            return true;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String,Object>[] getByReserver(String token) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Criteria c = new Criteria("reserver").is(token);
        Query q = new Query(c);
        Collection<? extends DawgDevice> col = mongoTemplate.find(q, PersistableDevice.class, COLLECTION_NAME);
        for(DawgDevice device : col) {
            list.add(device.getData());
        }
        return list.toArray(new Map[list.size()]);
    }

    @Override
    public synchronized String reserve(String id, String token, long expiration) {
        PersistableDevice device = getById(id);
        if (null == device) {
            return null;
        } else {
            if (device.isReserved()) {
                String reservedToken = device.getReserver();
                LOGGER.info("Reservation attempted, but device already reserved.  Current: reserved: deviceId="+id+", reserver="+reservedToken);
                return reservedToken;
            } else {
                device.unreserve();
                device.reserve(token);
                device.setExpiration(expiration);
                // Add the new token to the array of indexed keys
                String[] keys = device.getKeys();
                List<String> newKeys = new ArrayList<String>(Arrays.asList(keys));
                newKeys.add(token);
                device.setKeys(newKeys.toArray(new String[newKeys.size()]));

                mongoTemplate.save(device, COLLECTION_NAME);
                LOGGER.info("Device reserved: deviceId="+id+", reserver="+token);
                return token;
            }
        }
    }

    @Override
    public PersistableDevice getById(String id) {
        return mongoTemplate.findById(id, PersistableDevice.class,COLLECTION_NAME);
    }

    @Override
    public Map<String, String> isReserved(String id) {
        PersistableDevice device = getById(id);
        if(null != device && device.isReserved()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("reserver", device.getReserver());
            map.put("expiration", String.valueOf(device.getExpiration()));
            return map;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object>[] list() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Collection<? extends DawgDevice> col = mongoTemplate.findAll(PersistableDevice.class, COLLECTION_NAME);
        for(DawgDevice device : col) {
            list.add(device.getData());
        }
        return list.toArray(new Map[list.size()]);
    }
}
