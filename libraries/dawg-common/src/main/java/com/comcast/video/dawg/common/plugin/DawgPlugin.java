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
package com.comcast.video.dawg.common.plugin;

import com.comcast.video.dawg.common.MetaStb;

/**
 * Base interface for creating a class that can be plugged into dawg
 * @author Kevin Pearson
 *
 * @param <T> The type of object that this plugin provides, usually an stb io
 * @param <C> The type of configuration used to configure the plugin
 */
public interface DawgPlugin<T, C extends PluginConfiguration> {

    /**
     * Gives the class that can be used to configure the plugin
     * @return
     */
    public Class<? extends PluginConfiguration> getConfigClass();

    /**
     * Gets the actual instance of the object that the plugin provides
     * @param config The configuration for the plugin
     * @param stb The metadata of the stb that the plugin will be for
     * @return
     */
    public T getInstance(C config, MetaStb stb);
}
