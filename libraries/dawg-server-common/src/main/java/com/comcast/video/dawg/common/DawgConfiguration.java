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

import java.io.IOException;
import java.io.InputStream;

import org.ini4j.Ini;

import com.comcast.video.dawg.common.security.AuthConfigFactory;
import com.comcast.video.dawg.common.security.AuthServerConfig;
import com.comcast.video.dawg.common.security.LdapAuthServerConfig;

/**
 * The key value pairs of the dawg configuration.
 * @author Kevin Pearson
 *
 */
public class DawgConfiguration {

    public static final String INI_SECTION = "dawg";
    public static final String INI_AUTH_SECTION = "auth";

    public static final String DAWG_PATH_PARAM = "dawg.path";
    public static final String DEFAULT_DAWG_PATH = "/etc/dawg/";

    public static final String DAWG_SHOW_URL = "dawg-show-url";
    public static final String DAWG_HOUSE_URL = "dawg-house-url";
    public static final String DAWG_POUND_URL = "dawg-pound-url";
    public static final String DAWG_SHOW_HEALTH_URL = "dawg-show-health-url";
    public static final String DAWG_HOUSE_HEALTH_URL = "dawg-house-health-url";
    public static final String DAWG_POUND_HEALTH_URL = "dawg-pound-health-url";
    public static final String DEFAULT_DAWG_SHOW_HEALTH_URL = "http://localhost:8080/dawg-show/health";
    public static final String DEFAULT_DAWG_HOUSE_HEALTH_URL = "http://localhost:8080/dawg-house/health";
    public static final String DEFAULT_DAWG_POUND_HEALTH_URL = "http://localhost:8080/dawg-pound/health";
    
    public static final String DAWG_VERSION = "version";

    private Ini ini;
    private AuthServerConfig auth = null;
    private AuthConfigFactory authConfigFactory = new AuthConfigFactory();

    public DawgConfiguration() {
        this.ini = new Ini();
        if(!this.ini.containsKey(INI_SECTION))
            this.ini.add(INI_SECTION);
    }

    /**
     * Gets the url to the dawg-show server that this configuration points to
     * @return
     */
    public String getDawgShowUrl() {
        return DawgConfiguration.slash(this.get(DAWG_SHOW_URL));
    }

    /**
     * Gets the url to the dawg-house server that this configuration points to
     * @return
     */
    public String getDawgHouseUrl() {
        return DawgConfiguration.slash(this.get(DAWG_HOUSE_URL));
    }

    /**
     * Gets the version of dawg that is deployed
     * @return
     */
    public String getDawgVersion() {
        return this.get(DAWG_VERSION, "latest");
    }

    public String getDawgShowHealthUrl() {
        return this.get(DAWG_SHOW_HEALTH_URL, DEFAULT_DAWG_SHOW_HEALTH_URL);
    }

    public String getDawgHouseHealthUrl() {
        return this.get(DAWG_HOUSE_HEALTH_URL, DEFAULT_DAWG_HOUSE_HEALTH_URL);
    }

    public String getDawgPoundHealthUrl() {
        return this.get(DAWG_POUND_HEALTH_URL, DEFAULT_DAWG_POUND_HEALTH_URL);
    }

    /**
     * Gets the url to the dawg-pound server that this configuration points to
     * @return
     */
    public String getDawgPoundUrl() {
        return DawgConfiguration.slash(this.get(DAWG_POUND_URL));
    }
    
    private void lazyAuthCfg() {
        if (this.auth == null) {
            if (this.ini.containsKey(INI_AUTH_SECTION)) {
                Ini.Section sec = this.ini.get(INI_AUTH_SECTION);
                this.auth = this.authConfigFactory.createConfig(sec);
            } else {
                /** If none given, just create an empty config */
                this.auth = new AuthServerConfig();
            }
        }
    }
    
    public AuthServerConfig getAuthConfig() {
        lazyAuthCfg();
        return this.auth;
    }
    
    public LdapAuthServerConfig getLdapAuthConfig() {
        lazyAuthCfg();
        return (LdapAuthServerConfig) (this.auth instanceof LdapAuthServerConfig ? this.auth : null);
    }

    public String get(String key) {
        return this.get(key, null);
    }

    /**
     * Gets the key from the ini file from the {@value #INI_SECTION} section
     * 
     * @param key
     * @param def default to return if the key is not found
     * @return value if found, default if not found and default provided
     * @throws RuntimeException if no value is found and no default provided
     */
    public String get(String key, String def) {
        return get(key, String.class, def);
    }
    
    public <T> T get(String key, Class<T> clazz, T def) {
        Ini.Section sec = this.ini.get(INI_SECTION);

        if(!sec.containsKey(key) && (def == null))
            throw new RuntimeException("Configuration key '" + key + "' not found");

        return sec.get(key, clazz, def);
    }

    public void put(String key, String value) {
        Ini.Section sec = this.ini.get(INI_SECTION);

        sec.put(key, value);
    }

    /**
     * Helper method to append a slash at the end of the url if it does not already exists
     * @param url The url to add a slash to
     * @return
     */
    protected static String slash(String url) {
        if (url == null) {
            return null;
        }
        return url + (url.endsWith("/") ? "" : "/");
    }

    /**
     * Loads the config file from the path given by the system property dawg.path. If dawg.path
     * is not set then the default location of /etc/dawg/ is used.
     * @param configFileName The name of the file in the dawg location to load the config from
     * @throws IOException
     */
    public void load(String configFileName) throws IOException {
        if(configFileName == null)
            this.ini = Config.loadIni();
        else
            this.ini = Config.loadIni(configFileName);

        if(!this.ini.containsKey(INI_SECTION))
            this.ini.add(INI_SECTION);
    }

    public boolean tryLoad(String configFileName) {
        try {
            this.load(configFileName);
            return true;
        }
        catch(IOException ioe) {
            return false;
        }
    }

    /**
     * Loads the properties file and inputs the key value pairs into this config object
     * @param is The input stream to load the properties from
     * @throws IOException
     */
    public void load(InputStream is) throws IOException {
        this.ini = Config.loadIni(is);

        if(!this.ini.containsKey(INI_SECTION))
            this.ini.add(INI_SECTION);
    }

    public boolean tryLoad(InputStream is) {
        try {
            this.load(is);
            return true;
        }
        catch(IOException ioe) {
            return false;
        }
    }
}
