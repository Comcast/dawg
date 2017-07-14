/*
 * ===========================================================================
 * This file is the intellectual property of Comcast Corp.  It
 * may not be copied in whole or in part without the express written
 * permission of Comcast or its designees.
 * ===========================================================================
 * Copyright (c) 2015 Comcast Corp. All rights reserved.
 * ===========================================================================
 */
package com.comcast.dawg;

import java.io.IOException;
import java.util.List;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.glue.ZukeHook;
import com.comcast.dawg.utils.DawgCommonUIUtils;
import com.comcast.dawg.utils.DawgDriverController;
import com.comcast.zucchini.AbstractZucchiniTest;
import com.comcast.zucchini.TestContext;

import org.apache.log4j.Logger;

import cucumber.api.CucumberOptions;

/**
 * Runner class for Dawg House UI automation
 * 
 * @author Priyanka
 */
@CucumberOptions(glue = {"com.comcast.dawg.glue" }, features = {"src/test/resources/features/" }, tags = {"@uitest" })
public class DawgHouseUITest extends AbstractZucchiniTest {

    /** Logger for the DawgHouseTest class. */
    private static final Logger LOGGER = Logger.getLogger(DawgHouseUITest.class);

    @Override
    public List<TestContext> getTestContexts() {

        try {
            return DawgDriverController.getDriverTestContexts();
        } catch (DawgTestException | IOException e) {
            LOGGER.error("Failed to create TestContexts for Web driver. Reason: " + e.getMessage());
        }
        return null;
    }

    /**
     * Terminates the Web driver processes at the end of the test
     */
    @Override
    public void cleanup(TestContext context) {
        DawgDriverController.shutdown();
        try {
            new ZukeHook().clearAddedTagsandSTBs();
            DawgCommonUIUtils.getInstance().deleteSTBsOrModelsFromList(DawgHouseConstants.CONTEXT_TEST_STB_MODELS);
        } catch (DawgTestException e) {
            LOGGER.error("Failed to delete test STBs or STB models or Tags using rest request" + e.getMessage());
        }
    }
}
