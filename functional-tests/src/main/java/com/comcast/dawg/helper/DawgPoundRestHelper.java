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
package com.comcast.dawg.helper;

import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.RestURIConfig;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.video.dawg.house.Reservation;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;

import org.apache.http.HttpStatus;

/**
 * Helper functionalities for accessing Dawg Pound REST services
 * @author Jeeson
 */
public class DawgPoundRestHelper {

    /** lock object */
    private static Object lock = new Object();
    private static DawgPoundRestHelper dprestHelper;

    /**
     * Creates single instance of restHelper
     * @return restHelper
     */
    public static DawgPoundRestHelper getInstance() {
        synchronized (lock) {
            if (null == dprestHelper) {
                dprestHelper = new DawgPoundRestHelper();
            }
        }
        return dprestHelper;
    }

    /**
     * Reserve an STB in dawg pound via POST request
     * @param reservation 
     *          STB reservation params
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean reserveStbInDawg(Reservation reservation) throws DawgTestException {
        StringBuilder postFixUrl = new StringBuilder();
        postFixUrl.append(reservation.isForce() ? "/override" : "");
        postFixUrl.append("/" + reservation.getToken()).append("/" + reservation.getDeviceId()).append(
            "/" + reservation.getExpiration());
        String finalUrl = RestURIConfig.RESERVE_STB_URI.buildURL(TestServerConfig.getPound()) + postFixUrl.toString();
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(finalUrl, Method.POST);

        // Sending POST request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }
}
