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

import java.util.Map;

import com.comcast.video.dawg.common.DawgDevice;

public interface HouseService {

    public Map<String,Object>[] getStbsById(String... id);
    public Map<String,Object>[] getStbsByKey(String... keys);
    public Map<String,Object>[] getStbsByQuery(String query);
    public Map<String,Object>[] getAll();

    /**
     * Get all devices which are matching with the provided tag.
     *
     * @param tag
     *            the tag to be searched
     * @param query
     *            the query to be search along with tag value
     * @return an array of devices that were found using the provided tag and query.
     */
    public Map<String, Object>[] getStbsByTag(String tag, String query);

    public Map<String,Object> getById(String id);
    public void deleteStbById(String... id);

    /**
     * Inserts a device if does not exist, otherwise updates it
     * This is the same as {@link HouseService#replace(DawgDevice...)} except when updating properties not specified in the given stbs
     * will be set to their existing values. In replace, properties not specified in the given stbs will be set to null
     * @param stbs The stbs to upsert
     */
    public void upsertStb(DawgDevice... stb);

    /**
     * This is the same as {@link HouseService#upsertStb(DawgDevice...)} except when updating properties not specified in the given stbs
     * will be set back to null. In upsertStb, properties not specified in the given stbs will remain as their original
     * values
     * @param stbs The stbs to replace
     */
    public void replace(DawgDevice... stbs);

}
