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
package com.comcast.video.dawg.controller.house.mocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.controller.house.ChimpsToken;
import com.comcast.video.dawg.service.park.ParkService;

public class MockParkService implements ParkService {
    private Collection<Map<String, Object>> allStbs = new HashSet<Map<String, Object>>();
    private Collection<DawgModel> allModels = new HashSet<DawgModel>();
    private Collection<ChimpsToken> allTokens = new HashSet<ChimpsToken>();

    @SuppressWarnings({"unchecked" })
    private Map<String, Object>[] toArray(Collection<Map<String, Object>> coll) {
        Map<String, Object>[] rv = new Map[allStbs.size()];
        int s = 0;
        for (Map<String, Object> item : coll) {
            rv[s] = item;
            s++;
        }
        return rv;
    }

    @Override
    public Map<String, Object>[] findAll() {
        return toArray(allStbs);
    }

    @Override
    public Map<String, Object>[] findAll(Pageable pageable) {
        return findAll();
    }

    @Override
    public Map<String, Object>[] findByKeys(String[] keys) {
        List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
        stbloop :
        for (Map<String, Object> stb : allStbs) {
            for (Object obj : stb.values()) {
                if (obj != null) {
                    for (String key : keys) {
                        if (obj.toString().contains(key)) {
                            filtered.add(stb);
                            break stbloop;
                        }
                    }
                }
            }
        }
        return toArray(filtered);
    }

    @Override
    public Map<String, Object>[] findByKeys(String[] keys, Pageable pageable) {
        return findByKeys(keys);
    }

    @Override
    public void upsertModel(DawgModel... models) {
        for (DawgModel model : models) {
            allModels.add(model);
        }
    }

    @Override
    public List<DawgModel> getModels() {
        return new ArrayList<DawgModel>(allModels);
    }

    @Override
    public void deleteModel(String modelName) {
        DawgModel model = getModelById(modelName);
        if (model != null) {
            allModels.remove(model);
        }
    }

    @Override
    public DawgModel getModelById(String modelName) {
        for (DawgModel model : allModels) {
            if (model.getName().equalsIgnoreCase(modelName)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public List<DawgModel> getModelsByIds(String... modelNames) {
        List<DawgModel> rv = new ArrayList<DawgModel>();
        for (String modelName : modelNames) {
            DawgModel model = getModelById(modelName);
            if (model != null) {
                rv.add(model);
            }
        }
        return rv;
    }

    @Override
    public List<ChimpsToken> getSavedTokens() {
        return new ArrayList<ChimpsToken>(allTokens);
    }

    @Override
    public void saveToken(ChimpsToken token) {
        allTokens.add(token);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> getAllTags() {
        Map<String, Object>[] stbs = findAll();
        Collection<String> tags = new HashSet<String>();
        for(Map<String, Object> stb : stbs) {
            Collection<String> stbTags = (Collection<String>) stb.get(MetaStb.TAGS);
            tags.addAll(stbTags);
        }
        return tags;
    }

    @Override
    public Map<String, Object>[] findByCriteria(Criteria criteria,
            Pageable pageable) {
        return null;
    }

}
