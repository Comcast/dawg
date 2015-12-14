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

import java.io.IOException;
import java.util.Set;

import com.comcast.dawg.TestServers;
import com.comcast.dawg.selenium.Browser;
import com.comcast.dawg.selenium.BrowserServiceManager;
import com.comcast.video.dawg.common.DawgDevice;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.house.DawgHouseClient;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for the UI verification tests.
 *
 * @author  TATA
 */
public class UITestBase extends TestBase {

    /** The remote web driver. */
    protected static RemoteWebDriver driver;

    /**
     * Initializes the remote web driver.
     */
    @BeforeSuite
    public void startSelenium() {

        try {
            driver = BrowserServiceManager.getDriver(Browser.chrome);
        } catch (IOException e) {
            Assert.fail("Failed to set the driver.", e);
        }
    }

    /**
     * Stops the selenium services.
     */
    @AfterSuite
    public void stopSelenium() {
        BrowserServiceManager.shutdownAll();
    }

    @AfterMethod(description = "Method to dismiss any alert messages after test execution.")
    protected void dismissAlertMessage() {
        if (isAlertPresent()) {
            driver.switchTo().alert().dismiss();
        }
    }

    /**
     * Validates whether the alert message pops up.
     *
     * @return  true if alert is present, false otherwise.
     */
    public boolean isAlertPresent() {
        boolean isAlertPresent = false;

        try {
            driver.switchTo().alert();
            isAlertPresent = true;
        } catch (NoAlertPresentException Ex) {
            logger.info("No alert found.");
        }

        return isAlertPresent;
    }

    /**
     * Get all the STB meta data tags from backend.
     *
     * @param   stb  Meta stb.
     *
     * @return  all the tags to which stb is tagged.
     */
    public Set<String> getStbMetaDataTagsFromBackend(MetaStb stb) {
        MetaStb metaStb = (MetaStb) getStbMetaDataFromBackend(stb.getId());

        return metaStb.getTags();
    }

    /**
     * Gets the meta data for the given stb.
     *
     * @param   id  the unique id
     *
     * @return  the dawg device
     */
    public DawgDevice getStbMetaDataFromBackend(String id) {
        DawgHouseClient dawgHouseClient = new DawgHouseClient(TestServers.getHouse());
        return dawgHouseClient.getById(id);
    }
}
