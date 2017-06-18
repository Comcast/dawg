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
package com.comcast.dawg.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.jayway.restassured.response.Response;

/**
 * Common Utility class for handling REST related activities in dawg house automation
 * @author Jeeson
 *
 */
public class DawgCommonRestUtils {
    private static DawgCommonRestUtils restReqUtils = null;
    private static Object lock = new Object();

    /**
     * Creates single instance of DawgCommonRestReqUtils
     * 
     * @return DawgCommonRestReqUtils
     */
    public static DawgCommonRestUtils getInstance() {
        synchronized (lock) {
            if (null == restReqUtils) {
                restReqUtils = new DawgCommonRestUtils();
            }
        }
        return restReqUtils;
    }

    /**
     * Method perform login to dawg house and sets Cookie generated 
     * @return String
     *          Cookie generated during dawg house login
     * @throws DawgTestException     
     */
    public String getDawgCookie() throws DawgTestException {
        String cookie = null;
        DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServerConfig.getHouse());
        try {
            // logging into dawg house
            dawgHouseClient.login(TestServerConfig.getUsername(), TestServerConfig.getPassword());
            // Accessing the cookies set
            String dawgCookies = dawgHouseClient.getCookieStore().getCookies().toString();
            if (null == dawgCookies) {
                throw new DawgTestException("Failed to get cookie during dawg house login");
            }
            // Accessing the cookie corresponding to dawt          
            Matcher matcher = Pattern.compile(DawgHouseConstants.DAWG_COOKIE_PATTERN).matcher(dawgCookies);
            if (!matcher.find()) {
                throw new DawgTestException("Failed to access cookie from list of available cookies");
            }
            cookie = DawgHouseConstants.DAWG_COOKIE_PREFIX + matcher.group(1);

        } finally {
           // IOUtils.closeQuietly(dawgHouseClient);
        }
        return cookie;
    }

    /**
     * Returns REST response for (dawg house/ dawg pound) REST requests
     * @param dawgRestReqService
     * @return Response
     *          Dawg request response
     * @throws DawgTestException
     */
    public Response getRestResponse(DawgRestRequestService dawgRestReqService) throws DawgTestException {
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put(DawgHouseConstants.COOKIE, this.getDawgCookie());
        // Setting REST request header for dawg house/dawg pound
        return dawgRestReqService.setRequestHeader(headerMap).sendRequest();
    }


}
