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
package com.comcast.video.dawg.controller.pound;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.comcast.video.dawg.common.DawgConfiguration;

/**
 * Loads the configuration from a configuration file for the dawg-pound server
 * @author Kevin Pearson
 *
 */
@Component
public class DawgPoundConfiguration extends DawgConfiguration {

    public static final String DAWG_DB_HOST = "dawg-db-host";

    /**
     * Called when the bean is created and does the actual config loading
     * @throws IOException
     */
    @PostConstruct
    public void initConfig() throws IOException {
        String fileName = null;

        this.load(fileName);
    }

    public String getDawgDbHost() {
        return this.get(DAWG_DB_HOST);
    }
}
