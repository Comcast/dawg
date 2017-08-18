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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.config.RestURIConfig;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgHouseRestHelper;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.video.stbio.meta.Program;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;

/**
 * Utility class for handling STB operations in dawg index page
 *
 * @author  Priyanka
 */
public class DawgCommonUIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgCommonUIUtils.class);
    private static DawgCommonUIUtils testBaseUtils = null;
    private static Object lock = new Object();

    /**
     * Creates single instance of UITestBaseUtils
     * 
     * @return UITestBaseUtils
     */
    public static DawgCommonUIUtils getInstance() {
        synchronized (lock) {
            if (null == testBaseUtils) {
                testBaseUtils = new DawgCommonUIUtils();
            }
        }
        return testBaseUtils;
    }

    /**
     * Remove all the added test entities (STB Device/STB Model) from Dawg via REST request.
     * @param contextKey
     *          Entity to be removed (STB Device/STB Model). The TestContext must hold ArrayList of STBs/STB Models
     * @return true if all entities removed, false otherwise
     * @throws DawgTestException 
     */

    @SuppressWarnings("unchecked")
    public boolean deleteTestSTBsOrModels(String contextKey) throws DawgTestException {
        StringBuilder entityIds = new StringBuilder();
        Object testEntities = TestContext.getCurrent().get(contextKey);
        if (null != testEntities) {
            if (!DawgHouseConstants.CONTEXT_TEST_STBS.equals(contextKey) && !DawgHouseConstants.CONTEXT_TEST_STB_MODELS.equals(contextKey)) {
                throw new DawgTestException("Context key is not handled");
            }
            if (!(testEntities instanceof ArrayList<?>)) {
                throw new DawgTestException("The value stored in TestContext is not an instance of ArrayList");
            }
            ArrayList<String> entityList = (ArrayList<String>) testEntities;

            for (String entityId : entityList) {
                boolean status = false;
                if (contextKey == DawgHouseConstants.CONTEXT_TEST_STBS) {
                    status = DawgHouseRestHelper.getInstance().removeStbFromDawg(entityId);
                } else if (contextKey == DawgHouseConstants.CONTEXT_TEST_STB_MODELS) {
                    status = DawgHouseRestHelper.getInstance().removeSTBModelFromDawg(entityId);
                }
                if (!status) {
                    entityIds = entityIds.append(entityId).append(",");
                }
            }
            if (0 == entityIds.toString().trim().length()) {
                entityList.clear();
                TestContext.getCurrent().set(contextKey, entityList);
                //Clear the test STBs/STB models created stored in text context.
                clearTextContextValues(DawgHouseConstants.STBS);
                clearTextContextValues(DawgHouseConstants.STB_MODELS);
                return true;
            } else {
                LOGGER.error("Failed to delete " + contextKey + " {} from dawg house", entityIds);
                return false;
            }

        } else {
            LOGGER.info("Failed to find any test STBS or Models to delete using REST request");
        }
        return false;
    }


    /**
     * Clean up all the list of tags added.
     * @return true if all the added test tags removed successfully, false otherwise
     * @throws DawgTestException 
     */
    public boolean removeAllTagsAddedInTest() throws DawgTestException {
        Response response = null;
        StringBuilder tags = new StringBuilder();
        Map<String, String[]> tagsToBeCleared = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAGS);
        if (null != tagsToBeCleared && !tagsToBeCleared.isEmpty()) {
            for (Entry<String, String[]> tagEntry : tagsToBeCleared.entrySet()) {
                response = removeTagViaRestReq(tagEntry.getKey(), tagEntry.getValue());
                if (HttpStatus.SC_OK == response.getStatusCode()) {
                    LOGGER.info("Succesfully Removed the tag {} from dawg house", tagEntry.getKey());
                } else {
                    tags.append(tagEntry.getKey()).append(",");
                }
            }
            if (0 == tags.toString().trim().length()) {
                tagsToBeCleared.clear();
                // If test tags deleted from dawg house, then clear all the test tag values stored in text context.
                this.clearTextContextValues(DawgHouseConstants.TAGS);
                return true;
            } else {
                LOGGER.error("Failed to remove added tags {} from dawg house", tags);
                return false;
            }
        } else {
            LOGGER.error("No tag elements found to perform remove operation", tagsToBeCleared);
        }
        return false;
    }

    /**
     * Buffer the added tags in-order to clear it at the end of test completion.
     * @param  tagName  Tag Name to be added.
     * @param  stbIds   List of stb ids.
     */
    public void bufferTheTagsToBeCleared(String tagName, String... stbIds) {
        // Since multi tag addition is supported.                
        Map<String, String[]> tagsToBeCleared = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAGS);
        if (null == tagsToBeCleared) {
            tagsToBeCleared = new HashMap<String, String[]>();
        }
        tagsToBeCleared.put(tagName, stbIds);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_TAGS, tagsToBeCleared);
    }

    /**
     * Provides a test STB object.
     * @param Create test STBs based on the specified test preference id
     * @return  test stb object.
     * @throws DawgTestException 
     */
    public MetaStb addTestSTBToDawg(String testPreferenceId) throws DawgTestException {
        MetaStb testStb = null;
        MetaStbBuilder testStbBuider = MetaStbBuilder.build().uid(testPreferenceId).model(TestConstants.TEST_MODEL).caps(
            new String[]{TestConstants.TEST_CAPABILITY }).make(TestConstants.TEST_MAKE).power(TestConstants.TEST_POWER);
        if (TestConstants.STB_ID_ADVACED_FILTER.equals(testPreferenceId)) {
            testStb = testStbBuider.rackName("TestRack").controllerIpAddress("TestIpAddress").hardwareRevision(
                "TestHardwareRevision").slotName("TestSlotName").irBlasterType("TestIrBlasterType").stb();
        } else if (TestConstants.STB_ID_PREF.equals(testPreferenceId) || TestConstants.STB_ID_BULK_TAG.equals(testPreferenceId)) {
            testStb = testStbBuider.stb();
        } else if (TestConstants.STB_ID_EDIT_DEVICE.equals(testPreferenceId)) {
            testStb = testStbBuider.family("TestFamily").rackName("TestRack").controllerIpAddress("TestIpAddress").hardwareRevision(
                "TestHardwareRevision").remote("TestRemoteType").tags("TestTag").slotName("TestSlotName").irBlasterType(
                "TestIrBlasterType").stb();
            testStb.setProgram(Program.valueOf("TestProgram"));
        } else {
            throw new DawgTestException("Invalid test preference Id" + testPreferenceId);
        }

        addSTBToDawg(testStb);
        return testStb;
    }

    /**
     * Removes the tag associated with the box via REST request service.
     *
     * @param   tagName  Tag name to be removed.
     * @param   stbIds   List of stb IDs tag together.  
     * @return  Response of tag removal. 
     * @throws DawgTestException    
     */
    public Response removeTagViaRestReq(String tagName, String... stbIds) throws DawgTestException {
        return taggingOperation(TestConstants.REMOVE_OPERATION_TAG_REST, tagName, stbIds);
    }

    /**
     * Add the tag associated with the box using rest request.
     *
     * @param   tagName  Tag name to be removed.
     * @param   stbIds   List of stb IDs tag together. 
     * @return  Response of tag removal.   
     * @throws DawgTestException    
     */
    public Response addTagViaRestRequest(String tagName, String... stbIds) throws DawgTestException {
        return taggingOperation(TestConstants.ADD_OPERATION_TAG_REST, tagName, stbIds);
    }

    /**
     * Does the tagging operation specified by sending a REST request.
     *
     * @param   operation  To specify add or remove opration to be done.
     * @param   tagName    Tag name to be removed.
     * @param   stbIds     List of stb IDs tag together.   
     * @return  Response of tag removal.      
     * @throws DawgTestException 
     */
    private Response taggingOperation(String operation, String tagName, String... stbIds) throws DawgTestException {
        if (null == stbIds) {
            throw new IllegalArgumentException("No STB ids passed for tagging. Tag creation is not allowed with out any STB id passed.");
        }
        StringBuilder stbList = new StringBuilder();
        for (String stbId : stbIds) {
            if (stbList.length() > 0) {
                stbList.append(',');
            }
            stbList.append('"').append(stbId).append('"');
        }
        LOGGER.info(String.format("About to %s tag (%s) with stb list [%s].", operation, tagName, stbList));
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_REMOVE_TAG).buildURL(TestServerConfig.getHouse()) + operation;
        String reqBody = new String("{\"id\": [" + stbList + "],\"tag\":[\"" + tagName + "\"]}");
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.POST);
        return dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(reqBody).sendRequest();
    }

    /**
     * Create unique STBs for test purpose and return the array of test ids. 
     * @param  noOfStbRequired  Number of STBs required for testing. 
     * @param  testPref create test stbs based on the test preference specified 
     * @return  Array of device id created.
     * @throws DawgTestException 
     */
    private String[] createRequestedTestStbs(int noOfStbRequired, String testPref) throws DawgTestException {
        MetaStb stb = null;
        String[] testStbNames = new String[noOfStbRequired];
        for (int count = 0; count < noOfStbRequired; count++) {
            stb = addTestSTBToDawg(testPref);
            testStbNames[count] = stb.getId();
        }
        return testStbNames;
    }

    /**
     * Add the STB content and cache it for later deletion after completion of test execution.  
     * @param  stb  Meta stb object to be added.
     * @throws DawgTestException 
     */
    private void addSTBToDawg(MetaStb stb) throws DawgTestException {
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_REMOVE_STB).buildURL(TestServerConfig.getHouse()) + stb.getId();
        JsonCerealEngine cerealEngine = new JsonCerealEngine();
        // To cache the test STBs for later deletion.
        List<String> testStbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        if (null == testStbs) {
            testStbs = new ArrayList<String>();
        }

        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.PUT);
        Response response = null;
        try {
            response = dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(
                cerealEngine.writeToString(stb.getData())).sendRequest();
        } catch (CerealException e) {
            throw new DawgTestException("Failed to convert STB data to string");
        }
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            testStbs.add(stb.getId());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STBS, testStbs);
        } else {
            LOGGER.error("Failed to add STB {} ", stb.getId());
        }
    }

    /**
     * For adding test tag with specified no.of STBs in tag cloud
     * @param int - STB count to add
     * @param stbArray
     * @return Map returns test tag name and STB ids
     * @throws DawgTestException
     */
    public Map<String, List<String>> addTestTagForNSTBs(int stbCount, String... stbArray) throws DawgTestException {
        Map<String, List<String>> tagStbMap = new HashMap<String, List<String>>();
        // Create dynamic tag names
        String tagName = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
        if (null == stbArray || 0 == stbArray.length) {
            stbArray = createRequestedTestStbs(stbCount, TestConstants.STB_ID_PREF);
        }
        Response response = addTagViaRestRequest(tagName, stbArray);
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            tagStbMap.put(tagName, Arrays.asList(stbArray));
            bufferTheTagsToBeCleared(tagName, stbArray);
        } else {
            LOGGER.error("Failed to add test tag {} with STBs {} to dawgHouse. Response received {}", tagName,
                stbArray.toString(), response.getStatusCode());
        }
        return tagStbMap;
    }

    /**
     * Clean up all the created test tags/STBs, stored in text context   
     * @param type - tags or stbs 
     * @throws DawgTestException 
     */
    private void clearTextContextValues(String type) throws DawgTestException {
        String[] valuesStoredInContext = null;
        if (DawgHouseConstants.TAGS.equals(type)) {
            valuesStoredInContext = DawgHouseConstants.TEST_TAGS_STORED_IN_CONTEXT;
        } else if (DawgHouseConstants.STBS.equals(type)) {
            valuesStoredInContext = DawgHouseConstants.TEST_STBS_STORED_IN_CONTEXT;
        } else if (DawgHouseConstants.STB_MODELS.equals(type)) {
            valuesStoredInContext = DawgHouseConstants.TEST_STB_MODELS_STORED_IN_CONTEXT;
        } else {
            throw new DawgTestException("Invalid type" + type);
        }
        //Clear all the test tags/STBs/STB models stored in corresponding context key
        for (String contextKey : valuesStoredInContext) {
            TestContext.getCurrent().set(contextKey, null);
        }
    }

    /**
     * Get all the STB meta data tags from backend.    
     * @param stbId
     * @return  all the tags to which stb is tagged.
     */
    public List<String> getStbMetaDataTags(String stbId) {
        @SuppressWarnings("resource")
        DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServerConfig.getHouse());
        dawgHouseClient.login(TestServerConfig.getUsername(), TestServerConfig.getPassword());
        MetaStb metaStb = dawgHouseClient.getById(stbId);
        return new ArrayList<String>(metaStb.getTags());
    }
}
