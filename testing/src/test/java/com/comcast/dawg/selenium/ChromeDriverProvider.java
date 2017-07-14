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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Provides an executable file for chromedriver
 * @author Val Apgar
 *
 */
public class ChromeDriverProvider implements BrowserDriverProvider {

    public static final String WINDOWS = "win32/selenium-drivers-win32.exe";
    public static final String LINUX_32 = "linux32/selenium-drivers-linux32.bin";
    public static final String LINUX_64 = "linux64/selenium-drivers-linux64.bin";
    public static final String MAC_32 = "mac32/selenium-drivers-mac32.bin";
    public static final String LINUX_FILENAME = "chromedriver.bin";
    public static final String WINDOWS_FILENAME = "chromedriver.exe";
    public static final String MAC_FILENAME = LINUX_FILENAME;
    public static final String ENV_KEY = "CHROME_DRIVER";
    public static final String SYSTEM_PROP_KEY = "chrome.driver";

    public static final String DRIVER_DIR = "chromedriver/";

    private static final String USER_HOME = SystemUtils.USER_HOME;
    private static final String TEMP_DIR =
        File.separator + ".selenium" + File.separator + "chromedriver" + File.separator;

    protected boolean isWindows = SystemUtils.IS_OS_WINDOWS;
    protected boolean isLinux = SystemUtils.IS_OS_LINUX;
    protected boolean isMac = SystemUtils.IS_OS_MAC_OSX;

    protected String bitSize = System.getProperty("sun.arch.data.model");

    /**
     * Provides the appropriate chromedriver for the currently running os.
     *
     * @throws  IOException  if we're unable to determine the os
     */
    public File getDriverFile() throws IOException {
        String driver;
        String filename;
        String chromeDriverPath = System.getProperty(SYSTEM_PROP_KEY);
        chromeDriverPath = chromeDriverPath == null ? System.getenv(ENV_KEY) : chromeDriverPath;
        if (chromeDriverPath == null) {
            if (isWindows) {
                driver = WINDOWS;
                filename = WINDOWS_FILENAME;
            } else if (isLinux) {
                filename = LINUX_FILENAME;

                if (is32bit()) {
                    driver = LINUX_32;
                } else {
                    driver = LINUX_64;
                }
            } else if (isMac) {
                filename = MAC_FILENAME;
                driver = MAC_32;
            } else {
                System.out.println(this.toString());

                // We're on an unknown OS and the driver won't run.
                throw new IOException("A compatible executable could not be determined for this operating system");
            }

            System.out.println("Getting resource for: " + driver + " having filename: " + filename);
            System.out.println(ChromeDriverProvider.class.getResource(DRIVER_DIR + driver));

            InputStream srcFile = ChromeDriverProvider.class.getResourceAsStream(DRIVER_DIR + driver);
            File destFile = new File(USER_HOME + TEMP_DIR + filename);

            copyFile(srcFile, destFile);
            destFile.setExecutable(true);

            return destFile;
        } else {
            return new File(chromeDriverPath);
        }
    }

    protected boolean is32bit() {
        return bitSize.equals("32");
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("System profile: Windows = ");
        builder.append(isWindows);
        builder.append(", Linux = ");
        builder.append(isLinux);
        builder.append(", Mac = ");
        builder.append(isMac);
        builder.append(", BitSize =  ");
        builder.append(bitSize);

        return builder.toString();
    }

    // wrapped for easier testing.
    protected void copyFile(InputStream srcFile, File destFile) throws IOException {

        if (destFile.isDirectory()) {
            throw new IOException("Cannot copy to destination directory");
        } else {
            FileUtils.deleteQuietly(destFile);
            FileUtils.copyInputStreamToFile(srcFile, destFile);
        }
    }
}
