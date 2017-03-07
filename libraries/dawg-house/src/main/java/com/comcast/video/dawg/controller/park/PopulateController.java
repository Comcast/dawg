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
package com.comcast.video.dawg.controller.park;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.video.dawg.controller.house.ChimpsToken;
import com.comcast.video.dawg.controller.house.HouseRestController;
import com.comcast.video.dawg.service.park.ParkService;

/**
 * This is a controller for managing persisting chimps tokens so that they don't
 * have to constantly be readded.
 *
 * This also allows chimps to be autoscraped periodically
 *
 * @author Val Apgar
 *
 */
@Controller
public class PopulateController {

    Logger logger = LoggerFactory.getLogger(PopulateController.class);

    @Autowired
    HouseRestController houseController;
    @Autowired
    ParkService parkService;

    @RequestMapping(value = "devices/populateAndStore/{token}")
    @ResponseBody
    public int populate(@PathVariable String token,
            @RequestParam(required = false, defaultValue = "false", value = "isToOverride") Boolean isToOverrideMetaData) {
        int populated = houseController.populate(token, isToOverrideMetaData);
        parkService.saveToken(new ChimpsToken(token));
        return populated;
    }

    @RequestMapping(value = "populate")
    public String populate(Model model) {
        List<ChimpsToken> population = parkService.getSavedTokens();
        if (null == population) {
            population = new ArrayList<ChimpsToken>();
        }
        model.addAttribute("population", population);
        return "populate";
    }

    @RequestMapping(value = "populateTable")
    public String populateTable(Model model) {
        model.addAttribute("population", parkService.getSavedTokens());
        return "populateTable";
    }

    //day * hours / day * minutes / hour * seconds / minute * milli / seconds
    // must use a literal :(
    @Scheduled(fixedRate=1*12*60*60*1000)
    public void populate() {
        logger.info("POPULATE ALL THE THINGS!!!!@");
        for(ChimpsToken token : parkService.getSavedTokens()) {
            houseController.populate(token.getToken(), false);
        }
    }

    @RequestMapping(value = "repopulate")
    public String repopulate(Model model) {
        populate();
        return populate(model);
    }
}
