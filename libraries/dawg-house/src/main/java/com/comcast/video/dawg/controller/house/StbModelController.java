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
package com.comcast.video.dawg.controller.house;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comcast.video.dawg.common.DawgModel;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.service.house.HouseService;
import com.comcast.video.dawg.service.park.ParkService;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Family;
import com.comcast.video.stbio.meta.Model;

@Controller
public class StbModelController {

    @Autowired
    ParkService service;
    @Autowired
    HouseService houseService;

    @RequestMapping(value="models", method=RequestMethod.GET)
    @ResponseBody
    public List<DawgModel> getModels(@RequestParam String[] id) {
        return service.getModelsByIds(id);
    }

    /**
     * REST api to add a model
     * @param newModel The model to add
     * @return
     */
    @RequestMapping(value="models/{id}", method=RequestMethod.POST)
    @ResponseBody
    public boolean addModel(@RequestBody DawgModel newModel, @PathVariable String id) {
        DawgModel existing = service.getModelById(id);
        service.upsertModel(newModel);
        if (existing != null) {
            /** Make a diff between the existing model and the new model
             * and then apply that diff to every stb that is this
             * model */
            List<String> addedCaps = new ArrayList<String>();
            List<String> removedCaps = new ArrayList<String>();
            List<String> existingCaps = existing.getCapabilities();
            List<String> newCaps = newModel.getCapabilities();
            for (String cap : newCaps) {
                if (!existingCaps.contains(cap)) {
                    addedCaps.add(cap);
                }
            }
            for (String cap : existingCaps) {
                if (!newCaps.contains(cap)) {
                    removedCaps.add(cap);
                }
            }
            Map<String, Object>[] datas = houseService.getStbsByKey(newModel.getName());
            for (Map<String, Object> data : datas) {
                MetaStb stb = new MetaStb(data);
                if (stb.getModel().name().equals(newModel.getName())) {
                    if (!stb.getData().containsKey(MetaStb.CAPABILITIES)) {
                        stb.setCapabilities(enumerate(newCaps));
                    } else {
                        Collection<Capability> caps = stb.getCapabilities();
                        caps.addAll(enumerate(addedCaps));
                        caps.removeAll(enumerate(removedCaps));
                        stb.setCapabilities(caps);
                    }
                    stb.setFamily(Family.valueOf(newModel.getFamily()));
                    houseService.upsertStb(stb);
                }
            }
        } else {
            Map<String, Object>[] datas = houseService.getStbsByKey(newModel.getName());
            applyModels(datas, Arrays.asList(newModel));
        }
        return true;
    }

    /**
     * REST api to delete a model
     * @param id The id of the model to delete
     * @return
     */
    @RequestMapping(value="models/{id}", method=RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteModel(@PathVariable String id) {
        service.deleteModel(id);
        return true;
    }

    /**
     * REST api to get the view that shows all the models in a table
     * @return
     */
    @RequestMapping("/modelsConfig")
    public ModelAndView models() {
        List<DawgModel> models = service.getModels();
        Set<String> caps = new HashSet<String>();
        Set<String> families = new HashSet<String>();
        for (DawgModel model : models) {
            caps.addAll(model.getCapabilities());
            families.add(model.getFamily());
        }

        ModelAndView dataModel = new ModelAndView("models");
        dataModel.addObject("models", models);
        dataModel.addObject("capabilities", caps);
        dataModel.addObject("families", families);
        return dataModel;
    }

    /**
     * REST api to look for an stbs that do not have capabilities or family and
     * assigns them based on the model type
     */
    @RequestMapping("/assignmodels")
    public ModelAndView assignModels() {
        applyModels(houseService.getAll(), service.getModels()) ;
        return models();
    }

    /**
     * Looks through the list of stbs and finds ones that do not have capabilities or family. Then
     * looks through the list of models for a matching model and assigns the stb the family and capabilities of the model
     * @param stbs The stbs to populate family and capabilities for
     * @param models The mapping of model names to family and capabilities
     */
    public void applyModels(Map<String, Object>[] stbs, List<DawgModel> models) {
        for (Map<String, Object> map : stbs) {
            MetaStb stb = new MetaStb(map);
            Model stbModel = stb.getModel();
            if (null != stbModel) {
                boolean hasCapabilities = stb.getData().get(MetaStb.CAPABILITIES) != null;
                boolean hasFamily = stb.getData().get(MetaStb.FAMILY) != null;
                if (!hasCapabilities || !hasFamily) {
                    for (DawgModel model : models) {
                        if (model.getName().equals(stbModel.name())) {
                            if (!hasCapabilities) {
                                stb.setCapabilities(enumerate(model.getCapabilities()));
                            }
                            if (!hasFamily) {
                                stb.setFamily(Family.valueOf(model.getFamily()));
                            }
                            houseService.upsertStb(stb);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static Collection<Capability> enumerate(Collection<String> caps) {
        Collection<Capability> rv = new ArrayList<Capability>();
        for (String cap : caps) {
            rv.add(Capability.valueOf(cap));
        }
        return rv;
    }
}
