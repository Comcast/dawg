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
package com.comcast.video.dawg.show.util;

import java.util.HashMap;
import java.util.Map;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.exception.HttpRuntimeException;
import com.comcast.video.dawg.show.cache.MetaStbCache;

public class MockMetaStbCache extends MetaStbCache {
    public boolean throwExc = false;
    public Map<String, MetaStb> cache = new HashMap<String, MetaStb>();

    @Override
    public MetaStb getMetaStb(String deviceId, boolean refresh) {
        if (throwExc) {
            throw new HttpRuntimeException("Could not communicate with server");
        }
        return cache.get(deviceId);
    }
}
