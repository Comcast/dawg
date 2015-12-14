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
package com.comcast.video.dawg.service.park;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.controller.house.ChimpsToken;
import com.comcast.video.dawg.service.house.AbstractDawgService;

@Component
public class MongoParkService extends AbstractDawgService implements ParkService {

    @Override
    public Map<String, Object>[] findAll() {
        return find(new Query());
    }

    @Override
    public Map<String, Object>[] findAll(Pageable pageable) {
        if (null == pageable) {
            return findAll();
        }
        return find(new Query().with(pageable));
    }

    @Override
    public Map<String, Object>[] findByCriteria(Criteria criteria,
            Pageable pageable) {
        return find (new Query(criteria).with(pageable));
    }

    @Override
    public Map<String, Object>[] findByKeys(String[] keys) {
        return find(inKeys(keys));
    }

    @Override
    public Map<String, Object>[] findByKeys(String[] keys, Pageable pageable) {
        if (null == pageable) {
            return findByKeys(keys);
        }
        return find(inKeys(keys).with(pageable));
    }

    private Query inKeys(String[] keys) {
        List<Criteria> list = new ArrayList<Criteria>();
        for(String key : keys) {
            list.add(Criteria.where("keys").regex(key));
        }
        Criteria crit = new Criteria().andOperator(list.toArray(new Criteria[list.size()]));
        return new Query(crit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upsertModel(DawgModel... models) {
        for (DawgModel m : models) {
            template.save(getDBObject(m, "name"), DawgModel.class.getSimpleName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DawgModel> getModels() {
        return template.find(new Query(), DawgModel.class, DawgModel.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteModel(String modelName) {
        Query query = new Query(Criteria.where("name").is(modelName));
        template.remove(query, DawgModel.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DawgModel getModelById(String modelName) {
        Query query = new Query(Criteria.where("name").is(modelName));
        List<DawgModel> models = template.find(query, DawgModel.class, DawgModel.class.getSimpleName());
        return models.isEmpty() ? null : models.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DawgModel> getModelsByIds(String... modelNames) {
        Object[] vals = new Object[modelNames.length];
        for (int i = 0; i < modelNames.length; i++) {
            vals[i] = modelNames[i];
        }
        Criteria crit = new Criteria("name").in(vals);
        return template.find(new Query(crit), DawgModel.class, DawgModel.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChimpsToken> getSavedTokens() {
        return template.find(new Query(), ChimpsToken.class, ChimpsToken.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToken(ChimpsToken token) {
        template.save(getDBObject(token, "token"), ChimpsToken.class.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> getAllTags() {
        Map<String, Object>[] stbs = findAll();
        Collection<String> tags = new HashSet<String>();
        for(Map<String, Object> stb : stbs) {
            Collection<String> stbTags = (Collection<String>) stb.get(MetaStb.TAGS);
            if (stbTags != null) {
                tags.addAll(stbTags);
            }
        }
        return tags;
    }
}
