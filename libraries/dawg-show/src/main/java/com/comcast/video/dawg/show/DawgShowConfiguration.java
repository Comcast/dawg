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
package com.comcast.video.dawg.show;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.comcast.video.dawg.common.DawgConfiguration;

/**
 * Loads the configuration from a configuration file for the dawg-show server
 * @author Kevin Pearson
 *
 */
@Component
public class DawgShowConfiguration extends DawgConfiguration {

    public static final String OCR_URL = "ocr-url";
    public static final String PLUGINS_DIR = "plugins.dir";
    public static final String HOUSE_USER = "house.user";
    public static final String HOUSE_PASSWORD = "house.password";
    public static final String RACK_PROXY_URL_DEFAULT = "rack.proxy.url.default";
    public static final String RACK_PROXY_ENABLED_DEFAULT = "rack.proxy.enabled.default";
    public static final String DEFAULT_PLUGINS_DIR = "/opt/dawg/plugins";

    /**
     * Called when the bean is created and does the actual config loading
     * @throws IOException
     */
    @PostConstruct
    public void initConfig() throws IOException {
        String fileName = null;

        this.load(fileName);
    }

    public String getOCRUrl() {
        return DawgConfiguration.slash(this.get(OCR_URL));
    }

    public String getPluginDir() {
        return this.get(PLUGINS_DIR, DEFAULT_PLUGINS_DIR);
    }

    public String getDawgHouseUser() {
        return this.get(HOUSE_USER);
    }

    public String getDawgHousePassword() {
        return this.get(HOUSE_PASSWORD);
    }

    public String getRackProxyUrlDefault() {
        return this.get(RACK_PROXY_URL_DEFAULT, "");
    }

    public Boolean getRackProxyEnabledDefault() {
        return this.get(RACK_PROXY_ENABLED_DEFAULT, Boolean.class, false);
    }
}
