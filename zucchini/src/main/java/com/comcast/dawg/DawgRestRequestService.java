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
package com.comcast.dawg;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * Class which handles various REST requests in dawg house automation.
 * @author Jeeson 
 *
 */
public class DawgRestRequestService {
    /** Logger object for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgRestRequestService.class);
    // REST URL to send request
    private String url = null;
    // HTTP Method to execute
    private Method reqMethod = null;
    // Map holding header params to be set with the rest request
    private Map<String, Object> headerMap = new HashMap<String, Object>();
    // Request params to set while sending a rest request, if any
    private Map<String, String> paramMap = new HashMap<String, String>();
    // Represents the body to be set with the rest request
    private Object body = null;
    //Represents the content type of the request
    private String contentType = null;

    /**
     * Constructor. URL & Request Method is compulsory to make a REST request.
     * 
     * @param restUrl
     *            - REST url to send request
     * @param method
     *            - REST request method to invoke
     */
    public DawgRestRequestService(String restUrl, Method method) {
        this.url = restUrl;
        this.reqMethod = method;
    }

    /**
     * Method to send the REST Request to dawg house/dawg pound.
     * 
     * @return Response
     *          Receives the response from dawg-house/dawg-pound host
     * @throws DawgTestException
     */
    public Response sendRequest() throws DawgTestException {
        Response response = null;
        // Validates the REST url & Request Method Type.
        if (null == reqMethod || null == url) {
            LOGGER.error("Rest request configuration is not valid (URL: " + url + ". Request Type: " + reqMethod);
        }
        // Create RequestSpecification
        RequestSpecification sp = given();

        // Set default header configuration based on request type
        if (!headerMap.isEmpty()) {
            //sets header params
            sp.headers(headerMap);
        }
        if (!paramMap.isEmpty()) {
            //sets Path params
            sp.pathParams(paramMap);
        }
        if (null != contentType) {
            sp.contentType(contentType);
        }
        if (null != body) {
            // set request body, if set
            sp.body(body);
        }

        // Execute REST Requests
        switch (reqMethod) {
            case GET:
                response = sp.and().get(url);
                break;
            case POST:
                response = sp.and().post(url);
                break;
            case DELETE:
                response = sp.and().delete(url);
                break;
            case PUT:
                response = sp.and().put(url);
                break;
            default:
                throw new DawgTestException("Invalid HTTP Request Method:  " + reqMethod);
        }
        // Validate the Response
        if (null != response) {
            // Set the Pairing service request response to TestContext for further validations
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_REST_RESPONSE, response);
            LOGGER.info("\nStatus Code: " + response.getStatusCode());
            LOGGER.info("\nResponse: " + response.asString());
        } else {
            LOGGER.error("Response received", response);
        }
        return response;
    }

    /**
     * Method to set Request Path Parameters, if any
     * @param map - Parameter Map 
     * @return DawgRestRequestService
     */
    public DawgRestRequestService setRequestPathParams(Map<String, String> map) {
        paramMap = map;
        return this;
    }

    /**
     * Method to set Header Parameters, if any
     * 
     * @param map
     *            - Header Map
     * @return DawgRestRequestService
     */
    public DawgRestRequestService setRequestHeader(Map<String, Object> map) {
        this.headerMap = map;
        return this;
    }

    /**
     * Method to set request body
     * 
     * @param body
     *            - body to set
     * @return DawgRestRequestService
     */
    public DawgRestRequestService setRequestBody(Object body) {
        this.body = body;
        return this;
    }

    /**
     * Method to set content type
     * @param type
     * @return DawgRestRequestService
     */
    public DawgRestRequestService setContentType(String type) {
        this.contentType = type;
        return this;
    }

}
