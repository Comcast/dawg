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

import com.comcast.video.dawg.common.MetaStb;

/**
 * Entry into the cache for stb metadata. This will hold the metadata as well as the time
 * it was last used.
 * @author Kevin Pearson
 *
 */
public class MetaStbCacheEntry {
    private MetaStb metaStb;
    private long lastUsed;

    /**
     * Creates a MetaStbCacheEntry
     * @param metaStb The stb metadata
     * @param lastUsed The unix time it was last used
     */
    public MetaStbCacheEntry(MetaStb metaStb, long lastUsed) {
        setMetaStb(metaStb);
        setLastUsed(lastUsed);
    }

    public MetaStb getMetaStb() {
        return metaStb;
    }

    public void setMetaStb(MetaStb metaStb) {
        this.metaStb = metaStb;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

}
