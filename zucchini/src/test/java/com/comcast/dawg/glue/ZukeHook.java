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


import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.utils.DawgCommonUIUtils;
import com.comcast.dawg.utils.DawgStbModelUIUtils;
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
     * @throws DawgTestException 
     */
    @Before(order = 1)
    public void addScenarioToTestContext(Scenario scenario) throws DawgTestException {
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_SCENARIO, scenario);
        LOGGER.info("Starting scenario {}", scenario.getName());
    }

    /**
     * This method adds test STB model before cucumber scenario and keeps in test context      
     * @throws DawgTestException 
     */
    @Before("@stbModel")
    public void createTestStbModel() throws DawgTestException {
        // Add test STB model 
        DawgStbModelUIUtils stbModelUtil = DawgStbModelUIUtils.getInstance();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TEST_STB_MODEL,
            stbModelUtil.createTestStbModelAndCache().getName());
    }

    /**
     * This method adds test tags before cucumber scenario and keeps 
     * the added tag with STB details into the test context.     
     * @throws DawgTestException 
     */

    @Before("@toggleall")
    public void addTestTags() throws DawgTestException {
        // Get Random STB count to add test STBs to Tag1
        int stbCountArrLen = TestConstants.STB_TEST_COUNT.length;
        Integer stbCount = TestConstants.STB_TEST_COUNT[new Random().nextInt(stbCountArrLen)];
        //Adding test Tag1 with STBs
        Map<String, String[]> testTagOneInfo = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(stbCount);
        if (!testTagOneInfo.isEmpty()) {
            Entry<String, String[]> tagOneEntry = testTagOneInfo.entrySet().iterator().next();
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG1, tagOneEntry.getKey());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG1_STBS, tagOneEntry.getValue());
        } else {
            LOGGER.error("Failed to add test tag one with STB count {}", stbCount);
        }

        // Get Random STB count to add test STBs to Tag2
        stbCount = TestConstants.STB_TEST_COUNT[new Random().nextInt(stbCountArrLen)];
        //Adding test Tag2 with STBs
        Map<String, String[]> testTagTwoInfo = DawgCommonUIUtils.getInstance().addTestTagForNSTBs(stbCount);
        if (!testTagTwoInfo.isEmpty()) {
            Entry<String, String[]> tagTwoEntry = testTagTwoInfo.entrySet().iterator().next();
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG2, tagTwoEntry.getKey());
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG2_STBS, tagTwoEntry.getValue());
        } else {
            LOGGER.error("Failed to add test tag two with STB count {}", stbCount);
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

    @After("@toggleall, @restest")
    public void clearAddedTags(Scenario s) throws DawgTestException {
        DawgCommonUIUtils.getInstance().removeAllTagsAddedInTest();
        DawgCommonUIUtils.getInstance().deleteAllStbs();
    }
    /**
     * This method clears all the added test STB models from dawg-house   
     * @throws DawgTestException 
     */
    @After("@stbModel")
    public void deleteTestSTBModels(Scenario s) throws DawgTestException {
        DawgStbModelUIUtils.getInstance().deleteAllTestSTBModels();
    }
}
