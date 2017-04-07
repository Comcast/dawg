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
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import com.comcast.dawg.saucelab.SauceConnector;
import com.comcast.dawg.saucelab.SauceConstants;
import com.comcast.dawg.saucelab.SauceProvider;
import com.saucelabs.ci.sauceconnect.SauceTunnelManager;

/**
 * Singleton that starts up browsers used to test on
 * @author Kevin Pearson
 *
 */
public class BrowserServiceManager {
    private static final Logger LOGGER = Logger.getLogger(BrowserServiceManager.class);
    private static Map<RemoteWebDriver, DriverService> drivers = new HashMap<RemoteWebDriver, DriverService>();
    private static RemoteWebDriver global = null;


    /** Chrome browser setting arguments. */
    private static final List<String> CHROME_OPTION_ARGUMENTS = Collections.unmodifiableList(Arrays.asList(
        "--start-maximized", "allow-running-insecure-content", "ignore-certificate-errors"));

    private static String testMode;
    private static SauceTunnelManager tunnelManager = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                BrowserServiceManager.shutdownAll();
            }
        });
        testMode = SauceProvider.getTestMode();
    }

    /**
     * Gets a web driver for a given browser
     * @param browser The browser to get a driver for
     * @return
     * @throws IOException
     */
    public static synchronized RemoteWebDriver getDriver(Browser browser) throws IOException {

        if (global == null) {
            if (null != testMode && SauceConstants.SAUCE.equals(testMode)) {
                DesiredCapabilities sauceCapabilities = null;
                String osVersion = null;
                if (null == tunnelManager) {
                    tunnelManager = SauceConnector.startSauceConnect();
                }
                String platformtype = SauceProvider.getSaucePlatform();
                if (SauceConstants.WINDOWS.equals(platformtype)) {
                    osVersion = SauceProvider.getWinOsVersion();
                } else if (SauceConstants.MAC.equals(platformtype)) {
                    osVersion = SauceProvider.getMacOsVersion();
                } else if (SauceConstants.LINUX.equals(platformtype)) {
                    osVersion = SauceProvider.getLinuxOsVersion();
                } else {
                    LOGGER.error("Invalid Platform type" + platformtype);
                }
                sauceCapabilities = setSauceCapabilities(osVersion, browser);
                global = new RemoteWebDriver(new URL("http://" + SauceProvider.getSauceUserName() + ":" + SauceProvider.getSauceKey() + "@" + SauceConstants.SAUCE_URL), sauceCapabilities);
                drivers.put(global, null);
            } else {
                DriverService service = start(browser);
                global = new RemoteWebDriver(service.getUrl(), getDesiredBrowserCapabilities(browser));
                drivers.put(global, service);
            }
        }
        return global;
    }

    /**
     * Set the desired capabilities for running tests in sauce labs.   
     * @param osVersion 
     * @param browser
     *        Browser in which test to be run        
     * @return Capabilities corresponding to the browser and platform passed.
     */
    private static DesiredCapabilities setSauceCapabilities(String osVersion, Browser browser) {
        DesiredCapabilities sauceCapabilities = new DesiredCapabilities();
        sauceCapabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        sauceCapabilities.setCapability(CapabilityType.VERSION, SauceProvider.getChromeVersion());
        sauceCapabilities.setCapability(CapabilityType.PLATFORM, osVersion);
        sauceCapabilities.setCapability(SauceConstants.NAME, SauceConstants.DAWG_TEST_INFO);
        sauceCapabilities.setCapability(SauceConstants.TUNNEL_IDENTIFIER, SauceConstants.DAWG_TEST);
        return sauceCapabilities;
    }

    /**
     * Provides the browser desired capabilities.
     *
     * @param browser
     *        Browser for which the desired capabilities to be returned.
     * @return Capabilities corresponding to the browser passed.
     */
    private static Capabilities getDesiredBrowserCapabilities(Browser browser) {
        DesiredCapabilities capabilities = null;
        switch (browser) {
            case chrome:
                ChromeOptions options = new ChromeOptions();
                options.addArguments(CHROME_OPTION_ARGUMENTS);
                capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                break;
            default:
                capabilities = new DesiredCapabilities();
                break;
        }
        return capabilities;
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
     * Starts a selenium service for a given browser
     * @param browser The browser to start the service for
     * @return
     * @throws IOException
     */
    public static DriverService start(Browser browser) throws IOException {
        BrowserDriverProvider provider = getProvider(browser);
        DriverService service = new ChromeDriverService.Builder().usingDriverExecutable(provider.getDriverFile()).usingAnyFreePort().build();
        service.start();
        return service;
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
            if (null != testMode && SauceConstants.SAUCE.equals(testMode)) {
                driver.quit();
                SauceConnector.stopSauceConnect();
            } else {
                try {
                    drivers.get(driver).stop();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void shutdownAndRemove(RemoteWebDriver driver) {
        shutdown(driver);
        drivers.remove(driver);
    }
}
