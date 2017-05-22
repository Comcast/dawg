/* 
 * ===========================================================================
 * This file is the intellectual property of Comcast Corp.  It
 * may not be copied in whole or in part without the express written
 * permission of Comcast or its designees.
 * ===========================================================================
 * Copyright (c) 2016 Comcast Corp. All rights reserved.
 * ===========================================================================
 */

package com.comcast.dawg.glue;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.zucchini.TestContext;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

/**
 * Class having necessary steps to be executed before/after each scenario.
 *
 * @author  priyanka
 */
public class ZukeHook {

    // Logger instance
    private static final Logger LOGGER = LoggerFactory.getLogger(ZukeHook.class);

    /**
     * This method adds the cucumber scenario into the test context.  
     * 
     * @param  scenario
     */
    @Before(order = 1)
    public void addScenarioToTestContext(Scenario scenario) {
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_SCENARIO, scenario);
        LOGGER.info("Starting scenario {} {}", TestContext.getCurrent().name(), scenario.getName());
    }

    /**
     * Method executes before each scenario
     * 
     */
    @Before(order = 2)
    public void setupBefore() {

    }


}
