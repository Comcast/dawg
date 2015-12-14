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

import java.util.Arrays;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.pantry.test.Wiring;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.dawg.show.DawgShowConfiguration;
import com.comcast.video.dawg.show.util.TestUtils;

public class MetaStbCacheTest {

    class MockDawgHouseClient extends DawgHouseClient {

        public MetaStb getById(String id) {
            return TestUtils.createGenericMetaStb();
        }

        @Override
        public Collection<MetaStb> getByIds(Collection<String> ids) {
            return Arrays.asList(TestUtils.createGenericMetaStb());
        }
    }

    @Test
    public void testGetMetaStb() throws InterruptedException {
        final MockDawgHouseClient client = new MockDawgHouseClient();

        MetaStbCache cache = new MetaStbCache();
        DawgShowConfiguration config = new DawgShowConfiguration();
        config.put(DawgShowConfiguration.DAWG_HOUSE_URL, "http://www.dawg-house.com");
        Wiring.autowire(cache, config);
        Assert.assertNotNull(cache.getDawgHouseClient()); // really just for code coverage


        cache = new MetaStbCache() {
            protected DawgHouseClient getDawgHouseClient() {
                return client;
            }
        };

        cache.setCacheTime(500);
        Assert.assertEquals(500, cache.getCacheTime());
        MetaStb stb1 = cache.getMetaStb(TestUtils.ID);
        MetaStb stb2 = cache.getMetaStb(TestUtils.ID);
        Thread.sleep(1000);
        MetaStb stb3 = cache.getMetaStb(TestUtils.ID);

        Assert.assertEquals(stb1.getId(), TestUtils.ID);
        Assert.assertEquals(stb1.getName(), TestUtils.NAME);
        Assert.assertTrue(stb1 == stb2); // proves got a cached version of the metastb
        Assert.assertFalse(stb2 == stb3); // proves a new metastb was created because it expired
        Assert.assertEquals(stb3.getId(), TestUtils.ID);
        Assert.assertEquals(stb3.getName(), TestUtils.NAME);
    }
}
