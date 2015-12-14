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
package com.comcast.video.dawg.house;

import java.util.Collection;
import java.util.Map;

import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;

/**
 * The {@link IDawgHouseClient} defines all the methods for interacting
 * with dawg-house and producing objects extending DawgDevice
 * @author Val Apgar
 *
 */
public interface IDawgHouseClient<T extends DawgDevice> {

    /**
     * Get a DawgDevice by id.
     */
    public T getById(String id);

    /**
     * Get all DawgDevices in the data store.
     */
    public Collection<T> getAll();

    /**
     * Get a {@link Collection} of DawgDevices by ids.
     */
    public Collection<T> getByIds(Collection<String> ids);

    /**
     * Get a {@link Collection} of DawgDevices from the provided query.
     */
    public Collection<T> getByQuery(String query);

    /**
     * Delete a DawgDevice by id
     */
    public void delete(String id);

    /**
     * Delete all DawgDevices matching the {@link Collection} of ids
     */
    public void deleteByIds(Collection<String> id);

    /**
     * Delete a DawgDevice
     */
    public void delete(T stb);

    /**
     * Delete a {@link Collection} of stbs
     */
    public void delete(Collection<T> stbs);

    /**
     * Add or replace a DawgDevice
     */
    public void add(T stb);

    /**
     * Add or replace a {@link Collection} of DawgDevices
     */
    public void add(Collection<T> stbs);

    /**
     * Update a {@link Collection} of DawgDevice by matching id with the provided {@link Map} of data
     * @param id The {@link Collection} of matching DawgDevice ids to update
     * @param data The {@link Map} of data to update
     */
    public void update(Collection<String> id, Map<String,Object> data);

    /**
     * Persists the given stb if it exists
     * @param stb the stb to update
     */
    public void update(MetaStb stb);

    /**
     * Gets all the dawg models that are configured from the dawg house server
     * @param modelNames The names of the models to get
     * @return
     */
    public DawgModel[] getDawgModels(String... modelNames);
}
