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
package com.comcast.dawg.config;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.video.dawg.common.Config;
import com.comcast.zucchini.TestContext;

/**
 * Provides locations of the servers that are to be tested on. This will look up system properties for
 * the various servers and apply defaults to the dev servers if they are not set
 * 
 * @author Kevin Pearson
 */
public class TestServerConfig {
    public static final String DAWG_HOUSE_KEY = "dawg.house.url";
    public static final String DAWG_POUND_KEY = "dawg.pound.url";
    public static final String DAWG_SHOW_KEY = "dawg.show.url";
    public static final String DAWG_HOUSE_USERNAME = "dawg.house.username";
    public static final String DAWG_HOUSE_PASSWORD = "dawg.house.password";

    public static final String DEFAULT_DAWG_HOUSE = Config.get("testing", "default-dawg-house");
    public static final String DEFAULT_DAWG_POUND = Config.get("testing", "default-dawg-pound");
    public static final String DEFAULT_DAWG_SHOW = Config.get("testing", "default-dawg-show");
    public static final String DEFAULT_DAWG_HOUSE_USERNAME = Config.get("testing", "dawg-house-username");
    public static final String DEFAULT_DAWG_HOUSE_PASSWORD = Config.get("testing", "dawg-house-password");

    /**
     * Gets the url to the dawg-house server to be tested on
     * @return - dawg house url
     */
    public static final String getHouse() {
        return getValue(DawgHouseConstants.CONTEXT_DAWG_HOUSE_URL, DAWG_HOUSE_KEY, DEFAULT_DAWG_HOUSE);
    }

    /**
     * Gets the url to the dawg-pound server to be tested on
     * @return - dawg pound url
     */
    public static final String getPound() {
        return getValue(DawgHouseConstants.CONTEXT_DAWG_POUND_URL, DAWG_POUND_KEY, DEFAULT_DAWG_POUND);
    }

    /**
     * Gets the url to the dawg-show server to be tested on
     * @return - dawg show url
     */
    public static final String getShow() {
        return getValue(DawgHouseConstants.CONTEXT_DAWG_SHOW_URL, DAWG_SHOW_KEY, DEFAULT_DAWG_SHOW);
    }

    /**
     * Gets the dawg house username to login in 
     * @return - dawg house login user name
     */
    public static final String getUsername() {
        String username = getValue(DawgHouseConstants.CONTEXT_USERNAME, DAWG_HOUSE_USERNAME,
            DEFAULT_DAWG_HOUSE_USERNAME);
        return username.substring(0, username.length() - 1);
    }

    /**
     * Gets the dawg house password to login in 
     * @return - dawg house login password
     */
    public static final String getPassword() {
        String password = getValue(DawgHouseConstants.CONTEXT_PASSWORD, DAWG_HOUSE_PASSWORD,
            DEFAULT_DAWG_HOUSE_PASSWORD);
        return password.substring(0, password.length() - 1);
    }

    /**
     * Get values, if  system properties are not set then fetch default values from config file
     * @param contextVal 
     * @param property key
     * @param default value
     * @return corresponding system property value or config value
     */
    private static final String getValue(String contextKey, String propKey, String defaultVal) {
        String contextVal = TestContext.getCurrent().get(contextKey);
        if (null == contextVal) {
            String propVal = System.getProperty(propKey, defaultVal);
            contextVal = propVal + (propVal.endsWith("/") ? "" : "/");
            TestContext.getCurrent().set(contextKey, contextVal);
        }
        return contextVal;
    }
}
