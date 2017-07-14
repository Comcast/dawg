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
package com.comcast.dawg.constants;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Sets;

/**
 * Test constant class.
 *
 * @author Pratheesh TK
 */
public class TestConstants {

    /** Array of test STB count to add **/
    public static final Integer[] STB_TEST_COUNT = {1, 2, 3, 4 };

    /** Alert message while trying to duplicate family. */
    public static final String FAMILY_EXIST_ALERT_MSG_SUFFIX = " already exists";

    /** Alert message when no device is selected. */
    public static final String NO_DEVICE_SELECTED_ALERT_MSG = "No devices selected";

    /** Alert message while trying to add a duplicate model. */
    public static final String MODEL_EXIST_ALERT_MSG_SUFFIX = " already exists";

    /** Delete alert message suffix. */
    public static final String DELETE_ALERT_MSG_SUFFIX = "'?";

    /** Delete alert message prefix. */
    public static final String DELETE_ALERT_MSG_PREFIX = "Are you sure you want to delete '";

    /** Alert message while trying to duplicate capability. */
    public static final String CAPABILITY_EXIST_ALERT_MSG_SUFFIX = " already exists";

    /** Empty string. */
    public static final String EMPTY_STRING = "";

    /** Model name prefix. */
    public static final String MODEL_NAME_PREF = "testModel";

    /** STB Model capabilties. */
    public static final String INIT_MODEL_CAPS = "CAP";

    /** STB Model family. */
    public static final String INIT_MODEL_FAMILY = "FAMILY";

    /** JSON content type header value. */
    public static final String JSON_CONTENT_TYPE_HEADER_VALUE = "application/json; charset=utf-8";

    /** Url encoded content type header value. */
    public static final String URL_ENCODED_CONTENT_TYPE_HEADER_VALUE = "application/x-www-form-urlencoded";

    /** Set-top id prefix. * */
    public static final String STB_ID_PREF = "test";

    /** Tag name prefix. * */
    public static final String TAG_NAME_PREF = "testTag";

    /** Advanced filter name prefix. * */
    public static final String STB_ID_ADVACED_FILTER = "advfilter";

    /** Remove operation for tag rest call. */
    public static final String REMOVE_OPERATION_TAG_REST = "remove";

    /** Add operation for tag rest call. */
    public static final String ADD_OPERATION_TAG_REST = "add";

    /** Regular expression for tag name splitting. */
    public static final String TAG_NAME_SPLIT_REGEX = " ";

    /** Rest end point for add or update model configuration. */
    public static final String ADD_OR_UPDATE_MODEL_URI = "models/";

    /** Rest end point for login to dawg house. */
    public static final String LOGIN_URI = "login";

    /** Rest end point for GET model configuration. */
    public static final String GET_STB_MODEL_URI = "models?id=";

    /** Rest end point for GET STB by query configuration. */
    public static final String GET_STB_BY_QUERY_URI = "devices/query?q=";

    /** Rest end point for GET STB device list configuration. */
    public static final String GET_STB_DEVICE_LIST_URI = "devices/list*";

    /** Rest end point for assign STB models. */
    public static final String ASSIGN_MODELS = "assignmodels";

    /** Rest end point for update tags in tag cloud */
    public static final String TAG_UPDATE_REST_URI = "devices/update/tags/";

    /** Rest end point for populate STB. */
    public static final String POPULATE_STB_URI = "devices/populate/";

    /** Rest end point for reserve STB in dawg pound. */
    public static final String RESERVE_STB_URI = "reservations/reserve";

    /** Rest end point for add, update and remove an STB to dawg house */
    public static final String ADD_OR_REMOVE_STB_REST_URI = "devices/id/";

    /** Rest end point for remove an STB by query from dawg house */
    public static final String REMOVE_STB_BY_QUERY_URI = "devices/id/";

    /** Rest end point for update STB parameters in dawg house */
    public static final String UPDATE_STB_DEVICE_URI = "devices/update?id=";

    /** Rest end point for model confguration page uri. */
    public static final String MODEL_CONFIG_URI = "modelsConfig/";

    /** Constant for chromeDriverVersion */
    public static final String CHROME_DRIVER_VERSION = "chromeDriverVersion";

    /** Constant for version no */
    public static final String VERSION = "2.28";

    /** Constant which holds dawg house cookie expiration time */
    public static final long DEFAULT_TTL = TimeUnit.HOURS.toMillis(1);

    /** Configuration page loading time in seconds. */
    public static final int MODEL_CONFIG_PAGE_LOAD_WAIT = 2;

    /** Model delete completion wait in seconds. */
    public static final int DELETE_MODEL_COMPLETION_WAIT = 2;

    /** Model overlay wait time in seconds. */
    public static final int OVERLAY_LOAD_WAIT = 3;
    /** STB test model name. */
    public static final String TEST_MODEL = "TestModel";

    /** STB test power name. */
    public static final String TEST_POWER = "TestPower";

    /** STB test Make name. */
    public static final String TEST_MAKE = "TestMake";

    /** STB test Capability name. */
    public static final String TEST_CAPABILITY = "TestCap";

    /** STB test prefix. */
    public static final String TEST = "Test";

    /** Map for dawg roles */
    public static final HashSet<String> DAWG_ROLES = Sets.newHashSet("ROLE_COLLAR", "ROLE_HOUSE", "ROLE_POUND",
        "ROLE_SHOW");

    /**
     * Test STB capability.
     */
    public static enum Capability {
        AVAILABLE_TEST_CAP, NEW_TEST_CAP1, NEW_TEST_CAP2, NEW_TEST_CAP3
    }

    /**
     * Test STB families.
     */
    public static enum Family {
        AVAILABLE_TEST_FAMILY, NEW_TEST_FAMILY1, NEW_TEST_FAMILY2, NEW_TEST_FAMILY3
    }
    
    /** list of valid search filter conditions,**/
    public static final String[] VALID_FILTER_CONDITIONS = {"Model equals " + TEST_MODEL, "Make equals " + TEST_MAKE, "Capabilities equals " + TEST_CAPABILITY, "Capabilities matches " + TEST, "Make matches " + TEST };

    /**
     * Test STB request type.
     */
    public static enum RestReqType {

        DETAILED_STB_DEVICE_PARAM, BRIEF_STB_DEVICE_PARAM
    }
}
