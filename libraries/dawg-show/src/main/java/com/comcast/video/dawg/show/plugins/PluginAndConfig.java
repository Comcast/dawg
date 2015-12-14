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
package com.comcast.video.dawg.show.plugins;

import java.io.File;

import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.plugin.DawgPlugin;
import com.comcast.video.dawg.common.plugin.PluginConfiguration;

/**
 * A single object to hold a plugin, its configuration and the jar file it came from
 * @author Kevin Pearson
 *
 * @param <T> The class of the object that the plugin provides
 * @param <C> The class of the configuration
 */
public class PluginAndConfig<T, C extends PluginConfiguration> {
    private DawgPlugin<T, C> plugin;
    private C config;
    private File jarFile;

    /**
     * Creates a PluginAndConfig
     * @param plugin The plugin
     * @param config The configuration for the plugin
     * @param jarFile The location of the jarFile on the server
     */
    public PluginAndConfig(DawgPlugin<T, C> plugin, C config, File jarFile) {
        this.plugin = plugin;
        this.config = config;
        this.jarFile = jarFile;
    }

    public DawgPlugin<T, C> getPlugin() {
        return plugin;
    }

    public void setPlugin(DawgPlugin<T, C> plugin) {
        this.plugin = plugin;
    }

    public C getConfig() {
        return config;
    }

    public void setConfig(C config) {
        this.config = config;
    }

    /**
     * Quick method for calling {@link DawgPlugin#getInstance(PluginConfiguration, MetaStb)}
     * for the plugin and configuration.
     * @param stb The set top box metadata to get the plugin instance for
     * @return
     */
    public T getInstance(MetaStb stb) {
        return this.plugin.getInstance(config, stb);
    }

    public File getJarFile() {
        return jarFile;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }
}
