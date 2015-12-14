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
package com.comcast.video.dawg.service.house;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.controller.house.DawgHouseConfiguration;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDawgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDawgService.class);

    @Autowired
    protected DawgHouseConfiguration config;
    protected Class<PersistableDevice> CLASS_NAME = PersistableDevice.class;
    protected String COLLECTION_NAME = "DawgDevices";
    protected MongoOperations template;
    protected Mongo mongo;

    @PostConstruct
    public void init() throws UnknownHostException {
        mongo = new Mongo(config.getDawgDbHost());
        template = new MongoTemplate(mongo, "test");
    }

    @SuppressWarnings("unchecked")
    protected Map<String,Object>[] toArray(Collection<? extends DawgDevice> col) {
        if (null == col) {
            return new Map[0];
        }
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(DawgDevice d : col) {
            list.add(d.getData());
        }
        return list.toArray(new Map[list.size()]);
    }

    protected Map<String,Object>[] find(Query query) {
        LOGGER.debug("Configuration => {}", this.config);
        //String dcn = this.config.getDawgCollectionName();
        //LOGGER.debug("Collections: STATIC => {}, LOADED => {}", COLLECTION_NAME, dcn);
        return toArray(template.find(query, CLASS_NAME, COLLECTION_NAME));
        //return toArray(template.find(query, CLASS_NAME, this.config.getDawgCollectionName()));
    }

    /**
     * Helper method to get a DBObject with all the fields of an object and then
     * specify the _id of the object
     * @param obj The object to get a DBObject for
     * @param idField The name of the field on the object that will server as the id
     * @return
     */
    public DBObject getDBObject(Object obj, String idField) {
        try {
            DBObject dbObj = new BasicDBObject();
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fName = field.getName();
                Object fVal = field.get(obj);
                dbObj.put(fName, fVal);
                if (fName.equals(idField)) {
                    dbObj.put("_id", fVal);
                }
            }

            return dbObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
