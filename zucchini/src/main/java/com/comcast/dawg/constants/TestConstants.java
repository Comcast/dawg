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

import com.comcast.dawg.config.TestServerConfig;

/**
 * Test constant class.
 *
 * @author  Pratheesh TK
 */
public class TestConstants {

    /** Count of STBs to be tagged to tag one. */
    public static final int UNIQUE_TAG_ONE_STB_COUNT = 2;

    /** Count of STBs to be tagged to tag two. */
    public static final int UNIQUE_TAG_TWO_STB_COUNT = 3;

    /** Count of STBs to be added to delete tag one. */
    public static final int DELETE_TAG_ONE_STB_COUNT = 3;

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

    /** JSON content type header value. */
    public static final String JSON_CONTENT_TYPE_HEADER_VALUE = "application/json; charset=utf-8";

    /** Content type header. */
    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";

    /** Set-top id prefix. * */
    public static final String STB_ID_PREF = "test";

    /** Tag name prefix. * */
    public static final String TAG_NAME_PREF = "testTag";

    /** Remove operation for tag rest call. */
    public static final String REMOVE_OPERATION_TAG_REST = "remove";

    /** Add operation for tag rest call. */
    public static final String ADD_OPERATION_TAG_REST = "add";

    /** Regular expression for tag name splitting. */
    public static final String TAG_NAME_SPLIT_REGEX = " ";

    /** Rest URI for model configuration. */
    public static final String MODEL_UPDATE_REST_URI = TestServerConfig.getHouse() + "models/";

    /** Tag update rest URI. */
    public static final String TAG_UPDATE_REST_URI = TestServerConfig.getHouse() + "devices/update/tags/";

    /** The user name used for the integration tests. */
    public static final String USER = "integrationuser";
    
    /** Constant for chromeDriverVersion */
    public static final String CHROME_DRIVER_VERSION = "chromeDriverVersion";
    
    /** Constant for version no */
    public static final String VERSION = "2.28";
    
    /**
     * Test STB capability.
     */
    public static enum Capability {

        AVAILABLE_TEST_CAP, NEW_TEST_CAP1, NEW_TEST_CAP2, NEVER_ADD_CAP
    }

    /**
     * Test STB families.
     */
    public static enum Family {

        AVAILABLE_TEST_FAMILY, NEW_TEST_FAMILY1, NEW_TEST_FAMILY2, NEVER_ADD_FAMILY
    }
}
