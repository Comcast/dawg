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
package com.comcast.dawg;

import com.comcast.video.dawg.common.Config;

/**
 * Provides locations of the serversthat are to be tested on. This will look up system properties for
 * the various serversand apply defaults to the -dev servers if they are not set
 * @author Kevin Pearson
 *
 */
public class TestServers {
    public static final String DAWG_HOUSE_KEY = "dawg.house.url";
    public static final String DAWG_POUND_KEY = "dawg.pound.url";
    public static final String DAWG_SHOW_KEY  = "dawg.show.url";

    public static final String DEFAULT_DAWG_HOUSE = Config.get("testing", "default-dawg-house", "http://localhost/dawg-house/");
    public static final String DEFAULT_DAWG_POUND = Config.get("testing", "default-dawg-pound", "http://localhost/dawg-pound/");
    public static final String DEFAULT_DAWG_SHOW  = Config.get("testing", "default-dawg-show",  "http://localhost/dawg-show/");

    /**
     * Gets the url to the dawg-house server to be tested on
     * @return
     */
    public static final String getHouse() {
        return getUrl(DAWG_HOUSE_KEY, DEFAULT_DAWG_HOUSE);
    }

    /**
     * Gets the url to the dawg-pound server to be tested on
     * @return
     */
    public static final String getPound() {
        return getUrl(DAWG_POUND_KEY, DEFAULT_DAWG_POUND);
    }

    /**
     * Gets the url to the dawg-show server to be tested on
     * @return
     */
    public static final String getShow() {
        return getUrl(DAWG_SHOW_KEY, DEFAULT_DAWG_SHOW);
    }

    private static final String getUrl(String propKey, String defaultVal) {
        String propVal = System.getProperty(propKey, defaultVal);
        return propVal + (propVal.endsWith("/") ? "" : "/");
    }
}
