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

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.BrowserType;

import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.spells.saucelabs.SaucePhoenixDriver;
import com.comcast.zucchini.AbstractZucchiniTest;
import com.comcast.zucchini.TestContext;

/**
 * Parent test class for Dawg house portal automation
 * 
 * @author Priyanka
 */

public abstract class DawgTest extends AbstractZucchiniTest {

    /** Logger for the AbstractZucchiniTest class. */
    private static final Logger LOGGER = Logger.getLogger(DawgTest.class);

   

    /**
     * Creating driver and setting desired capabilities to test context
     */
    @Override
    public void setup(TestContext context) {
       
    }

    /**
     * Terminates the browser processes at the end of the test
     */
    @Override
    public void cleanup(TestContext context) {
        
    }

}
