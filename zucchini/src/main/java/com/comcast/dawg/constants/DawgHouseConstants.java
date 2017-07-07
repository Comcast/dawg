/**
 * Copyright 2017 Comcast Cable Communications Management, LLC
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
package com.comcast.dawg.constants;

/**
 * Constants used for Dawg house automation
 * @author  priyanka
 */
public class DawgHouseConstants {
    /**
     * Defines various test context variables
     */
    public static final String CONTEXT_WEB_DRIVER = "driver";
    public static final String CONTEXT_SAUCE_DRIVER = "saucedriver";
    public static final String CONTEXT_SCENARIO = "scenario";
    public static final String CONTEXT_USERNAME = "username";
    public static final String CONTEXT_PASSWORD = "password";
    public static final String CONTEXT_TAG1 = "tagOne";
    public static final String CONTEXT_TAG2 = "tagTwo";
    public static final String CONTEXT_TAG1_STBS = "tagOneStbs";
    public static final String CONTEXT_TAG2_STBS = "tagTwoStbs";
    public static final String CONTEXT_REST_RESPONSE = "response";
    public static final String CONTEXT_DAWG_HOUSE_URL = "dawgHouseurl";
    public static final String CONTEXT_DAWG_SHOW_URL = "dawgShowurl";
    public static final String CONTEXT_DAWG_POUND_URL = "dawgPoundurl";
    public static final String CONTEXT_TEST_STBS = "testStbs";
    public static final String CONTEXT_TEST_TAGS = "testTags";
    public static final String CONTEXT_TEST_STB_MODEL = "testStbModel";
    public static final String CONTEXT_NEW_PROPERTY_ADDED = "newCapabilityAdded";
    public static final String CONTEXT_PROPERTY_LIST = "capList";
    public static final String CONTEXT_ALREADY_EXISTING_PROPERTY = "configProperty";
    public static final String CONTEXT_NEW_STB_MODEL = "newModel";
    public static final String CONTEXT_MODEL_PROPERTIES = "newCapability";
    public static final String CONTEXT_AUTH_HEADER = "Authorization";
    public static final String CONTEXT_ALERT = "alert";
    public static final String CONTEXT_FILTER_CONDITION = "filterCondition";
    public static final String CONTEXT_EXPECTED_CONDITION = "expectedCondition";
    public static final String CONTEXT_FILTER_COUNT = "filterCount";
    public static final String CONTEXT_COOKIE = "cookie";
    public static final String CONTEXT_TEST_STB_ID = "advaceFilterStbId";
    public static final String CONTEXT_CONDTN_BTNS = "condtnBtns";
    public static final String CONTEXT_FILTER_TO_DELETE = "filterToDelete";
    public static final String CONTEXT_FILTER_TO_NOT_DELETE = "filterToNotDelete";

    /**
     * Defines various constants used in dawg-house automation
     */
    public static final String INVALID_LOGIN_MSG = "Invalid credentials";
    public static final String INVALID_USERNAME = "$$$$$";
    public static final String INVALID_PASSWORD = "password";
    public static final String SECRET_KEY = "14C549805A5911368929D139C5C6282E66CE017F8E861C4EAC4D2975269BEA6F";
    public static final String CONTENT_TYPE = "application/json";
    public static final String COOKIE = "Cookie";
    public static final String DAWG_COOKIE_PREFIX = "dawt=";
    public static final String DAWG_COOKIE_PATTERN = "\\[name: dawt\\]\\[value: (.+?)\\]";
    public static final String FAMILY = "family";
    public static final String MODEL = "model";
    public static final String CAPABILITY = "capability";
    public static final String SIGN_IN_BUTTON = "Sign In";
    public static final String DELETE_MODEL_BUTTON = "delete model";
    public static final String ADD_OR_UPDATE_MODEL = "Add or update model";
    public static final String GET_MODEL = "Get model";
    public static final String ASSIGN_MODEL = "Add or update model";
    public static final String ADD_OR_REMOVE_TAG = "Update tag";
    public static final String ADD_OR_REMOVE_STB = "Add or remove STB";
    public static final String DAWG_ROLES_STR = "roles";
    public static final String DEVICE_IDS = "deviceIds";
    public static final String CREDENTIALS = "creds";
    public static final String DAWG_USER_STR = "dawguser";
    public static final String DAWG_HOUSE_STR = "dawg-house";
    public static final String MODEL_CONFIG = "modelConfig";
    public static final String SPLIT_REGEX = "\\s*,\\s*";
    public static final String REPLACE_REGEX = "[()]";
    public static final String FILTER_REQ_PARAM = "testtoken" + System.currentTimeMillis() + "?q=return_nothing_query";
    public static final String BTN_NAME_AND = "AND";
    public static final String BTN_NAME_OR = "OR";
    public static final String BTN_NAME_NOT = "NOT";
    public static final String BTN_NAME_DELETE = "DEL";
    public static final String BTN_NAME_BREAK = "BREAK";
    public static final String BTN_NAME_SEARCH = "Search";
    public static final String BTN_NAME_ADD = "Add";
    public static final String CHECK_FILTERS = "check";
    public static final String UNCHECK_FILTERS = "unCheck";
}
