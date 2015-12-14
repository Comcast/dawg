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
package com.comcast.video.dawg.common;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  The TestNG test class for DawgConfiguration
 * @author TATA
 *
 */
public class DawgConfigurationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgConfigurationTest.class);

    @Test
    public void testGetDawgShowUrl() {
        DawgConfiguration config = new DawgConfiguration();
        config.put("dawg-show-url", "url");
        String expected = "url/";
        Assert.assertEquals(config.getDawgShowUrl(), expected);
    }

    @Test
    public void testGetDawgHouseUrl() {
        DawgConfiguration config = new DawgConfiguration();
        config.put("dawg-house-url", "url");
        String expected = "url/";
        Assert.assertEquals(config.getDawgHouseUrl(), expected);
    }

    @Test
    public void testGetDawgPoundUrl() {
        DawgConfiguration config = new DawgConfiguration();
        config.put("dawg-pound-url", "url");
        String expected = "url/";
        Assert.assertEquals(config.getDawgPoundUrl(), expected);
    }

    @Test(expectedExceptions=RuntimeException.class)
    public void testGetDawgShowUrlWithNullUrl() {
        DawgConfiguration config = new DawgConfiguration();
        String expected = "url/";
        Assert.assertEquals(config.getDawgShowUrl(), expected);
    }

    @Test
    public void testSlashWithNullUrl() {
        DawgConfiguration config = new DawgConfiguration();
        config.put("dawg-pound-url", null);
        Assert.assertNull(config.getDawgPoundUrl());
    }

    @Test
    public void testLoadConfigFile() {
        DawgConfiguration config = new DawgConfiguration();
        System.setProperty(DawgConfiguration.DAWG_PATH_PARAM, "src/test/resources");

        String configFileName = "dawg.ini";
        try {
            config.load(configFileName) ;
        } catch (IOException e) {
            Assert.fail("load config file threw " +  e);
        }
    }

    @Test
    public void testLoadConfigFileWithWrongPath() {
        DawgConfiguration config = new DawgConfiguration();
        System.setProperty(DawgConfiguration.DAWG_PATH_PARAM, "");

        //this forces a file to be read from the designated path to use an input stream.
        //If the string is just passed, then it is possible for this to pass based on
        //the environment variable settings.
        File f = new File("", "dawg.ini");

        try {
            config.load(new FileInputStream(f)) ;
        } catch (IOException e) {
            LOGGER.debug("Configuration loading failed upon not finding the specified file. This is the expected result.");
        }
    }
}
