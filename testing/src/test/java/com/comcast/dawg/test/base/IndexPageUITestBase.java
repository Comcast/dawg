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
package com.comcast.dawg.test.base;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;

import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.house.pages.IndexPage;

import com.comcast.video.dawg.common.MetaStb;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.seleniumhq.jetty7.http.HttpStatus;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

import static com.comcast.dawg.constants.TestConstants.USER;

/**
 * Base test class for index page verification tests.
 *
 * @author  Pratheesh TK
 */
public class IndexPageUITestBase extends UITestBase {

    /** Buffers the tag to be cleared. * */
    private Map<String, String[]> tagsToBeCleared = new HashMap<String, String[]>();

    /**
     * After class to clean up all the list of tags added.
     */
    @AfterClass
    protected void clearAllTagsAddedInTest() {
        HttpResponse response = null;

        for (Entry<String, String[]> tagEntry : tagsToBeCleared.entrySet()) {

            try {
                response = removeTag(tagEntry.getKey(), tagEntry.getValue());

                if (getStatus(response) != HttpStatus.OK_200) {
                    logger.error("Could not remove the tag : " + tagEntry.getKey() + ". Response received is : " +
                            getStatus(response));
                }
            } catch (IOException e) {
                logger.error("Failed to remove tag : " + tagEntry.getKey());
            }
        }
    }

    /**
     * Buffer the added tags in-order to clear it at the end of test completion.
     *
     * @param  tagName  Tag Name to be added.
     * @param  stbIds   List of stb ids.
     */
    protected void bufferTheTagsToBeCleared(String tagName, String... stbIds) {

        // Since multi tag addition is supported.
        for (String tag : tagName.split(TestConstants.TAG_NAME_SPLIT_REGEX)) {

            if (tagsToBeCleared.containsKey(tag)) {
                String[] stbIdsAlreadyExist = tagsToBeCleared.get(tagName);
                stbIds = (String[]) ArrayUtils.addAll(stbIds, stbIdsAlreadyExist);
            }

            tagsToBeCleared.put(tag, stbIds);
        }
    }

    /**
     * Provides a test STB object.
     *
     * @return  test stb object.
     */
    protected MetaStb createTestStb() {
        MetaStb testStb =
            MetaStbBuilder.build().uid(TestConstants.STB_ID_PREF).model("TestModel").caps(new String[] { "TestCap" }).make(
                    "TestMake").power("TestPower").stb();
        addStb(testStb);

        return testStb;
    }

    /**
     * Load index page and create a tag with the device ID passed. The class will buffer the tag
     * name in-order to remove it at the time of clean up.
     *
     * @param  indexPage  Index page object.
     * @param  tagName    Tag Name to be added.
     * @param  stbIds     List of stb ids.
     */
    protected void loadIndexPageAndAddTag(IndexPage indexPage, String tagName, String... stbIds) {
        indexPage.loadIndexPageAndAddTag(tagName, stbIds);
        bufferTheTagsToBeCleared(tagName, stbIds);
    }

    /**
     * Create a tag with the device ID passed in already loaded Index page. The class will buffer
     * the tag name in-order to remove it at the time of clean up.
     *
     * @param  indexPage  Index page object.
     * @param  tagName    Tag Name to be added.
     * @param  stbIds     List of stb ids.
     */
    protected void addTagFromIndexPage(IndexPage indexPage, String tagName, String... stbIds) {
        indexPage.addTag(tagName, stbIds);
        bufferTheTagsToBeCleared(tagName, stbIds);
    }

    /**
     * Removes the tag associated with the box.
     *
     * @param   tagName  Tag name to be removed.
     * @param   stbIds   List of stb IDs tag together.
     *
     * @return  Response of tag removal.
     *
     * @throws  IOException  If any connection failures.
     */
    public HttpResponse removeTag(String tagName, String... stbIds) throws IOException {
        return taggingOperation(TestConstants.REMOVE_OPERATION_TAG_REST, tagName, stbIds);
    }

    /**
     * Add the tag associated with the box using rest request.
     *
     * @param   tagName  Tag name to be removed.
     * @param   stbIds   List of stb IDs tag together.
     *
     * @return  Response of tag removal.
     *
     * @throws  IOException  If any connection failures.
     */
    public HttpResponse addTagUsingRestRequest(String tagName, String... stbIds) throws IOException {
        return taggingOperation(TestConstants.ADD_OPERATION_TAG_REST, tagName, stbIds);
    }

    /**
     * Does the tagging operation specified by sending a REST request.
     *
     * @param   operation  To specify add or remove opration to be done.
     * @param   tagName    Tag name to be removed.
     * @param   stbIds     List of stb IDs tag together.
     *
     * @return  Response of tag removal.
     *
     * @throws  IOException  If any connection failures.
     */
    private HttpResponse taggingOperation(String operation, String tagName, String... stbIds) throws IOException {

        if (null == stbIds) {
            throw new IllegalArgumentException(
                    "No STB ids passed for tagging. Tag creation is not allowed with out any STB id passed.");
        }

        StringBuffer stbList = new StringBuffer();

        for (String stbId : stbIds) {

            if (stbList.length() > 0) {
                stbList.append(',');
            }

            stbList.append('"').append(stbId).append('"');
        }

        logger.info(String.format("About to %s tag (%s) with stb list [%s].", operation, tagName, stbList));

        HttpEntity entity = new StringEntity("{\"id\": [" + stbList + "],\"tag\":[\"" + tagName + "\"]}");
        HttpPost method = new HttpPost(TestConstants.TAG_UPDATE_REST_URI + operation);
        method.addHeader("Content-Type", "application/json");
        method.setEntity(entity);

        HttpClient httpClient = new DefaultHttpClient();

        return httpClient.execute(method);
    }

    /**
     * Provide unique dynamic tag names.
     *
     * @return  tag name.
     */
    protected static String createDynamicTestTagNames() {
        return MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
    }

    /**
     * Validate the bulk tag tag count. If the expected count does not match with the tag count
     * returned the test is failed.
     *
     * @param  indexPage      Index page object.
     * @param  tagName        Name tag for which the count to be validated.
     * @param  expectedCount  expected tag count.
     */
    protected void validateTagCloudTagCount(IndexPage indexPage, String tagName, int expectedCount) {

        // Validate the tag count.
        int tagCount = indexPage.getTagCloudTagCount(tagName);
        Assert.assertEquals(tagCount, expectedCount,
                String.format("Expected tag count was %s but actual returned is : %s.", expectedCount,
                    tagCount));
    }

    /**
     * Loads the index page and return the object.
     *
     * @return  the index page after loading the page.
     */
    protected IndexPage getIndexPageLoaded() {
        IndexPage indexPage = new IndexPage(driver, USER);
        indexPage.load();

        return indexPage;
    }

    /**
     * Create unique STBs for test purpose and return the array of test ids.
     *
     * @param   noOfStbRequired  Number of STBs required for testing.
     *
     * @return  Array of device id created.
     */
    protected String[] createRequestedTestStbs(int noOfStbRequired) {
        MetaStb stb = null;
        String[] testStbNames = new String[noOfStbRequired];

        for (int count = 0; count < noOfStbRequired; count++) {
            stb = createTestStb();
            testStbNames[count] = stb.getId();
        }

        return testStbNames;
    }
}
