/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg.saucelab;

import com.comcast.video.dawg.common.Config;

/**
 * contains the sauce lab credentials and key informations to run tests in sauce. This will look up system properties 
 * and apply defaults if they are not set
 * 
 * @author Priyanka
 *
 */
public class SauceProvider {

    private static final String TEST_MODE = "test.mode";
    private static final String SAUCE_KEY = "sauce.key";
    private static final String SAUCE_USERNAME = "sauce.username";
    private static final String SAUCE_PORT = "sauce.port";
    private static final String SAUCE_PLATFORM = "test.platform";

    private static final String SAUCE_WIN_VERSION = "win.version";
    private static final String SAUCE_MAC_VERSION = "mac.version";
    private static final String SAUCE_LINUX_VERSION = "linux.version";
    private static final String CHROME_VERSION = "chrome.version";

    private static final String DEFAULT_TEST_MODE = Config.get("saucelab", "test-mode", "local");
    private static final String DEFAULT_SAUCE_KEY = Config.get("saucelab", "sauce-key",
        "c420f9b5-524c-4083-9bef-4ca0c4e6f15c");
    private static final String DEFAULT_SAUCE_USERNAME = Config.get("saucelab", "sauce-username", "CSVTE");
    private static final String DEFAULT_SAUCE_PORT = Config.get("saucelab", "sauce-port", "4445");
    private static final String DEFAULT_SAUCE_PLATFORM = Config.get("saucelab", "sauce-platform", "windows");
    private static final String DEFAULT_WIN_VERSION = Config.get("saucelab", "sauce-win-version", "Windows 8.1");
    private static final String DEFAULT_MAC_VERSION = Config.get("saucelab", "sauce-mac-version", "macOS 10.12");
    private static final String DEFAULT_LINUX_VERSION = Config.get("saucelab", "sauce-linux-version", "Linux");
    private static final String DEFAULT_CHROME_VERSION = Config.get("saucelab", "chrome-version", "46");


    /**
     * Gets the test mode to run tests in local or in saucelabs
     * @return
     */
    public static final String getTestMode() {
        return getData(TEST_MODE, DEFAULT_TEST_MODE);
    }

    /**
     * Gets the sauce lab key
     * @return
     */
    public static final String getSauceKey() {
        return getData(SAUCE_KEY, DEFAULT_SAUCE_KEY);
    }

    /**
     * Gets the username of saucelab
     * @return
     */
    public static final String getSauceUserName() {
        return getData(SAUCE_USERNAME, DEFAULT_SAUCE_USERNAME);
    }

    /**
     * Gets the sauce port
     * @return
     */
    public static final String getSaucePort() {
        return getData(SAUCE_PORT, DEFAULT_SAUCE_PORT);
    }

    /**
     * Gets the sauce platform to be test run
     * @return
     */
    public static final String getSaucePlatform() {
        return getData(SAUCE_PLATFORM, DEFAULT_SAUCE_PLATFORM);
    }

    /**
     * Gets the sauce platform version for windows
     * @return
     */
    public static final String getWinOsVersion() {
        return getData(SAUCE_WIN_VERSION, DEFAULT_WIN_VERSION);
    }

    /**
     *  Gets the sauce platform version for Mac OSX
     * @return
     */
    public static final String getMacOsVersion() {
        return getData(SAUCE_MAC_VERSION, DEFAULT_MAC_VERSION);
    }

    /**
     *  Gets the sauce platform version for Linux
     * @return
     */
    public static final String getLinuxOsVersion() {
        return getData(SAUCE_LINUX_VERSION, DEFAULT_LINUX_VERSION);
    }

    /**
     *  Get chrome version for running test in sauce labs.
     * @return
     */
    public static final String getChromeVersion() {
        return getData(CHROME_VERSION, DEFAULT_CHROME_VERSION);
    }

    private static final String getData(String propKey, String defaultVal) {
        return System.getProperty(propKey, defaultVal);
    }
}
