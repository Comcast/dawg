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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.saucelab.SauceConstants;
import com.comcast.dawg.saucelab.SauceLabConfig;
import com.comcast.dawg.saucelab.SauceTestException;
import com.comcast.magicwand.builders.PhoenixDriverBuilder;
import com.comcast.magicwand.builders.PhoenixDriverIngredients;
import com.comcast.magicwand.drivers.PhoenixDriver;
import com.comcast.magicwand.enums.DesktopOS;
import com.comcast.magicwand.enums.OSType;
import com.comcast.magicwand.spells.saucelabs.SaucePhoenixDriver;
import com.comcast.magicwand.spells.saucelabs.SauceProvider;
import com.comcast.magicwand.spells.web.chrome.ChromeWizardFactory;

/**
 * Singleton that starts up browsers used to test on
 * @author Kevin Pearson
 *
 */
public class BrowserServiceManager {
    private static Map<RemoteWebDriver, PhoenixDriver> drivers = new HashMap<RemoteWebDriver, PhoenixDriver>();
    private static RemoteWebDriver global = null;

    /** Chrome browser setting arguments. */
    private static final List<String> CHROME_OPTION_ARGUMENTS = Collections.unmodifiableList(Arrays.asList(
        "--start-maximized", "allow-running-insecure-content", "ignore-certificate-errors"));

    private static final String testMode = SauceLabConfig.getTestMode();
    private static final String sauceKey = SauceLabConfig.getSauceKey();
    private static final String sauceUsername = SauceLabConfig.getSauceUserName();
    private static SaucePhoenixDriver saucedriver = null;
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                BrowserServiceManager.shutdownAll();
            }
        });
    } 

    /**
     * Gets a web driver for a given browser
     * @param browser The browser to get a driver for
     * @return remote web driver
     * @throws IOException
     * @throws SauceTestException 
     */
    public static synchronized RemoteWebDriver getDriver(Browser browser) throws IOException, SauceTestException {
        if (global == null) {
            if (null != testMode && SauceConstants.SAUCE.equals(testMode)) {
                global = createSauceRemoteDriver(browser.toString());
            } else {
                //If not sauce run locally
                PhoenixDriver pDriver = null;
                if ("chrome".equals(browser.toString())) {
                    PhoenixDriverIngredients ingredients = new PhoenixDriverIngredients();
                    ingredients.addCustomDriverConfiguration(TestConstants.CHROME_DRIVER_VERSION, TestConstants.VERSION);
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments(CHROME_OPTION_ARGUMENTS);
                    ingredients.addDriverCapability(ChromeOptions.CAPABILITY, options);
                    pDriver = new PhoenixDriverBuilder().forCustom(new ChromeWizardFactory()).withIngredients(
                        ingredients).build();
                }
                global = (RemoteWebDriver) pDriver.getDriver();
                drivers.put(global, pDriver);
            }
        }
        return global;
    }

    /**
     * Create remote driver for running tets in sauce labs.   
     * @param browser
     *        Browser in which test to be run
     * @throws SauceTestException 
     */
    private static RemoteWebDriver createSauceRemoteDriver(String browser) throws SauceTestException {
        String osVersion = null;
        String platformtype = SauceLabConfig.getSaucePlatform();
        if (SauceConstants.WINDOWS.equals(platformtype)) {
            osVersion = SauceLabConfig.getWinOsVersion();
        } else if (SauceConstants.MAC.equals(platformtype)) {
            osVersion = SauceLabConfig.getMacOsVersion();
        } else if (SauceConstants.LINUX.equals(platformtype)) {
            //There is no specific linux os version in saucelabs
            osVersion = "";
        } else {
            throw new SauceTestException("Invalid Platform type" + platformtype);
        }
        PhoenixDriverIngredients pdi = buildSauceDriverIngredients(browser, SauceLabConfig.getChromeVersion(),
            OSType.valueOf(platformtype.toUpperCase()), osVersion);

        saucedriver = (SaucePhoenixDriver) new PhoenixDriverBuilder().withIngredients(pdi).build();
        global = (RemoteWebDriver) saucedriver.getDriver();
        drivers.put(global, saucedriver);
        return global;
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

        String vpnOptions = " -i " + SauceConstants.DAWG_TEST;
        // @formatter:off
        PhoenixDriverIngredients pid = new PhoenixDriverIngredients()
         .addCustomDriverConfiguration(SauceProvider.USERNAME, sauceUsername)
         .addCustomDriverConfiguration(SauceProvider.API_KEY, sauceKey)
         .addCustomDriverConfiguration(SauceProvider.URL, SauceConstants.SAUCE_URL)
         .addBrowser(browserType)
         .addDesktopOS(new DesktopOS(platform, osVersion))
         .addDriverCapability(CapabilityType.VERSION, browserVersion)
         .addDriverCapability(SauceConstants.NAME, SauceConstants.DAWG_TEST_INFO)
         .addDriverCapability(SauceConstants.TUNNEL_IDENTIFIER,SauceConstants.DAWG_TEST)
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
     * Gets a driver provider for the given browser. This provider will give the executable for the selenium
     * driver
     * @param browser The browser to get a provider for.
     * @return
     */
    public static BrowserDriverProvider getProvider(Browser browser) {
        switch (browser) {
            case chrome:
                return new ChromeDriverProvider();
        }
        throw new RuntimeException("Could not find a driver provider for " + browser);
    }

    /**
     * Shuts down all the selenium services that were started with this manager
     */
    public static void shutdownAll() {
        for (RemoteWebDriver driver : drivers.keySet()) {
            shutdown(driver);
        }
        drivers.clear();
    }

    /**
     * Shuts down the selenium service for the given browser
     * @param browser
     */
    public static void shutdown(RemoteWebDriver driver) {
        if (null != driver) {
            try {
                drivers.get(driver).quit();
                if (null != testMode && SauceConstants.SAUCE.equals(testMode)) {
                    saucedriver.closeVPNConnection();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shuts down and remove the driver
     * @param RemoteWebDriver
     */
    public static void shutdownAndRemove(RemoteWebDriver driver) {
        shutdown(driver);
        drivers.remove(driver);
    }
}
