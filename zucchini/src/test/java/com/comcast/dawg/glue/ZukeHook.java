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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.utils.SeleniumImgGrabber;
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
     * Method executes before each scenario
     * 
     */
    @Before(order = 2)
    public void setupBefore() {

    }

}
