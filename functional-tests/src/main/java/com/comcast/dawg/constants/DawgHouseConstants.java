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

import java.util.concurrent.TimeUnit;

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
    public static final String CONTEXT_TEST_TAG1 = "tagOne";
    public static final String CONTEXT_TEST_TAG2 = "tagTwo";
    public static final String CONTEXT_TEST_TAG3 = "tagThree";
    public static final String CONTEXT_TEST_EXISTING_TAG1 = "bulkTag1";
    public static final String CONTEXT_TEST_EXISTING_TAG2 = "bulkTag2";
    public static final String CONTEXT_DELETE_TAG1 = "tag1Delete";
    public static final String CONTEXT_DELETE_TAG2 = "tag2Delete";

    public static final String CONTEXT_TAG1_WITH_STB_SET1 = "tagOneStbs";
    public static final String CONTEXT_TAG2_WITH_STB_SET2 = "tagTwoStbs";
    public static final String CONTEXT_TAG3_WITH_STB_SET1 = "tagThreeStbs";

    public static final String CONTEXT_TEST_STB_WITH_EXISTING_TAG1 = "bulkTagStb1";
    public static final String CONTEXT_TEST_STB_WITH_EXISTING_TAG2 = "bulkTagStb2";
    public static final String CONTEXT_DELETE_TAG1_STBS = "deleteTag1Stbs";
    public static final String CONTEXT_DELETE_TAG2_STBS = "deleteTag2Stbs";
    public static final String CONTEXT_REST_RESPONSE = "response";
    public static final String CONTEXT_STB_MODEL = "dawgHouseSTBModel";
    public static final String CONTEXT_STB_DEVICE = "dawgHouseSTBDevice";
    public static final String CONTEXT_STB_TAGS = "tagsInAnStb";
    public static final String CONTEXT_DAWG_HOUSE_URL = "dawgHouseurl";
    public static final String CONTEXT_DAWG_SHOW_URL = "dawgShowurl";
    public static final String CONTEXT_DAWG_POUND_URL = "dawgPoundurl";
    public static final String CONTEXT_TEST_STBS = "testStbs";
    public static final String CONTEXT_RESERVATION = "reservationDawgPound";
    public static final String CONTEXT_RESERVATION_TOKEN = "stbReservationToken";
    public static final String CONTEXT_TEST_STB_MODELS = "testStbModels";
    public static final String CONTEXT_TEST_TAGS = "testTags";
    public static final String CONTEXT_TEST_STB_MODEL = "testStbModel";  
    public static final String CONTEXT_NEW_PROPERTY_ADDED = "newCapabilityAdded";
    public static final String CONTEXT_PROPERTY_LIST = "capList";
    public static final String CONTEXT_ALREADY_EXISTING_PROPERTY = "configProperty";
    public static final String CONTEXT_NEW_STB_MODEL = "newModel";
    public static final String CONTEXT_MODEL_PROPERTIES = "newCapability";
    public static final String CONTEXT_ALERT = "alert";
    public static final String CONTEXT_FILTER_CONDITION = "filterCondition";
    public static final String CONTEXT_EXPECTED_CONDITION = "expectedCondition";
    public static final String CONTEXT_FILTER_COUNT = "filterCount";   
    public static final String CONTEXT_TEST_STB_ADVACE_FILTER = "advaceFilterStbId";
    public static final String CONTEXT_TEST_STB_WIHTOUT_TAG = "bulkTagSTB";
    public static final String CONTEXT_CONDTN_BTNS = "condtnBtns";
    public static final String CONTEXT_FILTER_TO_DELETE = "filterToDelete";
    public static final String CONTEXT_FILTER_TO_NOT_DELETE = "filterToNotDelete";

    public static final String CONTEXT_STB_LIST_IN_FILTER_TABLE = "stbListsInFilterTable";
    public static final String CONTEXT_TAG_ADDED = "newTagAdded";
    public static final String CONTEXT_STB_COUNT = "stbCount";
    public static final String CONTEXT_STB_SELECTED = "stbContentselected";
    public static final String CONTEXT_TAG_SELECTED = "tagSelected";
    public static final String CONTEXT_SEARCH_HISTORY = "searchHistory";
    public static final String CONTEXT_SEARCH_HISTORY_ENTRY = "searchHistoryEntry";
    public static final String CONTEXT_TAG_WITH_STBS = "stbWithTags";

    public static final String CONTEXT_TEST_STB_FOR_EDIT_DEVICE = "stbForEditDevice";
    public static final String CONTEXT_STB_PROPERTIES = "stbProperties";
    public static final String CONTEXT_BTN_SELECTED = "btnSelected";   
    public static final String CONTEXT_DEVICE_PROPERTY_SELECTED = "StbDeviceProperty";
    public static final String CONTEXT_EDIT_DEVICE_ROW_COUNT = "editDeviceRowCount";

    /**
     * Defines various constants used in dawg-house automation
     */
    public static final String INVALID_LOGIN_MSG = "Invalid credentials";
    public static final String INVALID_USERNAME = "$$$$$";
    public static final String INVALID_PASSWORD = "password";
    public static final String INVALID_STB_DEVICE_ID = "xyz";
    public static final String STB_MAKE = "newMake";
    public static final String VALID_CLIENT_TOKEN = "testuser";
    public static final String INVALID_CLIENT_TOKEN = "xyz";
    public static final String CONTENT_TYPE = "application/json";
    public static final String COOKIE = "Cookie";
    public static final String DAWG_COOKIE_PREFIX = "dawt=";
    public static final String DAWG_COOKIE_PATTERN = "\\[name: dawt\\]\\[value: (.+?)\\]";
    public static final String FAMILY = "family";
    public static final String MODEL = "model";
    public static final String MAC = "mac";
    public static final String CAPABILITY = "capability";
    public static final String SIGN_IN_BUTTON = "Sign In";
    public static final String DELETE_MODEL_BUTTON = "delete model";
    public static final String LOGIN = "Login to dawg";
    public static final String ADD_OR_UPDATE_MODEL = "Add or update model";
    public static final String GET_MODEL = "Get model";
    public static final String GET_STB_BY_QUERY = "Get stb by query";
    public static final String ASSIGN_MODEL = "Add or update model";
    public static final String ADD_OR_REMOVE_TAG = "Update tag";
    public static final String ADD_OR_REMOVE_STB = "Add or remove STB";
    public static final String UPDATE_STB_DEVICE = "Update STB";
    public static final String GET_STB_DEVICE_LIST = "get STB device list";
    public static final String REMOVE_STB_BY_QUERY = "remove STB by query";
    public static final String POPULATE_STB = "populate STB";
    public static final String RESERVE_STB = "reserve STB";
    public static final String GET_STB_RESERVATION_LIST = "get STB reservation list";
    public static final String UNRESERVE_STB = "unreserve STB";
    public static final String GET_STB_RESERVATION_DETAIL = "get STB reservation token";
    public static final String IS_STB_RESERVED = "check if STB is reserved";
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
    public static final String BTN_CLOSE = "Close";
    public static final String BTN_SAVE = "Save";
    public static final String BTN_NAME_ADD = "Add";
    public static final String JSON_RES_FIELD_VALUE = "#@#";
    public static final String STB_ID_KEY_VAL_PAIR = "\"id\": \"" + JSON_RES_FIELD_VALUE + "\"";
    public static final String STB_MAC_KEY_VAL_PAIR = "\"macAddress\": \"" + JSON_RES_FIELD_VALUE + "\"";    
    public static final long VALID_EXPIRATION_TIME = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365);
    public static final long INVALID_EXPIRATION_TIME = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365);
    public static final String CHECK = "check";
    public static final String UNCHECK = "unCheck";
    public static final String TAGS = "tags";
    public static final String STBS = "stbs";
    public static final String STB_MODELS = "stbModels";  
    public static final String TEST_STB_NAME = "testStbName";
    public static final String ENTER_KEY = "ENTER";

    /**Array of all the test tags stored in text contexts**/
    public static final String[] TEST_TAGS_STORED_IN_CONTEXT = {CONTEXT_TEST_TAG1, CONTEXT_TEST_TAG2, CONTEXT_TEST_TAG3, CONTEXT_DELETE_TAG1, CONTEXT_DELETE_TAG2, CONTEXT_TEST_EXISTING_TAG1, CONTEXT_TEST_EXISTING_TAG2 };
    /**Array of all the test STBs stored in text contexts**/
    public static final String[] TEST_STBS_STORED_IN_CONTEXT = {CONTEXT_TAG1_WITH_STB_SET1, CONTEXT_TAG2_WITH_STB_SET2, CONTEXT_TAG3_WITH_STB_SET1, CONTEXT_DELETE_TAG1_STBS, CONTEXT_DELETE_TAG2_STBS, CONTEXT_TEST_STB_ADVACE_FILTER, CONTEXT_TEST_STB_WIHTOUT_TAG, CONTEXT_TEST_STB_WITH_EXISTING_TAG1, CONTEXT_TEST_STB_WITH_EXISTING_TAG2, CONTEXT_TEST_STB_FOR_EDIT_DEVICE };
    /**Array of all the test STB Models stored in text contexts**/
    public static final String[] TEST_STB_MODELS_STORED_IN_CONTEXT = {CONTEXT_STB_MODEL };

}
