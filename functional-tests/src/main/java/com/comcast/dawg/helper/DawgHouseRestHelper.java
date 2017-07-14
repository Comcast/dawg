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

import java.util.ArrayList;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.RestURIConfig;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.dawg.MetaStbBuilder;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper functionalities for accessing Dawg House REST services
 * @author Jeeson
 *
 */
public class DawgHouseRestHelper {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgHouseRestHelper.class);

    /** lock object */
    private static Object lock = new Object();
    private static DawgHouseRestHelper dhrestHelper;

    /**
     * Creates single instance of restHelper
     * @return restHelper
     */
    public static DawgHouseRestHelper getInstance() {
        synchronized (lock) {
            if (null == dhrestHelper) {
                dhrestHelper = new DawgHouseRestHelper();
            }
        }
        return dhrestHelper;
    }

    /**
     * Add an STB model to dawg house via POST request
     * @param modelName
     *          STB model name
     * @param capabilities
     *          STB model capabilities
     * @param family
     *          STB model family
     * @return boolean returns True if response is not null, have status code 200 and contains true
     * @throws DawgTestException
     */
    public boolean addSTBModelToDawg(String modelName, String capabilities, String family) throws DawgTestException {
        ArrayList<String> testStbModels = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODELS);
        String url = RestURIConfig.ADD_UPDATE_MODEL_URI.buildURL(TestServerConfig.getHouse()) + modelName;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.POST);
        String reqBody = new String("{\"name\":\"" + modelName + "\",\"capabilities\":[\"" + capabilities + "\"],\"family\":\"" + family + "\"}");

        // Setting content type and request body for POST
        Response response = dawgRestReqService.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(
            reqBody).sendRequest();

        if (null == testStbModels) {
            testStbModels = new ArrayList<String>();
        }
        if ((null != response) && (HttpStatus.SC_OK == response.getStatusCode()) && ("true".equals(
            response.asString()))) {
            // Storing added STB models to test context
            testStbModels.add(modelName);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_MODELS, testStbModels);
            return true;
        }
        return false;
    }

    /**
     * Send GET request to get an STB model from dawg house
     * @param modelId 
     *          STB model id
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean sendGetReqForSTBModel(String modelId) throws DawgTestException {
        String url = RestURIConfig.GET_STB_MODEL_URI.buildURL(TestServerConfig.getHouse()) + modelId;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Get an STB device from dawg house via GET request passing model name as query 
     * @param stbModelName 
     *          STB model name
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean sendGetReqByQueryForSTBDevice(String stbModelName) throws DawgTestException {
        String url = RestURIConfig.GET_STB_BY_QUERY.buildURL(TestServerConfig.getHouse()) + stbModelName;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Remove an STB model from dawg house
     * @param modelId
     *          STB model id
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException
     */
    public boolean removeSTBModelFromDawg(String modelId) throws DawgTestException {
        String url = RestURIConfig.ADD_UPDATE_MODEL_URI.buildURL(TestServerConfig.getHouse()) + modelId;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.DELETE);

        // Sending DELETE request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Check if given STB model is present in dawg house
     * @param modelId
     *          STB model id
     * @return boolean returns True if response is not null, have status code 200 and contains given model id
     * @throws DawgTestException
     */
    public boolean stbModelExistsInDawg(String modelId) throws DawgTestException {
        String url = RestURIConfig.GET_STB_MODEL_URI.buildURL(TestServerConfig.getHouse()) + modelId;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode()) && response.asString().contains(
            modelId);
    }

    /**
     * Populate STB reservation token via POST request
     * @param clientToken
     *          client token
     * @return boolean returns True if response is not null, have status code 200
     * @throws DawgTestException
     */
    public boolean populateStbInDawg(String clientToken) throws DawgTestException {
        String url = RestURIConfig.POPULATE_STB_URI.buildURL(TestServerConfig.getHouse()) + clientToken;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.POST);

        // Sending POST request
        Response response = dawgRestReqService.sendRequest();
        return (null != response);
    }


    /**
     * Check if given STB device is present in dawg house
     * @param deviceId
     *          STB device id
     * @return boolean returns True if response is not null, have status code 200 and contains given model id
     * @throws DawgTestException
     */
    public boolean stbDeviceExistsInDawg(String deviceId) throws DawgTestException {
        String url = RestURIConfig.ADD_REMOVE_STB_REST_URI.buildURL(TestServerConfig.getHouse()) + deviceId;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode()) && response.asString().contains(
            deviceId);
    }

    /**
     * Send POST request to retrieve STB from dawg house, passing device id in request body
     * @param deviceId
     *          STB device id
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean sendPostReqToRetrieveSTB(String deviceId) throws DawgTestException {
        String url = RestURIConfig.ADD_REMOVE_STB_REST_URI.buildURL(TestServerConfig.getHouse());
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.POST);

        // Sending POST request
        Response response = dawgRestReqService.setContentType(
            TestConstants.URL_ENCODED_CONTENT_TYPE_HEADER_VALUE).setRequestBody("id=" + deviceId).sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Creates MetaStb object for testing purpose.
     * @param deviceId
     *          STB device id
     * @param modelName
     *          STB model name
     * @param caps
     *          STB model capabilities
     * @param family
     *          STB model family name
     * @param mac
     *          STB MAC address
     * @return MetaStb
     *          test STB object.
     */
    public MetaStb createTestStb(String deviceId, String modelName, String caps, String family, String mac) {
        return MetaStbBuilder.build().id(deviceId).model(modelName).caps(caps).family(family).mac(mac).stb();
    }

    /**
     * Add an STB Device to dawg house via PUT request
     * @param id
     *          STB device id
     * @param mac
     *          STB MAC address
     * @param model
     *          STB model name
     * @param capability
     *          STB model capabilities
     * @param family
     *          STB model family name
     * @param make
     *          STB make
     * @param reqType
     *          RestReqType
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException
     */
    public boolean addStbToDawg(String id, String mac, String model, String capability, String family, String make, TestConstants.RestReqType reqType) throws DawgTestException {

        ArrayList<String> testStbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        String url = RestURIConfig.ADD_REMOVE_STB_REST_URI.buildURL(TestServerConfig.getHouse()) + id;
        String reqBody = createRestReqBodyStr(id, mac, model, capability, family, make, reqType);
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.PUT);

        // Sending PUT request
        Response response = dawgRestReqService.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(
            reqBody).sendRequest();

        if (null == testStbs) {
            testStbs = new ArrayList<String>();
        }
        if ((null != response) && (HttpStatus.SC_OK == response.getStatusCode()) && ("true".equals(
            response.asString()))) {
            // Storing added STBs to test context
            testStbs.add(id);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STBS, testStbs);
            return true;
        }
        return false;
    }

    /**
     * Creates the string equivalent of request body for 'add an STB' rest requests
     * @param id
     *          STB device id
     * @param mac
     *          STB MAC address
     * @param model
     *          STB model name
     * @param capability
     *          STB model capabilities
     * @param family
     *          STB model family name
     * @param make
     *          STB model make
     * @param reqType
     *          Type of request body
     * @return String
     *          String equivalent of REST request body with STB parameters
     */
    public String createRestReqBodyStr(String id, String mac, String model, String capability, String family, String make, TestConstants.RestReqType reqType) {
        String reqBody = null;
        switch (reqType) {
            case DETAILED_STB_DEVICE_PARAM:
                reqBody = new String("{\"id\":\"" + id + "\",\"macAddress\":\"" + mac + "\",\"model\":\"" + model + "\",\"capabilities\":" + capability + ",\"family\":\"" + family + "\"}");
                break;
            case BRIEF_STB_DEVICE_PARAM:
                reqBody = new String("{\"id\":\"" + id + "\", \"macAddress\":\"" + mac + "\", \"model\":\"" + model + "\", \"make\":\"" + make + "\"}");
                break;
            default:
                LOGGER.error("Invalid Request type received {}", reqType);
        }
        return reqBody;
    }

    /**
     * Remove STB from dawg house via GET request, passing device id as query param
     * @param deviceId
     *          STB device id
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean removeSTBFromDawgByQuery(String deviceId) throws DawgTestException {
        String url = RestURIConfig.REMOVE_STB_BY_QUERY_URI.buildURL(TestServerConfig.getHouse()) + deviceId;
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Remove the added test STBs from dawg house via DELETE request
     * @param  deviceId 
     *          STB device Id
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean removeStbFromDawg(String deviceId) throws DawgTestException {
        String url = RestURIConfig.ADD_REMOVE_STB_REST_URI.buildURL(TestServerConfig.getHouse()) + deviceId;
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.DELETE);

        // Sending DELETE request
        Response response = dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Get list of STB devices available in dawg house via GET request
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException
     */
    public boolean sendGetReqForSTBDeviceList() throws DawgTestException {
        String url = RestURIConfig.GET_STB_DEVICE_LIST_URI.buildURL(TestServerConfig.getHouse());
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Method to perform assignmodels in dawg house
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean assignModelDawg() throws DawgTestException {
        String url = RestURIConfig.ASSIGN_MODELS_URI.buildURL(TestServerConfig.getHouse());
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.GET);

        // Sending GET request
        Response response = dawgRestReqService.sendRequest();
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

    /**
     * Update an already existing STB parameters in dawg house via POST request
     * @param testStb
     *          MetaStb
     * @return boolean returns True if response is not null and have status code 200
     * @throws DawgTestException 
     */
    public boolean updateStbInDawg(MetaStb testStb) throws DawgTestException {
        String url = RestURIConfig.UPDATE_STB_DEVICE_URI.buildURL(TestServerConfig.getHouse()) + testStb.getId();
        DawgRestRequestService dawgRestReqService = new DawgRestRequestService(url, Method.POST);
        JsonCerealEngine cerealEngine = new JsonCerealEngine();

        // Sending POST request
        Response response;
        try {
            response = dawgRestReqService.setContentType(TestConstants.JSON_CONTENT_TYPE_HEADER_VALUE).setRequestBody(
                cerealEngine.writeToString(testStb.getData())).sendRequest();
        } catch (CerealException e) {
            throw new DawgTestException("Failed to convert STB data to string");
        }
        return (null != response) && (HttpStatus.SC_OK == response.getStatusCode());
    }

}
