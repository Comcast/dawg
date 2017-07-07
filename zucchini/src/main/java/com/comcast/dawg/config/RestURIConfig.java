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
package com.comcast.dawg.config;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;

/**
 * Enum defined for various REST request URLs in Dawg house/Dawg pound
 * @author priyanka.sl
 */
public enum RestURIConfig {
    //@formatter:off
    ADD_UPDATE_MODEL_URI(DawgHouseConstants.ADD_OR_UPDATE_MODEL, TestConstants.ADD_OR_UPDATE_MODEL_URI),
    GET_STB_MODEL_URI(DawgHouseConstants.GET_MODEL, TestConstants.GET_STB_MODEL_URI),
    ASSIGN_MODELS_URI(DawgHouseConstants.ASSIGN_MODEL, TestConstants.GET_STB_MODEL_URI),
    UPDATE_TAG_REST_URI(DawgHouseConstants.ADD_OR_REMOVE_TAG, TestConstants.TAG_UPDATE_REST_URI), 
    ADD_REMOVE_STB_REST_URI(DawgHouseConstants.ADD_OR_REMOVE_STB, TestConstants.ADD_OR_REMOVE_STB_REST_URI),
    MODEL_CONFIG_PAGE_URI(DawgHouseConstants.MODEL_CONFIG,TestConstants.MODEL_CONFIG_URI);
   //@formatter:on

    private String operation;

    private String requestURI;

    /**
     * Get the REST request URI for performing various dawg house operations
     * @param operation operations associated with various REST URI
     * @return RestURIConfig
     * @throws DawgTestException
     */

    public static RestURIConfig getReqURI(String operation) throws DawgTestException {
        RestURIConfig uriConfig = null;
        for (RestURIConfig url : RestURIConfig.values()) {
            if (url.getOperation().equals(operation)) {
                uriConfig = url;
                break;
            }
        }
        if (null == uriConfig) {
            throw new DawgTestException(operation + " not defined in RestURIConfig");
        }
        return uriConfig;
    }

    /**
     * Enum constructor
     * 
     * @param operation various dawg house operations 
     * @param requestURI rest request UI end point
     */
    private RestURIConfig(String operation, String restURI) {
        this.operation = operation;
        this.requestURI = restURI;

    }

    /**
     * Method returns various dawg-house operations associated with REST URI 
     *
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Method returns the REST request url associated with the enum constant
     *
     * @return requestURI
     */
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Creates the dawg house REST URI by appending the dawg house host with end points
     * @return String URI for various REST requests
     */
    public String buildURL() {
        StringBuilder restURI = new StringBuilder();
        restURI.append(TestServerConfig.getHouse());
        // Append DawgHouse host to endpoint       
        return restURI.append(getRequestURI()).toString();
    }

}
