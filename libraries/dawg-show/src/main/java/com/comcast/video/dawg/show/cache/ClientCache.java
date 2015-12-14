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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import com.comcast.video.dawg.show.video.Compare;
import com.comcast.video.dawg.show.video.FrameUpdater;

/**
 * Caches some objects specific to the client's HttpSession
 * @author Kevin Pearson
 *
 */
public class ClientCache {

    public static final int CLIENT_INACTIVITY_TIMEOUT = (int) TimeUnit.HOURS.toMillis(2);

    private UniqueIndexedCache<Compare> compareCache = new UniqueIndexedCache<Compare>();
    private UniqueIndexedCache<BufferedImage> imgCache = new UniqueIndexedCache<BufferedImage>();
    private Map<String, FrameUpdater> frameUpdaters = new HashMap<String, FrameUpdater>();

    /**
     * Gets the cache that holds to current comparisons
     * @return
     */
    public UniqueIndexedCache<Compare> getCompareCache() {
        return compareCache;
    }

    public void setCompareCache(UniqueIndexedCache<Compare> compareCache) {
        this.compareCache = compareCache;
    }

    /**
     * Gets the cache that holds snapped images
     * @return
     */
    public UniqueIndexedCache<BufferedImage> getImgCache() {
        return imgCache;
    }

    public void setImgCache(UniqueIndexedCache<BufferedImage> imgCache) {
        this.imgCache = imgCache;
    }

    public FrameUpdater getFrameUpdater(String deviceId) {
        return frameUpdaters.get(deviceId);
    }

    public FrameUpdater storeFrameUpdater(String deviceId, FrameUpdater frameUpdater) {
        return frameUpdaters.put(deviceId, frameUpdater);
    }

    /**
     * Method to get the ClientCache for a given session. If the client
     * does not have a cache then one is created
     * @param session The http session for the client
     * @return
     */
    public static ClientCache getClientCache(HttpSession session) {
        synchronized (session) {
            ClientCache clientCache = (ClientCache) session.getAttribute(ClientCache.class.getSimpleName());
            if (clientCache == null) {
                clientCache = new ClientCache();
                session.setAttribute(ClientCache.class.getSimpleName(), clientCache);
                session.setMaxInactiveInterval(CLIENT_INACTIVITY_TIMEOUT);
            }
            return clientCache;
        }
    }
}
