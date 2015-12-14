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

import static com.comcast.video.dawg.util.DawgUtil.toLowerAlphaNumeric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.dawg.show.DawgShowConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cache that holds metadata for stbs
 * @author Kevin Pearson
 *
 */
@Component
public class MetaStbCache {
    public static final long DEFAULT_CACHE_TIME = TimeUnit.MINUTES.toMillis(30);
    private Map<String, MetaStbCacheEntry> cache =  new HashMap<String, MetaStbCacheEntry>();

    private long cacheTime = DEFAULT_CACHE_TIME;

    @Autowired
    private DawgShowConfiguration config;

    /**
     * Gets the metadata for the stb with the given deviceId. If there is no cached object then
     * it will go out to the client to get the information. This will change to going out to dawg-house.
     * @param deviceId The id of the device to get
     * @return
     */
    public MetaStb getMetaStb(String deviceId) {
        return getMetaStb(deviceId, false);
    }

    /**
     * Gets the metadata for the stb with the given deviceId. If there is no cached object then
     * it will go out to the client to get the information. This will change to going out to dawg-house.
     * @param deviceId The id of the device to get
     * @param refresh true if the cached stb should be refreshed
     * @return
     */
    public MetaStb getMetaStb(String deviceId, boolean refresh) {
        Collection<MetaStb> stbs = getMetaStbs(new String[] {deviceId}, refresh);
        return stbs.size() > 0 ? stbs.iterator().next() : null;
    }

    /**
     * Gets the metadata for a list of device ids. If there is no cached object then
     * it will go out to the client to get the information. This will change to going out to dawg-house.
     * @param deviceIds The ids of the devices to get metadata for
     * @return
     */
    public Collection<MetaStb> getMetaStbs(String[] deviceIds) {
        return getMetaStbs(deviceIds, false);
    }

    /**
     * Gets the metadata for a list of device ids. If there is no cached object then
     * it will go out to the client to get the information. This will change to going out to dawg-house.
     * @param deviceIds
     * @param refresh true if the cached stb should be refreshed
     * @return
     */
    public Collection<MetaStb> getMetaStbs(String[] deviceIds, boolean refresh) {
        Collection<MetaStb> stbs = new ArrayList<MetaStb>();
        Collection<String> uncachedDeviceIds = new ArrayList<String>();
        for (String deviceId : deviceIds) {
            deviceId = toLowerAlphaNumeric(deviceId);
            MetaStbCacheEntry entry = cache.get(deviceId);
            if ((entry == null) || refresh) {
                uncachedDeviceIds.add(deviceId);
            } else {
                long life = System.currentTimeMillis() - entry.getLastUsed();
                if (life > cacheTime) {
                    uncachedDeviceIds.add(deviceId);
                } else {
                    entry.setLastUsed(System.currentTimeMillis());
                    stbs.add(entry.getMetaStb());
                }
            }
        }
        if (!uncachedDeviceIds.isEmpty()) {
            DawgHouseClient dawgClient = getDawgHouseClient();
            Collection<MetaStb> gotStbs = dawgClient.getByIds(uncachedDeviceIds);
            for (MetaStb stb : gotStbs) {
                putMetaStb(stb);
            }
            stbs.addAll(gotStbs);
        }
        return stbs;
    }

    protected DawgHouseClient getDawgHouseClient() {
        return new DawgHouseClient(config.getDawgHouseUrl());
    }

    /**
     * Puts the given stb in the cache
     * @param stb The stb to put in the cache
     */
    public MetaStbCacheEntry putMetaStb(MetaStb stb) {
        return putMetaStb(toLowerAlphaNumeric(stb.getMacAddress()), stb);
    }

    /**
     * Puts the given stb in the cache
     * @param stb The stb to put in the cache
     * @param deviceId The deviceId to put the stb under
     */
    public MetaStbCacheEntry putMetaStb(String deviceId, MetaStb stb) {
        MetaStbCacheEntry entry = new MetaStbCacheEntry(stb, System.currentTimeMillis());
        cache.put(deviceId, entry);
        return entry;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

}
