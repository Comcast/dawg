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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.controller.house.ChimpsToken;

public interface ParkService {

    public Map<String,Object>[] findAll();
    public Map<String,Object>[] findAll(Pageable pageable);
    public Map<String,Object>[] findByCriteria(Criteria criteria, Pageable pageable);
    public Map<String,Object>[] findByKeys(String[] keys);
    public Map<String,Object>[] findByKeys(String[] keys, Pageable pageable);

    /**
     * Adds a list of model to the database
     * @param models The list of models to add
     */
    public void upsertModel(DawgModel... models);

    /**
     * Gets all models in the database
     * @return
     */
    public List<DawgModel> getModels();

    /**
     * Deletes the model with the given name
     * @param modelName
     */
    public void deleteModel(String modelName);

    /**
     * Gets the model with the given name
     * @param modelName
     * @return
     */
    public DawgModel getModelById(String modelName);

    /**
     * Gets the models with the given names
     * @param modelName
     * @return
     */
    public List<DawgModel> getModelsByIds(String... modelNames);

    /**
     * Gets the CHIMPS tokens that have been used to populate dawg house
     * @return
     */
    public List<ChimpsToken> getSavedTokens();

    /**
     * Saves a chimps token
     * @param token The CHIMPS token to save
     */
    public void saveToken(ChimpsToken token);

    /**
     * Aggregates all the tags from all devices
     * @return
     */
    public Collection<String> getAllTags();

}
