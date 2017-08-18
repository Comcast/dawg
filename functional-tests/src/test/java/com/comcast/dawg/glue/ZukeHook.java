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

package com.comcast.dawg.glue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.utils.DawgCommonUIUtils;
import com.comcast.dawg.utils.DawgStbModelUIUtils;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.zucchini.TestContext;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * Class having necessary steps to be executed before/after each scenario.
 * @author  priyanka
 */
public class ZukeHook {

    // Logger instance
    private static final Logger LOGGER = LoggerFactory.getLogger(ZukeHook.class);

    /**
     * This method adds the cucumber scenario into the test context. 
     * @param  scenario    
     */
    @Before(order = 1)
    public void addScenarioToTestContext(Scenario scenario) {
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_SCENARIO, scenario);
        LOGGER.info("Starting scenario {}", scenario.getName());
    }

    /**
     * This method adds test STB model before cucumber scenario and keeps in test context      
     * @throws DawgTestException 
     */
    @Before("@stbModel")
    public void createTestStbModel() throws DawgTestException {
        String testSTBModel = getValue(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
        if (null == testSTBModel) {
            // Add test STB model 
            DawgStbModelUIUtils stbModelUtil = DawgStbModelUIUtils.getInstance();
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_MODEL,
                stbModelUtil.createTestStbModelAndCache().getName());
        }
    }

    /**
     * This method adds test tags before cucumber scenario and keeps 
     * the added tag with STB details into the test context.     
     * @throws DawgTestException 
     */

    @Before("@toggleall, @tagcloud")
    public void addTestTags(Scenario scenario) throws DawgTestException {
        Integer stbCount = 2;
        Map<String, List<String>> testTagOneInfo = new HashMap<String, List<String>>();
        //Check whether test tag and STBs exists in context.        
        if (null == getValue(DawgHouseConstants.CONTEXT_TEST_TAG1) || null == TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1)) {
            // Get Random STB count to add test STBs to Tag1            
            //Adding test Tag1 with STBs
            testTagOneInfo = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(stbCount, null);
            if (!testTagOneInfo.isEmpty()) {
                Entry<String, List<String>> tagOneEntry = testTagOneInfo.entrySet().iterator().next();
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_TAG1, tagOneEntry.getKey());
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1, tagOneEntry.getValue());
            } else {
                LOGGER.error("Failed to add test tag one with STB count {}", stbCount);
            }
        }
        if (null == getValue(DawgHouseConstants.CONTEXT_TEST_TAG2) || null == TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2)) {
            //Adding test Tag2 with STBs
            Map<String, List<String>> testTagTwoInfo = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(stbCount,
                null);
            if (!testTagTwoInfo.isEmpty()) {
                Entry<String, List<String>> tagTwoEntry = testTagTwoInfo.entrySet().iterator().next();
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_TAG2, tagTwoEntry.getKey());
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2, tagTwoEntry.getValue());
            } else {
                LOGGER.error("Failed to add test tag two with STB count {}", stbCount);
            }
        }
        // Test Tag3 creation is required only for tag cloud UI behavior validations
        if (scenario.getSourceTagNames().contains("@commonStbs")) {
            //Create test tag3 which hold the STBs of first added test tag1
            if (null == getValue(DawgHouseConstants.CONTEXT_TEST_TAG3) || null == TestContext.getCurrent().get(
                DawgHouseConstants.CONTEXT_TAG3_WITH_STB_SET1)) {
                // Get the added test STBs from the tag1 
                List<String> stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
                Map<String, List<String>> testTagThreeWithTagOneStbs = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(
                    0, stbs.toArray(new String[stbs.size()]));
                if (!testTagThreeWithTagOneStbs.isEmpty()) {
                    Entry<String, List<String>> tagThreeEntry = testTagThreeWithTagOneStbs.entrySet().iterator().next();
                    TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_TAG3, tagThreeEntry.getKey());
                    TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG3_WITH_STB_SET1, tagThreeEntry.getValue());
                } else {
                    LOGGER.error("Failed to add test tag three with STBs");
                }
            }
        }

    }

    /**
     * This method adds test tags to perform tag deletion test before cucumber scenario and 
     * added tag with STB details into the test context.     
     * @throws DawgTestException 
     */

    @Before("@deleteTag")
    public void addTestTagsToDoDeletion(Scenario scenario) throws DawgTestException {

        Entry<String, List<String>> tagOneEntry = null;
        // Create delete test tag to perform delete tag operation
        if (null == getValue(DawgHouseConstants.CONTEXT_DELETE_TAG1) && null == TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_DELETE_TAG1_STBS)) {
            //  Create test tag for delete which hold the STBs of tag1.                     
            Map<String, List<String>> testTagOneForDeletion = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(2,
                null);
            if (!testTagOneForDeletion.isEmpty()) {
                tagOneEntry = testTagOneForDeletion.entrySet().iterator().next();
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DELETE_TAG1, tagOneEntry.getKey());
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DELETE_TAG1_STBS, tagOneEntry.getValue());
            } else {
                LOGGER.error("Failed to add test tag three with STBs {}", 2);
            }
        }
        if (null == getValue(DawgHouseConstants.CONTEXT_DELETE_TAG2) && null == TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_DELETE_TAG2_STBS)) {
            //  Create test tag2(to perform tag deletion ui checking) which hold any one of STBs of tag1.  
            Map<String, List<String>> testTagTwoForDeletion = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(0,
                tagOneEntry.getValue().get(0));
            if (!testTagTwoForDeletion.isEmpty()) {
                Entry<String, List<String>> tagTwoEntry = testTagTwoForDeletion.entrySet().iterator().next();
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DELETE_TAG2, tagTwoEntry.getKey());
                TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DELETE_TAG2_STBS, tagTwoEntry.getValue());
            } else {
                LOGGER.error("Failed to add test tag three with STBs {}", 2);
            }
        }
    }

    /**
     * This method add test STBs for performing advanced filter search      
     * @throws DawgTestException 
     */
    @Before("@advanceSearch")
    public void createStbForAdvanceSearch() throws DawgTestException {
        String testSTB = getValue(DawgHouseConstants.CONTEXT_TEST_STB_ADVACE_FILTER);
        if (null == testSTB) {
            MetaStb stb = DawgCommonUIUtils.getInstance().addTestSTBToDawg(TestConstants.STB_ID_ADVACED_FILTER);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_ADVACE_FILTER, stb.getId());
        }
    }


    /**
     * This method add test STBs to edit device details in edit device overlay      
     * @throws DawgTestException 
     */
    @Before("@editDevice")
    public void createStbModelForEditDevice() throws DawgTestException {
        String testSTBId = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE);
        if (null == testSTBId) {
            MetaStb stb = DawgCommonUIUtils.getInstance().addTestSTBToDawg(TestConstants.STB_ID_EDIT_DEVICE);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE, stb.getId());

        }
    }


    /**
     * This method add test STB for performing bulk tag ui testing   
     * @throws DawgTestException 
     */
    @Before("@bulkTag")
    public void createStbForBulkTag() throws DawgTestException {
        //Create a test STB
        String testSTB = getValue(DawgHouseConstants.CONTEXT_TEST_STB_WIHTOUT_TAG);
        if (null == testSTB) {
            MetaStb stb = DawgCommonUIUtils.getInstance().addTestSTBToDawg(TestConstants.STB_ID_BULK_TAG);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_WIHTOUT_TAG, stb.getId());
        }
    }

    /**
     * This method add test Tag for performing bulk tag ui testing   
     * @throws DawgTestException 
     */
    @Before("@bulkTagwithSTB")
    public void createBulkTag(Scenario s) throws DawgTestException {
        //Adding test Tag2 with only 1 STB
        Map<String, List<String>> existingtag1 = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(1, null);
        if (!existingtag1.isEmpty()) {
            Entry<String, List<String>> tagEntry = existingtag1.entrySet().iterator().next();
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG1, tagEntry.getKey());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_WITH_EXISTING_TAG1,
                tagEntry.getValue().get(0));
        } else {
            LOGGER.error("Failed to add test tag with STB for bulk tag UI checking");
        }
        //Adding test Tag2 with 2 STBs
        Map<String, List<String>> existingtag2 = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(2, null);
        if (!existingtag2.isEmpty()) {
            Entry<String, List<String>> tagEntry = existingtag2.entrySet().iterator().next();
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG2, tagEntry.getKey());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_WITH_EXISTING_TAG2, tagEntry.getValue());
        } else {
            LOGGER.error("Failed to add test tag with STB for bulk tag UI checking");
        }
    }

    /**
     * This method embeds screenshots to cucumber reports when ever a step fails.
     * @param  scenario
     */
    @After("@uitest")
    public void embedScreenshotOnFail(Scenario s) {
        if (s.isFailed()) {
            SeleniumImgGrabber.addImage();
        }
    }

    /**
     * This method clears all the added test tags and deletes the added test STBs from dawg-house   
     * @throws DawgTestException 
     */
    @After("@rest_stb_add, @dawg_pound, @bulkTagwithSTB, @bulkTag, @editDevice, @deleteTag")
    public void clearAddedTagsAndStbs() throws DawgTestException {
        DawgCommonUIUtils.getInstance().removeAllTagsAddedInTest();
        DawgCommonUIUtils.getInstance().deleteTestSTBsOrModels(DawgHouseConstants.CONTEXT_TEST_STBS);
    }

    /**
     * This method clears all the added test STB models from dawg-house   
     * @throws DawgTestException 
     */
    @After("@rest_stb_model_add")
    public void deleteTestSTBModels() throws DawgTestException {
        DawgCommonUIUtils.getInstance().deleteTestSTBsOrModels(DawgHouseConstants.CONTEXT_TEST_STB_MODELS);
    }

    /**
     * Get the text context value
     * @param key - context key   
     */
    private String getValue(String key) {
        return TestContext.getCurrent().get(key);
    }
}
