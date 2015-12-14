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
package com.comcast.video.dawg.show;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * Class that checks if a browser is supported.
 * Chrome does not work on IOS because it cannot play mjpegs.
 * Default Android browser does not work because it also cannot play mjpegs
 * @author Kevin Pearson
 *
 */
public class BrowserSupport implements ViewConstants {

    public static final Map<OperatingSystem, Set<Browser>> SUPPORTED = new HashMap<OperatingSystem, Set<Browser>>();
    /** Need to have this because UserAgentUtils seems to have a bug where it put CHROME_MOBILE in the Safari Group */
    public static final Set<Browser> UNSUPPORTED = set(Browser.CHROME_MOBILE);

    static {
        SUPPORTED.put(OperatingSystem.LINUX, set(Browser.FIREFOX, Browser.CHROME));
        SUPPORTED.put(OperatingSystem.WINDOWS, set(Browser.FIREFOX, Browser.CHROME));
        SUPPORTED.put(OperatingSystem.IOS, set(Browser.SAFARI));
        SUPPORTED.put(OperatingSystem.ANDROID, set(Browser.FIREFOX));
        SUPPORTED.put(OperatingSystem.MAC_OS_X, set(Browser.FIREFOX, Browser.CHROME, Browser.SAFARI));
    };

    /**
     * Helper method that creates a set with the given items
     * @param items The items to initialize the set with
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> Set<T> set(T... items) {
        Set<T> set = new HashSet<T>();
        for (T item : items) {
            set.add(item);
        }
        return set;
    }

    /**
     * Returns true if the browser is supported by dawg-show. Browser compatibility is mainly
     * determined by its ability to show the motion jpeg from the axis video server.
     * @param userAgentString The user agent string that will be parsed
     * @return
     */
    public static boolean isBrowserSupported(String userAgentString) {
        UserAgent ua = UserAgent.parseUserAgentString(userAgentString);
        OperatingSystem osGroup = ua.getOperatingSystem().getGroup();
        Browser browser = ua.getBrowser();
        Set<Browser> supportedBrowsers = SUPPORTED.get(osGroup);
        return (supportedBrowsers != null) && supportedBrowsers.contains(browser.getGroup()) && !UNSUPPORTED.contains(browser);
    }
}
