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
import java.io.InputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.ini4j.Ini;

/**
 * Loads in the global configuration for Dawg from an ini format.
 *
 * @author Andrew Benton
 */
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    static Ini ini = null;

    private static final String DAWG_PATH_VAR = "DAWG_CONF_PATH";
    private static final String DAWG_PROP_KEY = "dawg.path";
    private static final String DAWG_DEFAULT_PATH = "/etc/dawg";
    public  static final String DAWG_CONF = "dawg.ini";

    private static File tryGetFile(Path path) {
        File file = path.toFile();

        if(file.exists())
            return file;
        else
            return null;
    }

    private static void loadGlobalConf() {
        if(Config.ini != null)
            return;

        try {
            Config.ini = Config.loadIni();

            if(!Config.ini.containsKey("dawg")) {
                LOGGER.error("Loaded ini file does not contain a \"dawg\" section.");
                Config.ini.add("dawg");
            }

            if(!Config.ini.containsKey("cats")) {
                LOGGER.error("Loaded ini file does not contain a \"cats\" section.");
                Config.ini.add("cats");
            }

            if(!Config.ini.containsKey("chimps")) {
                LOGGER.error("Loaded ini file does not contain a \"chimps\" section.");
                Config.ini.add("chimps");
            }
        }
        catch(IOException ioe) {
            LOGGER.error("Failed to load config from local path, system path, or environment path.  Providing blank config.");
            Config.ini = new Ini();
            Config.ini.add("dawg");
            Config.ini.add("cats");
            Config.ini.add("chimps");
        }
    }

    public static Ini loadIni() throws IOException {
        return Config.loadIni(DAWG_CONF);
    }

    public static Ini loadIni(String fileName) throws IOException {
            File f = null;

            if(f == null) f = tryGetFile(Paths.get(System.getenv(DAWG_PATH_VAR), fileName)); //path  dawg.ini
            if(f == null) f = tryGetFile(Paths.get(System.getProperty(DAWG_PROP_KEY, DAWG_DEFAULT_PATH), fileName));
            if(f == null) f = tryGetFile(Paths.get("/", "etc", "dawg", fileName));           //etc's dawg.ini
            if(f == null) f = tryGetFile(Paths.get(fileName));                               //local dawg.ini
            if(f == null) f = tryGetFile(Paths.get(System.getProperty("user.home"), fileName)); //${HOME} dawg.ini

            return new Ini(f); //if f is null, this will except
    }

    public static Ini loadIni(InputStream is) throws IOException {
        return new Ini(is);
    }

    /**
     * Check whether the key in the given section exists.
     *
     * @param section The section to search in
     * @param key The key to search for
     * @return Value indicating if the key is found.
     */
    public static boolean containsKey(String section, String key) {
        Config.loadGlobalConf();

        Ini.Section sec = Config.ini.get(section);

        return sec.containsKey(key);
    }

    /**
     * Get the `key` from the `section` and return it as a string.
     *
     * @param section The section to search in
     * @param key The key to search for int he given section
     * @return The retrieved string
     */
    public static String get(String section, String key) {
            Config.loadGlobalConf();

            Ini.Section sec = Config.ini.get(section);

            return sec.get(key);
    }

    /**
     * Get the `key` from the `section` and return it as a string.
     *
     * @param section The section to search in
     * @param key The key to search for int he given section
     * @return The retrieved string
     */
    public static String get(String section, String key, String defaultString) {
        Config.loadGlobalConf();

        Ini.Section sec = Config.ini.get(section);

        if(sec == null) {
            LOGGER.error("ERROR: Unable to get section: \"{}\"", section);
        }

        if(sec.containsKey(key))
            return sec.get(key);
        else
            return defaultString;
    }

    /**
     * Write the current config that's in the ini in memory back to disk.
     */
    public static void store() throws IOException {
        Config.loadGlobalConf();
        Config.ini.store();
    }

    public static void store(File destination) throws IOException {
        Config.loadGlobalConf();
        Config.ini.store(destination);
    }

    public static void put(String section, String key, String value) {
        Config.loadGlobalConf();

        Ini.Section sec = Config.ini.get(section);

        sec.put(key, value);
    }
}
