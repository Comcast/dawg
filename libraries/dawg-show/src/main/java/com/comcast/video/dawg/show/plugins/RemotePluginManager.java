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
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.plugin.KeyInputPlugin;
import com.comcast.video.dawg.common.plugin.PluginConfiguration;
import com.comcast.video.dawg.show.DawgShowConfiguration;
import com.comcast.video.dawg.show.key.Remote;
import com.comcast.video.dawg.show.key.RemoteManager;
import com.comcast.video.stbio.KeyInput;

/**
 * Bean class to hold and manage plugins
 * @author Kevin Pearson
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class RemotePluginManager {
    public static final Logger LOGGER = Logger.getLogger(RemotePluginManager.class);

    @Autowired
    private DawgShowConfiguration config;

    @Autowired
    private RemoteManager remoteManager;

    private Map<String, PluginAndConfig<KeyInput, ? extends PluginConfiguration>> plugins = new HashMap<>();

    /**
     * When the server starts up it will reload all the plugins back in
     * @throws IOException
     */
    @PostConstruct
    public void loadPlugins() throws IOException {
        File pluginsDir = new File(config.getPluginDir());
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs();
        }
        if (pluginsDir.exists()) {
            FilenameFilter jarSuffix = new SuffixFileFilter(".jar");
            File[] files = pluginsDir.listFiles(jarSuffix);
            for (File file : files) {
                storePlugin(file);
            }
        } else {
            LOGGER.warn("Plugins directory does not exist '" + pluginsDir.getAbsolutePath() + "'");
        }
    }

    /**
     * Stores a jar and finds all the plugin classes in that jar
     * @param jarBase64 The base64 string representing the jar
     * @throws IOException
     */
    public void storePlugin(String jarBase64) throws IOException {
        byte[] jarBytes = Base64.decodeBase64(jarBase64);
        File file = File.createTempFile("plugin", ".jar", new File(config.getPluginDir()));
        FileUtils.writeByteArrayToFile(file, jarBytes);
        storePlugin(file);
    }

    /**
     * Stores a jar and finds all the plugin classes in that jar
     * @param jarFile The jar file on the server
     * @throws IOException
     */
    public void storePlugin(File jarFile) throws IOException {
        URL jarUrl = jarFile.toURI().toURL();
        URLClassLoader urlcl = new URLClassLoader(new URL [] {jarUrl}, this.getClass().getClassLoader());
        Reflections ref = new Reflections(
                new ConfigurationBuilder().setUrls(
                    ClasspathHelper.forClassLoader(urlcl)
                    ).addClassLoader(urlcl)
                );
        /** Find all classes in the jar that implement a KeyInputPlugin and add them to our plugin list */
        Set<Class<? extends KeyInputPlugin>> plugins = ref.getSubTypesOf(KeyInputPlugin.class);
        for (Class<? extends KeyInputPlugin> clazz : plugins) {
            try {
                KeyInputPlugin<?> kip = clazz.newInstance();
                PluginConfiguration config = kip.getConfigClass().newInstance();
                PluginAndConfig<KeyInput, ?> pac = new PluginAndConfig(kip, config, jarFile);
                LOGGER.info("Adding plugin class '" + clazz.getName() + "' for remote " + kip.getRemoteType());
                if (this.plugins.containsKey(kip.getRemoteType())) {
                    /** Remove the old remote plugin */
                    File oldJar = this.plugins.get(kip.getRemoteType()).getJarFile();
                    FileUtils.deleteQuietly(oldJar);
                }
                this.plugins.put(kip.getRemoteType(), pac);
                /** Updates the mapping to the UI remote */
                this.remoteManager.addRemoteTypeToRemote(kip.getRemoteUiId(), kip.getRemoteType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes the plugin for the given remote.
     * If this is the last plugin that belongs to the jar then the jar is also removed
     * TODO If multiple plugins belong to same jar and one plugin is deleted, when the server restarts
     *      the deleted one will come back. Most plugins are one per jar and single classes in that jar
     *      aren't usually removed, this is is a far edge case.
     * @param remoteType The remote type that identifies what plugin to get
     */
    public void removePlugin(String remoteType) {
        PluginAndConfig removed = this.plugins.remove(remoteType);
        if (removed != null) {
            boolean jarStillUsed = false;
            for (PluginAndConfig pac : plugins.values()) {
                if (pac.getJarFile().equals(removed.getJarFile())) {
                    jarStillUsed = true;
                    break;
                }
            }
            if (!jarStillUsed) {
                FileUtils.deleteQuietly(removed.getJarFile());
            }
            Remote remote = this.remoteManager.getRemote(remoteType);
            if (remote != null) remote.getRemoteTypes().remove(remoteType);
        }
    }

    /**
     * Gets the KeyInput for a particular stb to use.
     * TODO Should probably implement some sort of caching so we aren't reconstructing
     * a KeyInput every time we send a key.
     * @param stb The stb metadata
     * @param remoteType The overriding parameter for the remote to use
     * @return
     */
    public KeyInput getKeyInput(MetaStb stb, String remoteType) {
        String rt = StringUtils.isEmpty(remoteType) ? stb.getRemoteType() : remoteType;
        PluginAndConfig<KeyInput, ? extends PluginConfiguration> pac = this.plugins.get(rt);
        if (pac != null) {
            KeyInput ki = pac.getInstance(stb);
            if (ki != null) {
                return ki;
            }
        }
        return new IrClient(stb, rt);

    }

    /**
     * Gets the plugin and config for a particular remoteType
     * @param remoteType The remote type that identifies what plugin to get
     * @return
     */
    public PluginAndConfig getPlugin(String remoteType) {
        return this.plugins.get(remoteType);
    }

    /**
     * Creates a mapping between remoteType to the actual class name of the
     * plugin
     * @return
     */
    public Map<String, String> getPluginClassnames() {
        Map<String, String> classNameMap = new HashMap<String, String>();
        for (String remoteType : plugins.keySet()) {
            PluginAndConfig pac = plugins.get(remoteType);
            classNameMap.put(remoteType, pac.getPlugin().getClass().getName());
        }
        return classNameMap;
    }
}
