/*
 * ===========================================================================
 * This file is the intellectual property of Comcast Corp.  It
 * may not be copied in whole or in part without the express written
 * permission of Comcast or its designees.
 * ===========================================================================
 * Copyright (c) 2017 Comcast Corp. All rights reserved.
 * ===========================================================================
 */
package com.comcast.dawg;

import java.util.ArrayList;
import java.util.List;

import com.comcast.zucchini.AbstractZucchiniTest;
import com.comcast.zucchini.TestContext;

import cucumber.api.CucumberOptions;

/**
 * Runner class for Dawg House REST automation
 * 
 * @author Priyanka
 */
@CucumberOptions(glue = {"com.comcast.dawg.glue" }, features = {"src/test/resources/features/" }, tags = {"@dawg_rest" })
public class DawgHouseRestTest extends AbstractZucchiniTest {

    /**
     * @inheritDoc
     */
    @Override
    public List<TestContext> getTestContexts() {
        List<TestContext> tc = new ArrayList<TestContext>();
        TestContext testContext = new TestContext("DawgHouseRestTest");
        tc.add(testContext);
        return tc;
    }

}
