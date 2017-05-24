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

import org.apache.log4j.Logger;

import com.comcast.dawg.utils.DawgDriverController;
import com.comcast.zucchini.AbstractZucchiniTest;
import com.comcast.zucchini.TestContext;

import cucumber.api.CucumberOptions;

/**
 * Runner class for Dawg House automation
 * 
 * @author Priyanka
 */
@CucumberOptions(glue = {"com.comcast.dawg.glue" }, features = {"src/test/resources/features/" }, tags = {"@uitest" })
public class DawgHouseTest extends AbstractZucchiniTest {

    /** Logger for the DawgHouseTest class. */
    private static final Logger LOGGER = Logger.getLogger(DawgHouseTest.class);
   
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

    }

}
