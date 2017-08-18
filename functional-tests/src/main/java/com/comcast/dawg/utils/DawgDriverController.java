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
package com.comcast.dawg.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.SauceLabConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.SauceLabConstants;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.selenium.Browser;
import com.comcast.magicwand.builders.PhoenixDriverBuilder;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.OSType;
import com.comcast.magicwand.spells.saucelabs.SaucePhoenixDriver;
import com.comcast.magicwand.spells.saucelabs.SauceProvider;
import com.comcast.magicwand.spells.web.chrome.ChromeWizardFactory;
import com.comcast.zucchini.TestContext;

/**
 * Controller class for Creating Web driver instances 
 * @author Priyanka 
 */
public class DawgDriverController {
    
    /** Chrome browser setting arguments. */
    private static final List<String> CHROME_OPTION_ARGUMENTS = Collections.unmodifiableList(Arrays.asList(
        "--start-maximized", "allow-running-insecure-content", "ignore-certificate-errors"));

    private static final String testMode = SauceLabConfig.getTestMode();
    private static final String sauceKey = SauceLabConfig.getSauceKey();
    private static final String sauceUsername = SauceLabConfig.getSauceUserName();


    /**
     * Method returns the list of testContexts based on the test mode selected(Sauce Labs or Local Vms)    
     * @return List of TestContext
     * @throws IOException
     * @throws DawgTestException 
     */
    public static List<TestContext> getDriverTestContexts() throws IOException, DawgTestException {
        List<TestContext> contexts = new ArrayList<TestContext>();
        String browser = Browser.CHROME.name().toLowerCase();
        TestContext testContext = new TestContext(browser);
        RemoteWebDriver webDriver = null;
        if (SauceLabConstants.SAUCE.equals(testMode)) {
            SaucePhoenixDriver sauceDriver = createSauceRemoteDriver(browser);
            webDriver = (RemoteWebDriver) sauceDriver.getDriver();
            testContext.set(DawgHouseConstants.CONTEXT_SAUCE_DRIVER, sauceDriver);

        } else {
            //To run tests in Local Machine
            PhoenixDriver pDriver = null;
            if ("chrome".equals(browser)) {
                PhoenixDriverIngredients ingredients = new PhoenixDriverIngredients();
                ingredients.addCustomDriverConfiguration(TestConstants.CHROME_DRIVER_VERSION, TestConstants.VERSION);
                ChromeOptions options = new ChromeOptions();
                options.addArguments(CHROME_OPTION_ARGUMENTS);
                ingredients.addDriverCapability(ChromeOptions.CAPABILITY, options);
                pDriver = new PhoenixDriverBuilder().forCustom(new ChromeWizardFactory()).withIngredients(ingredients).build();
            } else {
                throw new DawgTestException("Invalid browser type" + browser);
            }
            webDriver = (RemoteWebDriver) pDriver.getDriver();
        }
        if (null == webDriver) {
            throw new DawgTestException("Failed to Start Web Driver");
        }
        testContext.set(DawgHouseConstants.CONTEXT_WEB_DRIVER, webDriver);
        contexts.add(testContext);
        return contexts;
    }

    /**
     * Create remote driver for running tets in sauce labs.   
     * @param browser
     *        Browser in which test to be run
     * @throws DawgTestException 
     */
    private static SaucePhoenixDriver createSauceRemoteDriver(String browser) throws DawgTestException {
        String osVersion = null;
        String platformtype = SauceLabConfig.getSaucePlatform();
        if (SauceLabConstants.WINDOWS.equals(platformtype)) {
            osVersion = SauceLabConfig.getWinOsVersion();
        } else if (SauceLabConstants.MAC.equals(platformtype)) {
            osVersion = SauceLabConfig.getMacOsVersion();
        } else if (SauceLabConstants.LINUX.equals(platformtype)) {
            //There is no specific linux os version in saucelabs
            osVersion = "";
        } else {
            throw new DawgTestException("Invalid Platform type" + platformtype);
        }
        PhoenixDriverIngredients pdi = buildSauceDriverIngredients(browser, SauceLabConfig.getChromeVersion(),
            OSType.valueOf(platformtype.toUpperCase()), osVersion);
        SaucePhoenixDriver saucedriver = (SaucePhoenixDriver) new PhoenixDriverBuilder().withIngredients(pdi).build();
        //  return (RemoteWebDriver) saucedriver.getDriver();
        return saucedriver;
    }

    /**
     * Setting driver ingredients to establish sauce VPN connection and setting sauce labs credentials
     * @param browserType
     * @param browser version
     * @param OSType
     * @param OsVersion
     * @return PhoenixDriverIngredients
     */
    private static PhoenixDriverIngredients buildSauceDriverIngredients(String browserType, String browserVersion, OSType platform, String osVersion) {

        String vpnOptions = " -i " + SauceLabConstants.DAWG_TEST;
        // @formatter:off
        PhoenixDriverIngredients pid = new PhoenixDriverIngredients()
         .addCustomDriverConfiguration(SauceProvider.USERNAME, sauceUsername)
         .addCustomDriverConfiguration(SauceProvider.API_KEY, sauceKey)
         .addCustomDriverConfiguration(SauceProvider.URL, SauceLabConstants.SAUCE_URL)
         .addBrowser(browserType)
         .addDesktopOS(new DesktopOS(platform, osVersion))
         .addDriverCapability(CapabilityType.VERSION, browserVersion)
         .addDriverCapability(SauceLabConstants.NAME, SauceLabConstants.DAWG_TEST_INFO)
         .addDriverCapability("maxDuration",3600)
          .addDriverCapability(SauceLabConstants.TUNNEL_IDENTIFIER,SauceLabConstants.DAWG_TEST)
        //Enable Sauce Connect
        .addCustomDriverConfiguration(SauceProvider.VPN, Boolean.valueOf(true))
        // disable verbosity
        .addCustomDriverConfiguration(SauceProvider.VPN_QUIET_MODE, Boolean.valueOf(false))
        //Port used for SauceConnect
        .addCustomDriverConfiguration(SauceProvider.VPN_PORT, Integer.valueOf(SauceLabConfig.getSaucePort()))
        // Options used for Sauce Connect
        .addCustomDriverConfiguration(SauceProvider.VPN_OPTIONS, vpnOptions)
        // Version of SauceConnect to use. default is 3
        .addCustomDriverConfiguration(SauceProvider.VPN_VERSION, Integer.valueOf(4));
      // @formatter:on
        return pid;
    }

    /**
     * Shuts down the driver processes   
     */
    public static void shutdown() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        SaucePhoenixDriver sauceDriver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_SAUCE_DRIVER);
        if (null != driver) {
            driver.quit();
            if (SauceLabConstants.SAUCE.equals(testMode)) {
                sauceDriver.closeVPNConnection();
            }
        }
    }

}
