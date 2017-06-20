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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgRestRequestService;
import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.RestURIConfig;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.MetaStbBuilder;
import com.comcast.video.dawg.house.DawgHouseClient;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.internal.http.Method;
import com.jayway.restassured.response.Response;

/**
 * Utility class for handling STB operations in dawg index page
 *
 * @author  Pratheesh TK
 */
public class DawgCommonUIUtils {

    private DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServerConfig.getHouse());
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
     * Remove all the added test STB's.
     * @return true if all STBs removed, false otherwise
     * @throws DawgTestException 
     */
    public boolean deleteAllStbs() throws DawgTestException {
        StringBuilder stbIds = new StringBuilder();
        Map<String, MetaStb> testStbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        try {
            if (null != testStbs && !testStbs.isEmpty()) {
                for (Entry<String, MetaStb> entry : testStbs.entrySet()) {
                    String stbId = entry.getKey();
                    int statusCode = removeStbViaRestReq(stbId);
                    if (HttpStatus.SC_OK == statusCode) {
                        LOGGER.info("Succesfully deleted STB {} from dawg house", stbId);
                    } else {
                        stbIds = stbIds.append(stbId).append(",");
                    }
                }
                if (0 == stbIds.toString().trim().length()) {
                    return true;
                } else {
                    LOGGER.error("Failed to delete STBs {} from dawg house", stbIds);
                    return false;
                }
            }
        } finally {
            IOUtils.closeQuietly(dawgHouseClient);
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
     *
     * @return  test stb object.
     * @throws DawgTestException 
     */
    protected MetaStb addTestSTBToDawg() throws DawgTestException {
        MetaStb testStb = MetaStbBuilder.build().uid(TestConstants.STB_ID_PREF).model("TestModel").caps(
            new String[]{"TestCap" }).make("TestMake").power("TestPower").stb();
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
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_REMOVE_TAG).buildURL() + operation;
        String reqBody = new String("{\"id\": [" + stbList + "],\"tag\":[\"" + tagName + "\"]}");
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.POST);
        dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(reqBody);
        return DawgCommonRestUtils.getInstance().getRestResponse(dawgReqRunner);
    }

    /**
     * Create unique STBs for test purpose and return the array of test ids. 
     * @param   noOfStbRequired  Number of STBs required for testing.  
     * @return  Array of device id created.
     * @throws DawgTestException 
     */
    private String[] createRequestedTestStbs(int noOfStbRequired) throws DawgTestException {
        MetaStb stb = null;
        String[] testStbNames = new String[noOfStbRequired];
        for (int count = 0; count < noOfStbRequired; count++) {
            stb = addTestSTBToDawg();
            testStbNames[count] = stb.getId();
        }
        return testStbNames;
    }

    /**
     * Add the STB content and cache it for later deletion after completion of test execution.  
     * @param  stb  Meta stb object to be added.
     * @throws DawgTestException 
     */  
    protected void addSTBToDawg(MetaStb stb) throws DawgTestException {
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_REMOVE_STB).buildURL() + stb.getId();
        // To cache the test STBs for later deletion.
        List<String> testStbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STBS);
        if (null == testStbs) {
            testStbs = new ArrayList<String>();
        }
        String reqBody = new String("{\"macAddress\":\"" + stb.getMacAddress() + "\", \"name\":\"" + stb.getId() + "\"}");
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.PUT);     
        dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE).setRequestBody(reqBody);  
        Response response = DawgCommonRestUtils.getInstance().getRestResponse(dawgReqRunner);
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            testStbs.add(stb.getId());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STBS, testStbs);
        } else {
            LOGGER.error("Failed to add STB {} ", stb.getId());
        }
    }

    /**
     * Remove the added test STBs from dawg house  
     * @param  id stb Id
     * @throws DawgTestException 
     */
    private int removeStbViaRestReq(String id) throws DawgTestException {
        String url = RestURIConfig.getReqURI(DawgHouseConstants.ADD_OR_UPDATE_MODEL).buildURL() + id;
        DawgRestRequestService dawgReqRunner = new DawgRestRequestService(url, Method.DELETE);
        dawgReqRunner.setContentType(DawgHouseConstants.CONTENT_TYPE);        
        return DawgCommonRestUtils.getInstance().getRestResponse(dawgReqRunner).getStatusCode();

    }

    /**
     * For adding test tag with specified no.of STBs in tag cloud
     * @param int - STB count to add
     * @return Map returns test tag name and STB ids
     * @throws DawgTestException
     */
    public Map<String, String[]> addTestTagForNSTBs(int stbCount) throws DawgTestException {
        Map<String, String[]> tagStbMap = new HashMap<String, String[]>();
        // Create dynamic tag names
        String tagName = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
        String[] stbArray = createRequestedTestStbs(stbCount);
        Response response = addTagViaRestRequest(tagName, stbArray);
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            tagStbMap.put(tagName, stbArray);
            bufferTheTagsToBeCleared(tagName, stbArray);
        } else {
            LOGGER.error("Failed to add test tag {} with STBs {} to dawgHouse. Response received {}", tagName,
                stbArray.toString(), response.getStatusCode());
        }
        return tagStbMap;
    }
}