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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comcast.cereal.CerealException;
import com.comcast.cereal.CerealSettings;
import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.PersistableDevice;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.controller.house.DawgHouseConfiguration;
import com.comcast.video.dawg.controller.house.filter.Condition;
import com.comcast.video.dawg.controller.house.filter.Operator;
import com.comcast.video.dawg.controller.house.filter.SearchableField;
import com.comcast.video.dawg.controller.house.filter.TagCondition;
import com.comcast.video.dawg.service.park.ParkService;

@Controller
public class ParkController {

    /** JSON array separator. */
    private static final String SEPARATOR_JSON_ARRAY = ",";

    /** JSON array end element. */
    private static final String END_ELEMENT_JSON_ARRAY = "]";

    /** JSON array start element. */
    private static final String START_ELEMENT_JSON_ARRAY = "[";

    /** Empty JSON object. */
    private static final String EMPTY_JSON_OBJECT = "{}";

    /** Name of session attribute where advanced search conditions are saved. */
    private static final String SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME = "searchConditions";

    /** Name of session attribute where latest advanced search condition saved. */
    private static final String LATEST_SEARCH_CONDITION_SESSION_ATTRIBUTE_NAME = "latestSearchCondition";

    /** Name of model attribute where advanced search conditions are saved. */
    private static final String SEARCH_CONDITIONS_MODEL_ATTRIBUTE_NAME = SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME;

    /** Name of model attribute where latest advanced search condition saved. */
    private static final String LATEST_SEARCH_CONDITION_MODEL_ATTRIBUTE_NAME = LATEST_SEARCH_CONDITION_SESSION_ATTRIBUTE_NAME;

    Logger logger = Logger.getLogger(ParkController.class);

    @Autowired
    ParkService service;
    @Autowired
    ServerUtils serverUtils;

    @Autowired
    private DawgHouseConfiguration config;

    JsonCerealEngine engine = new JsonCerealEngine(false);

    @PostConstruct
    public void init() {
        CerealSettings settings = new CerealSettings();
        settings.setIncludeClassName(false);
        engine.setSettings(settings);
    }

    @RequestMapping({ "/", "index*" })
    public String index(Model model) throws CerealException {
        return "login";
    }
    
    @RequestMapping(value="/login", method = { RequestMethod.GET })
    public ModelAndView loginView(@RequestParam(required=false) String invalid) throws Exception {
    ModelAndView mav = new ModelAndView("login");
    if (invalid != null) {
    	ArrayList<String> errors = new ArrayList<String>();
    	errors.add("Invalid credentials");
    	mav.addObject("loginErrors", errors);
    	}
    return mav;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/{token}*")
    public String userFront(@PathVariable String token,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean asc,
            @RequestParam(required = false) String[] sort, Model model,
            HttpSession session) throws CerealException{

        StringBuilder builder = new StringBuilder();
        builder.append("Request=").append("token=").append(token).append(",q=")
            .append(q).append(",tag=").append(tag).append(",page=")
            .append(page).append(",size=").append(size).append(",asc=")
            .append(asc).append(",sort=").append(sort);
        logger.info(builder.toString());
        if (isValid(token)) {
            token = clean(token);

            Map<String, Object>[] stbs = null;
            Map<String, Object>[] allDevices = null;
            Pageable pageable = null;
            if (null != sort) {
                if (0 == size) {
                    size = 100;
                }
                Direction direction = asc ? Direction.ASC : Direction.DESC;
                Sort sorting = new Sort(direction, sort);
                pageable = new PageRequest(page, size, sorting);
            }

            if (null != tag) {
                if (null != q) {
                    model.addAttribute("search", q);
                }
                stbs = service.findByCriteria(new TagCondition(tag, q).toCriteria(), pageable);
            } else if ((null != q) && (!q.trim().isEmpty())) {
                String[] keys = getKeys(q);
                stbs = service.findByKeys(keys, pageable);
                model.addAttribute("search", q);
            }else {

                Condition condition = (Condition) session
                    .getAttribute("condition");
                if (condition != null) {
                    stbs = service.findByCriteria(condition.toCriteria(),
                            pageable);
                    session.removeAttribute("condition");
                } else {
                    stbs = service.findAll(pageable);
                    model.addAttribute("search", "");
                    allDevices = stbs;
                }
            }

            Map<String, Object> requestParams = new HashMap<String, Object>();
            requestParams.put("tag",tag);
            requestParams.put("q", q);
            requestParams.put("page", page);
            requestParams.put("size", size);
            requestParams.put("asc", asc);
            requestParams.put("sort", sort);

            String requestParamsJson = engine.writeToString(requestParams,
                    Map.class);
            model.addAttribute("requestParams", requestParamsJson);

            Map<String, Object> devices = new HashMap<String, Object>();
            Map<String, List<String>> deviceTags = new HashMap<String, List<String>>();
            Object tagData = null;
            boolean anyReserved = false;
            for (Map<String, Object> stb : stbs) {
                if (stb.containsKey(MetaStb.MACADDRESS)) {
                    String id = (String) stb.get(MetaStb.ID);
                    devices.put(id, stb);
                    tagData = stb.get(MetaStb.TAGS);
                    if (tagData != null && (tagData instanceof List)) {
                        deviceTags.put(id, (List<String>) tagData);
                    } else {
                        deviceTags.put(id, null);
                    }

                    String reserver = (String) stb
                        .get(PersistableDevice.RESERVER);
                    if ((token != null) && token.equals(reserver)) {
                        anyReserved = true;
                    }
                }
            }
            String deviceData = engine.writeToString(devices, Map.class);

            /** Get all the tags from other devices */
            allDevices = allDevices == null ? service.findAll() : allDevices;
            Collection<String> otherTags = new HashSet<String>();
            for (Map<String, Object> device : allDevices) {
                String id = (String) device.get(MetaStb.ID);
                if (!deviceTags.containsKey(id)) {
                    tagData = device.get(MetaStb.TAGS);
                    if(tagData != null && (tagData instanceof List)) {
                        otherTags.addAll((List<String>) tagData);
                    }
                }
            }

            model.addAttribute(LATEST_SEARCH_CONDITION_MODEL_ATTRIBUTE_NAME,
                    returnAndRemoveLatestSearchCondition(session));
            model.addAttribute(SEARCH_CONDITIONS_MODEL_ATTRIBUTE_NAME, getSearchConditions(session));

            model.addAttribute("token", token);
            model.addAttribute("deviceData", deviceData);
            model.addAttribute("deviceTags", engine.writeToString(deviceTags));
            model.addAttribute("otherTags",
                    engine.writeToString(new ArrayList<String>(otherTags)));
            model.addAttribute("data", devices.values());
            model.addAttribute("dawgShowUrl", config.getDawgShowUrl());
            model.addAttribute("dawgPoundUrl", config.getDawgPoundUrl());
            model.addAttribute("anyReserved", anyReserved);
            model.addAttribute("filterFields", SearchableField.values());
            model.addAttribute("operators", Operator.values());
            return "index";
        } else {
            /** User login token is invalid, so redirects to login page */
            return "login";
        }

    }

    @RequestMapping(value = "/filter/store", method = RequestMethod.POST)
    @ResponseBody
    public void storeFilter(@RequestBody String condition, HttpSession session) throws CerealException, ClassNotFoundException {
        /** Retaining the search condition in session to display the last advanced search executed by user. */
        session.setAttribute(LATEST_SEARCH_CONDITION_SESSION_ATTRIBUTE_NAME, condition);

        Condition c = engine.readFromString(condition, Condition.class);
        session.setAttribute("condition", c);
        validateAndUpdateSessionSearchConditions(c, session);
    }

    /**
     * Controller to remove the search condition stored in the history.
     *
     * @param condition
     *            advance search condition that need to be removed from the search condition list available in the
     *            session.
     * @param session
     *            User session object, where user previously executed search conditions are present.
     */
    @RequestMapping(value = "/filter/remove", method = RequestMethod.POST)
    @ResponseBody
    public void removeFilter(@RequestBody String condition, HttpSession session) {
        List<Condition> searchConditions = (List<Condition>) session
            .getAttribute(SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME);
        if (!CollectionUtils.isEmpty(searchConditions) && null != condition ) {
            try {
                Condition conditionToRemove = engine.readFromString(condition, Condition.class);
                searchConditions.remove(conditionToRemove);
            } catch (CerealException cerealException) {
                logger.error(String.format(
                            "Failed to convert search condition in JSON \"%s\" to object and remove it from list.", condition),
                        cerealException);
            }
        }
        session.setAttribute(SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME, searchConditions);
    }

    /**
     * Method add the new search condition to the advance search list present in the session. Before adding to the list
     * it removes any duplicate entry present in the list.
     *
     * @param condition
     *            advance search condition carried out by user.
     * @param session
     *            User session object where user previously executed search conditions are present.
     */
    private void validateAndUpdateSessionSearchConditions(Condition condition, HttpSession session) {
        /** Add the new advanced search condition to previously executed search list. */
        List<Condition> searchConditions = (List<Condition>) session
            .getAttribute(SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME);
        if (null != searchConditions) {
            // If the user carried out a similar advance search earlier, it need to be removed from the previous
            // stored list.
            searchConditions.remove(condition);
        } else {
            searchConditions = new CopyOnWriteArrayList<Condition>();
        }
        searchConditions.add(condition);
        session.setAttribute(SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME, searchConditions);
    }

    /**
     * Provides the list of previously executed advanced search condition in a JSON array.
     *
     * @param session
     *            User session object where user previously executed search condition are present.
     * @return JSON array string of search carried out by user, blank JSON array string if no search where executed by
     *         the user.
     */
    private String getSearchConditions(HttpSession session) {
        String searchCondtions = new String(START_ELEMENT_JSON_ARRAY + END_ELEMENT_JSON_ARRAY);
        List<Condition> searchConditionList = (List<Condition>) session
            .getAttribute(SEARCH_CONDITIONS_SESSION_ATTRIBUTE_NAME);
        if (!CollectionUtils.isEmpty(searchConditionList)) {
            // --class representation is required in JSON output, so not using the class level JSON engine.
            JsonCerealEngine jsonEngine = new JsonCerealEngine(true);
            jsonEngine.getSettings().setUseObjectReferences(false);
            try {
                searchCondtions = jsonEngine.writeToString(searchConditionList, List.class);
            } catch (CerealException cerealException) {
                logger.error("Failed to convert the search condition to JSON.", cerealException);
            }
        }
        return searchCondtions;
    }

    /**
     * Provides the user latest executed search JSON string extracted from the user session object. After processing the
     * request it removes the latest search condition from session
     *
     * @param session
     *            User session object where last user executed advance search condition is present.
     * @return Latest JSON advanced search condition executed by user, blank JSON object if no latest advanced search
     *         executed by user.
     */
    private String returnAndRemoveLatestSearchCondition(HttpSession session) {
        String searchCondition = EMPTY_JSON_OBJECT;
        Object sessionSearchCondtion = session.getAttribute(LATEST_SEARCH_CONDITION_SESSION_ATTRIBUTE_NAME);
        if (null != sessionSearchCondtion) {
            searchCondition = (String) sessionSearchCondtion;
            session.removeAttribute(LATEST_SEARCH_CONDITION_SESSION_ATTRIBUTE_NAME);
        }
        return searchCondition;
    }

    private String[] getKeys(String query) {
        query = query.toLowerCase();
        logger.info("keys=" + (Arrays.asList(query.split(" "))));
        return query.split(" ");
    }

    protected String clean(String string) {
        return serverUtils.clean(string);
    }

    protected boolean isValid(String string) {
        return serverUtils.isValid(string);
    }
}
