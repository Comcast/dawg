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
package com.comcast.dawg.test.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

import com.comcast.dawg.TestServers;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;

/**
 * Test base class which enable test to track execution cycle.
 */
@Listeners(TestListener.class)
public class TestBase {

    /** Test logger. */
    protected static Logger logger = Logger.getLogger(TestBase.class);

    /** To cache the test STBs for later deletion. */
    protected Map<String, MetaStb> testStbs = new HashMap<String, MetaStb>();

    private DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServers.getHouse());

    /**
     * After class method to remove all the added test STB's.
     */
    @AfterClass
    public void deleteStbs() {
        try {
            if (!testStbs.isEmpty()) {
                dawgHouseClient.deleteByIds(testStbs.keySet());
            }
        } finally {
            IOUtils.closeQuietly(dawgHouseClient);
        }
    }

    /**
     * Gets the status of the HttpResponse.
     *
     * @param   response  the http response.
     *
     * @return  the response status.
     */
    protected int getStatus(HttpResponse response) {
        int status = 0;

        if (null != response) {
            StatusLine statusLine = response.getStatusLine();

            if (null != statusLine) {
                status = statusLine.getStatusCode();
            }

        }

        return status;
    }

    /**
     * Delete the STB.
     *
     * @param  stb  Meta stb object.
     */
    protected void deleteStb(MetaStb stb) {
        logger.info("About to delete device : " + stb.getId());
        dawgHouseClient.delete(stb);

        logger.info("Successfully deleted device : " + stb.getId());

        if (testStbs.containsKey(stb.getId())) {
            testStbs.remove(stb);
        }
    }

    /**
     * Add the STB content and cache it for later deletion after completion of test execution.
     *
     * @param  stb  Meta stb object to be added.
     */
    protected void addStb(MetaStb stb) {
        dawgHouseClient.add(stb);
        testStbs.put(stb.getId(), stb);
    }
}
