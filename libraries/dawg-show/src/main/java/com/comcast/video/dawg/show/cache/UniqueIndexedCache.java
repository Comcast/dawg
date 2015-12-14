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
package com.comcast.video.dawg.show.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class that allows storing of objects with a jvm-unique id
 * @author Kevin Pearson
 *
 * @param <T>
 */
public class UniqueIndexedCache<T> {
    private Map<String, T> items = new HashMap<String, T>();

    /**
     * Stores an object in the cache and then return its id
     * @param item The item to store
     * @return
     */
    public String storeItem(T item) {
        String id = UUID.randomUUID().toString();
        items.put(id, item);
        return id;
    }

    /**
     * Gets the item for the given id
     * @param id The id of the item to get
     * @return
     */
    public T getItem(String id) {
        return getItem(id, false);
    }

    /**
     * Gets the item for the given id
     * @param id The id of the item to get
     * @param remove true if the item should be removed from the cache after
     * @return
     */
    public T getItem(String id, boolean remove) {
        T item = items.get(id);
        if (item == null) {
            throw new CacheItemNotFoundException("Not item was found for '" + id + "'");
        }
        if (remove) {
            items.remove(item);
        }
        return item;
    }
}
