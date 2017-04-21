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

import java.util.List;

import com.comcast.zucchini.TestContext;

import org.apache.log4j.Logger;

import cucumber.api.CucumberOptions;

/**
 * Class for testing of Dawg house automation
 * 
 * @author Priyanka
 */
@CucumberOptions(glue = {"com.comcast.dawg.glue" }, features = {"src/test/resources/features/" }, tags = {"@uitest" })
public class DawgHouseTest extends DawgTest {

    /** Logger for the DawgHouseTest class. */
    private static final Logger LOGGER = Logger.getLogger(DawgHouseTest.class);

    @Override
    public List<TestContext> getTestContexts() {
      
          //  return DevPortalTestController.getBrowserTestContexts();
        
        return null;
    }

}
