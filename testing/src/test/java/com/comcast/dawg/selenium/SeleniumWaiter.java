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
package com.comcast.dawg.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for wait on loading of DOM element.
 */
public class SeleniumWaiter {

    /** Logger object for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumWaiter.class);

    /** One second represented in milli second. */
    private static final int ONE_SEC_IN_MILLI = 1000;

    /** Browser specific web driver object. * */
    private WebDriver driver;

    /**
     * Constructor to initialize web driver.
     *
     * @param  driver  Browser specific web driver object.
     */
    public SeleniumWaiter(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * It will wait till the presence of element on the DOM. If wait time go beyond to expected
     * timeout it will throw time out exception.
     *
     * @param   locatorName   element locator.
     * @param   timeoutInSec  timeout in seconds.
     *
     * @return  The functions' return value if the function returned something different from null
     *          or false before the timeout expired.
     */
    public WebElement waitForPresence(By locatorName, int timeoutInSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);

        return wait.until(ExpectedConditions.presenceOfElementLocated(locatorName));
    }

    /**
     * Wait until an element is no longer attached to the DOM.
     *
     * @param   element       The element to wait for.
     * @param   timeoutInSec  timeout in seconds.
     *
     * @return  false is the element is still attached to the DOM, true otherwise.
     */
    public boolean waitForStaleness(WebElement element, int timeoutInSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);

        return wait.until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * Wait for the presence of alert.
     *
     * @param  timeoutInSec  timeout in seconds.
     */
    public void waitForAlertPresence(int timeoutInSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Wait for definite time in seconds.
     *
     * @param  timeoutInSec  time out in second.
     */
    public static void waitTill(int timeoutInSec) {
        long startTime = 0;
        long timeLeft = timeoutInSec * ONE_SEC_IN_MILLI;

        while (timeLeft > 0) {
            startTime = System.currentTimeMillis();

            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted exception caught during sleep.", e);
            }

            timeLeft = timeLeft - (System.currentTimeMillis() - startTime);
        }
    }
}
