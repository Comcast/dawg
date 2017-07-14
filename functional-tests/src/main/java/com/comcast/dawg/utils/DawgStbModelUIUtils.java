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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.RestURIConfig;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.constants.TestConstants.Capability;
import com.comcast.dawg.constants.TestConstants.Family;
import com.comcast.video.dawg.common.DawgModel;
import com.comcast.dawg.MetaStbBuilder;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for handling STB operations in dawg index page
 *
 * @author  Pratheesh TK
 */
public class DawgStbModelUIUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(DawgStbModelUIUtils.class);
    private static DawgStbModelUIUtils stbModelUtils = null;
    private static Object lock = new Object();

    /**
     * Creates single instance of DawgStbModelUIUtils
     * 
     * @return DawgStbModelUIUtils
     */
    public static DawgStbModelUIUtils getInstance() {
        synchronized (lock) {
            if (null == stbModelUtils) {
                stbModelUtils = new DawgStbModelUIUtils();
            }
        }
        return stbModelUtils;
    }

    /**
     * Creates a test STB model with properties(family and capabilities) and cache the model name for later deletion. 
     * @return  Successfully created STB model.
     * @throws DawgTestException    
     */
    public DawgModel createTestStbModelAndCache() throws DawgTestException {
        /** Cached model name for final deletion. */
        List<String> cachedDawgModelNames = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODELS);
        if (null == cachedDawgModelNames) {
            cachedDawgModelNames = new ArrayList<String>();
        }
        DawgModel dawgModel = new DawgModel();
        dawgModel.setName(MetaStbBuilder.getUID(TestConstants.MODEL_NAME_PREF));
        dawgModel.setFamily(Family.AVAILABLE_TEST_FAMILY.name());
        dawgModel.setCapabilities(new ArrayList<String>(Arrays.asList(Capability.AVAILABLE_TEST_CAP.name())));
        int response = addStbModelViaRest(dawgModel);
        LOGGER.info("Response of adding STB model : " + response);
        if (HttpStatus.SC_OK != response) {
            throw new DawgTestException("Failed to add the model, the response returned is : " + response);
        }
        cachedDawgModelNames.add(dawgModel.getName());
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_MODELS, cachedDawgModelNames);
        return dawgModel;
    }

    /**
     * To add STB model using rest call.   
     * @param   model  Dawg model object which need to be added.   
     * @return  http response status code of addition of model. 
     * @throws DawgTestException
     */
    private int addStbModelViaRest(DawgModel model) throws DawgTestException {
        StringBuilder capabilities = this.getCapabilities(model);
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_UPDATE_MODEL).buildURL(TestServerConfig.getHouse()) + model.getName();
        String reqBody = "{\"name\": \"" + model.getName() + "\",\"capabilities\":[" + capabilities + "],\"family\": \"" + model.getFamily() + "\"}";
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.POST);
        Response response = dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(
            reqBody).sendRequest();
        return response.getStatusCode();
    }

    /**
     * Get capabilities for the given STB model   
     * @param   model  Dawg model   
     * @return  Capabilities of given STB model.
     */
    private StringBuilder getCapabilities(DawgModel model) {
        StringBuilder capabilities = new StringBuilder();
        if (null != model.getCapabilities()) {
            for (String capability : model.getCapabilities()) {
                if (0 < capabilities.length()) {
                    capabilities.append(',');
                }
                capabilities.append('"').append(capability).append('"');            }
        }
        return capabilities;
    }

    /**
     * Create a Test STB model       
     * @return  String stb model 
     * @throws DawgTestException     
     */
    public String createNewTestModel() {
        List<String> cachedDawgModelNames = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODELS);
        if (null == cachedDawgModelNames) {
            cachedDawgModelNames = new ArrayList<String>();
        }
        // Get a unique model name for addition.
        String newTestModel = MetaStbBuilder.getUID(TestConstants.MODEL_NAME_PREF);
        // Caching the model name for later deletion.
        cachedDawgModelNames.add(newTestModel);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_MODELS, cachedDawgModelNames);
        return newTestModel;
    }
}
