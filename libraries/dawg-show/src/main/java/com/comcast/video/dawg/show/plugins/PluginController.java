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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.CerealSettings;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.common.plugin.PluginConfigValue;
import com.comcast.video.dawg.common.plugin.PluginConfiguration;
import com.comcast.video.dawg.show.ViewConstants;

/**
 * Controller for adding, removing, getting plugins
 * @author Kevin Pearson
 *
 */
@Controller
public class PluginController implements ViewConstants {

    @Autowired
    private RemotePluginManager pluginManager;

    private JsonCerealEngine jsonEngine = new JsonCerealEngine();

    public PluginController() {
        this.jsonEngine = new JsonCerealEngine();
        CerealSettings settings = new CerealSettings();
        settings.setIncludeClassName(false);
        this.jsonEngine.setSettings(settings);
    }

    /**
     * Uploads plugins from a jar
     * @param jarBase64 The base64 data representing the .jar file
     * @throws IOException
     */
    @RequestMapping(method = { RequestMethod.POST }, value = "/plugins/remote")
    @ResponseBody
    public void uploadRemotePlugin(@RequestBody String jarBase64) throws IOException {
        pluginManager.storePlugin(jarBase64);
    }

    /**
     * Gets the configuration for a particular plugin
     * @param remoteType The remote type that identifies what plugin to get
     * @return
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(method = { RequestMethod.GET }, value = "/plugins/remote/{remoteType}")
    @ResponseBody
    public PluginConfiguration getRemotePlugin(@PathVariable String remoteType) throws IOException {
        PluginAndConfig pac = pluginManager.getPlugin(remoteType);
        return pac == null ? null : pac.getConfig();
    }

    /**
     * Removes a plugin from being stored.
     * @param remoteType The remote type that identifies what plugin to get
     * @throws IOException
     */
    @RequestMapping(method = { RequestMethod.DELETE }, value = "/plugins/remote/{remoteType}")
    @ResponseBody
    public void removeRemotePlugin(@PathVariable String remoteType) throws IOException {
        this.pluginManager.removePlugin(remoteType);
    }

    /**
     * Gets the config and display map. Display map is a mapping from field name to how the field
     * should be displayed in the UI
     * @param remoteType The remote type that identifies what plugin to get
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked" })
    @RequestMapping(method = { RequestMethod.GET }, value = "/plugins/remote/cdm/{remoteType}")
    @ResponseBody
    public Map<String, Object> getPluginConfigAndDisplayMap(@PathVariable String remoteType) throws IOException {
        PluginConfiguration config = getRemotePlugin(remoteType);
        if (config == null) return null;
        Map<String, String> displayMap = new HashMap<String, String>();
        Set<Field> fields = ReflectionUtils.getFields(config.getClass(), ReflectionUtils.withAnnotation(PluginConfigValue.class));
        for (Field field : fields) {
            String disp = field.getAnnotation(PluginConfigValue.class).display();
            if (StringUtils.isEmpty(disp)) {
                disp = field.getName();
            }
            displayMap.put(field.getName(), disp);
        }
        Map<String, Object> configAndDM = new HashMap<>();
        configAndDM.put("config", config);
        configAndDM.put("displayMap", displayMap);
        return configAndDM;
    }

    /**
     * Updates the configuration for a particular plugin.
     * The body is the json representation of the plugin
     * @param remoteType The remote type that identifies what plugin to get
     * @param config The body that represents the new configuration
     * @throws IOException
     * @throws CerealException
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(method = { RequestMethod.POST }, value = "/plugins/remote/config/{remoteType}")
    @ResponseBody
    public void updatePluginConfig(@PathVariable String remoteType, @RequestBody Map<String, Object> config) throws IOException, CerealException {
        PluginAndConfig pac = pluginManager.getPlugin(remoteType);
        if (pac != null) {
            jsonEngine.apply(config, pac.getConfig());
        }
    }

    /**
     * Gets the view of all the plugin remotes
     * @return
     * @throws CerealException
     */
    @RequestMapping(method = { RequestMethod.GET }, value = "/view/plugins/remotes")
    public ModelAndView uploadRemotePluginView() throws CerealException {
        ModelAndView mav = new ModelAndView(REMOTE_PLUGIN);
        mav.addObject("plugins", jsonEngine.writeToString(pluginManager.getPluginClassnames()));
        return mav;
    }
}
