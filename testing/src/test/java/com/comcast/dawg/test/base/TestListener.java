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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Overriding the testng listener class to log the test execution details.
 *
 * @author  Pratheesh TK
 */
public class TestListener implements ITestListener {

    /** Listener class logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    /**
     * Called when a test class finishes all of its tests.
     *
     * @param  context  the test context
     */
    @Override
    public final void onFinish(ITestContext context) {
        LOGGER.info("***** Finished testing class: " + context.getClass().getName() + " *****");
    }

    /**
     * Called when a test class starts (before any tests run).
     *
     * @param  context  the test context
     */
    @Override
    public final void onStart(ITestContext context) {
        LOGGER.info("***** Starting testing on class: " + context.getClass().getName() + " *****");
    }

    /**
     * Called when a test fails.
     *
     * @param  testResult  the test result object
     */
    @Override
    public final void onTestFailure(ITestResult testResult) {
        LOGGER.info("***** FAILED - " + getTestName(testResult) + " *****");
    }

    /**
     * Called when a test is skipped.
     *
     * @param  testResult  the test result object
     */
    @Override
    public final void onTestSkipped(ITestResult testResult) {
        LOGGER.info("***** SKIPPED - " + getTestName(testResult) + " ****");
    }

    /**
     * Called when a test starts.
     *
     * @param  testResult  the test result object
     */
    @Override
    public final void onTestStart(ITestResult testResult) {
        LOGGER.info("***** STARTED - " + getTestName(testResult) + " *****");
    }

    /**
     * Called when a test passes.
     *
     * @param  testResult  the test result object
     */
    @Override
    public final void onTestSuccess(ITestResult testResult) {
        LOGGER.info("***** PASSED - " + getTestName(testResult) + " *****");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult testResult) {
        LOGGER.info("***** FAILED - " + getTestName(testResult) + " (but was within success percentage). *****");

    }

    /**
     * Provide the test name with parameter list by parsing the test result object.
     *
     * @param   testResult  describes the result of a test.
     *
     * @return  The test name detail.
     */
    private String getTestName(ITestResult testResult) {
        StringBuilder testName = new StringBuilder();
        String testResultName = testResult.getName();

        if (null != testResultName) {
            testName.append(testResultName);
        } else {
            testName.append("**Unknown test**");
        }

        for (Object param : testResult.getParameters()) {

            if (null != param) {
                testName.append(" - ").append(param.toString());
            } else {
                testName.append(" - null");
            }
        }

        return testName.toString();
    }
}
