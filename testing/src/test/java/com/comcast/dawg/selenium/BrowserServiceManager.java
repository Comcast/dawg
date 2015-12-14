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

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

/**
 * Singleton that starts up browsers used to test on
 * @author Kevin Pearson
 *
 */
public class BrowserServiceManager {
    private static Map<RemoteWebDriver, DriverService> drivers = new HashMap<RemoteWebDriver, DriverService>();
    private static RemoteWebDriver global = null;

    /** Chrome browser setting arguments. */
    private static final List<String> CHROME_OPTION_ARGUMENTS = Collections.unmodifiableList(Arrays.asList(
                "--start-maximized", "allow-running-insecure-content", "ignore-certificate-errors"));


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
     * @return
     * @throws IOException
     */
    public static synchronized RemoteWebDriver getDriver(Browser browser) throws IOException {
        if (global == null) {
            DriverService service = start(browser);
            global = new RemoteWebDriver(service.getUrl(), getDesiredBrowserCapabilities(browser));
            drivers.put(global, service);
        }
        return global;
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
        if (driver != null) {
            try {
                drivers.get(driver).stop();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void shutdownAndRemove(RemoteWebDriver driver) {
        shutdown(driver);
        drivers.remove(driver);
    }
}
