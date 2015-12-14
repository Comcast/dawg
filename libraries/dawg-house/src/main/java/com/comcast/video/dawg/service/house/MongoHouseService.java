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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.controller.house.filter.TagCondition;
import com.comcast.video.dawg.service.park.ParkService;
import com.comcast.video.stbio.meta.Capability;

@Repository
public class MongoHouseService extends AbstractDawgService implements HouseService {
    @Autowired
    private ParkService parkService;
    private static final Logger logger = Logger.getLogger(MongoHouseService.class);
    /**
     * Update an existing document or insert the devices if the document doesn't exist.
     */
    @Override
    public void upsertStb(DawgDevice... stbs) {
        upsertStb(false, stbs);
    }

    public void replace(DawgDevice... stbs) {
        upsertStb(true, stbs);
    }

    @SuppressWarnings("unchecked")
    private void upsertStb(boolean replace, DawgDevice... stbs) {

        if (!template.collectionExists(PersistableDevice.class)) {
            template.createCollection(PersistableDevice.class);
        }

        for (DawgDevice s : stbs) {
            PersistableDevice persistable = new PersistableDevice(s);
            PersistableDevice existing = template.findById(persistable.getId(), CLASS_NAME, COLLECTION_NAME);
            String modelName = (String) persistable.getData().get(MetaStb.MODEL);
            String familyName = (String) persistable.getData().get(MetaStb.FAMILY);
            Collection<Capability> capabilities = (Collection<Capability>) persistable.getData().get(MetaStb.CAPABILITIES);
            Collection<String> modifiedProperties = (Collection<String>) persistable.getData().get(MetaStb.MODIFIED_PROPS);
            DawgModel model = parkService.getModelById(modelName);
            if (null != existing) {
                persistable = persistKeys(existing, s, replace);
                boolean unmodifiedFamily = (null == modifiedProperties) || !(modifiedProperties.contains(MetaStb.FAMILY));
                boolean unmodifiedCapabilities = (null == modifiedProperties) || !(modifiedProperties.contains(MetaStb.CAPABILITIES));
                if (model == null) {
                    if (unmodifiedFamily){
                        logger.warn("Dawg House has no knowledge of the model '" + modelName + "' while attempting to update. Family will be cleared.");
                        persistable.getData().remove(MetaStb.FAMILY);
                    }
                    if (unmodifiedCapabilities) {
                        logger.warn("Dawg House has no knowledge of the model '" + modelName + "' while attempting to update. Capabilities will be cleared.");
                        persistable.getData().remove(MetaStb.CAPABILITIES);
                    }
                } else {
                    if (unmodifiedFamily) {
                        persistable.getData().put(MetaStb.FAMILY, model.getFamily());
                    }
                    if (unmodifiedCapabilities) {
                        persistable.getData().put(MetaStb.CAPABILITIES, model.getCapabilities());
                    }
                }

            } else {
                if (model == null) {
                    logger.warn("Dawg House has no knowledge of the model '"+ modelName+ "'. Family and capabilities will not be populated.");
                } else {
                    if (null == familyName || familyName.isEmpty()) {
                        persistable.getData().put(MetaStb.FAMILY, model.getFamily());
                    }
                    if (null == capabilities || capabilities.isEmpty()) {
                        persistable.getData().put(MetaStb.CAPABILITIES, model.getCapabilities());
                    }
                }
            }
            template.save(persistable, COLLECTION_NAME);
        }
    }

    private synchronized PersistableDevice persistKeys(PersistableDevice source, DawgDevice target, boolean replace) {
        // copy all existing data into the new document
        Map<String,Object> data = replace ? new HashMap<String, Object>() : source.getData();

        data.putAll(target.getData());
        // this is a dirty way to regenerate the keys
        PersistableDevice update = new PersistableDevice(data);
        update.reserve(source.getReserver());
        update.setExpiration(source.getExpiration());
        return update;
    }

    @Override
    public Map<String,Object>[] getStbsById(String... id) {
        Criteria c = new Criteria(MetaStb.ID).in(Arrays.asList(id));
        List<PersistableDevice> list = template.find(new Query(c), PersistableDevice.class, COLLECTION_NAME);
        return toArray(list);
    }

    @Override
    public Map<String,Object>[] getStbsByKey(String... keys) {
        Collection<String> keyList = new ArrayList<String>();
        for (String key : keys) {
            keyList.add(key.toLowerCase().trim());
        }
        Criteria criteria = new Criteria("keys").all(keyList);
        List<PersistableDevice> list = template.find(new Query(criteria), PersistableDevice.class, COLLECTION_NAME);
        return toArray(list);
    }

    @Override
    public Map<String,Object>[] getStbsByQuery(String query) {
        Criteria c = new Criteria("keys").regex(query);
        List<PersistableDevice> list =  template.find(new Query(c),PersistableDevice.class, COLLECTION_NAME);
        return toArray(list);
    }

    @Override
    public void deleteStbById(String... id) {
        Criteria c = new Criteria("id").in(Arrays.asList(id));
        template.remove(new Query(c), COLLECTION_NAME);
    }


    @Override
    public Map<String,Object> getById(String id) {
        return toArray(template.findById(id, PersistableDevice.class,COLLECTION_NAME));
    }

    @Override
    public Map<String,Object>[] getAll() {
        Query query = new Query();
        List<PersistableDevice> list = template.find(query, PersistableDevice.class, COLLECTION_NAME);
        return toArray(list);
    }

    @Override
    public Map<String, Object>[] getStbsByTag(String tag, String query) {
        return find(new Query(new TagCondition(tag, query).toCriteria()));
    }

    private Map<String,Object> toArray(PersistableDevice device) {
        if (null == device) {
            return null;
        }
        return device.getData();
    }
}
