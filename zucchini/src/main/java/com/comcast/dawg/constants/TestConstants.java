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

import com.google.common.collect.Sets;

/**
 * Test constant class.
 *
 * @author Pratheesh TK
 */
public class TestConstants {

	/** Array of test STB count to add **/
	public static final Integer[] STB_TEST_COUNT = { 1, 2, 3, 4 };

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

	/** Rest end point for add or update model configuration. */
	public static final String ADD_OR_UPDATE_MODEL_URI = "models/";

	/** Rest end point for GET model configuration. */
	public static final String GET_STB_MODEL_URI = "models/?id=";

	/** Rest end point for assign STB models. */
	public static final String ASSIGN_MODELS = "assignmodels";

	/** Rest end point for update tags in tag cloud */
	public static final String TAG_UPDATE_REST_URI = "devices/update/tags/";

	/** Rest end point for add or update an stb to dawg house */
	public static final String ADD_OR_REMOVE_STB_REST_URI = "devices/id/";

	/** Rest end point for model confguration page uri. */
	public static final String MODEL_CONFIG_URI = "modelsConfig/";

	/** Constant for chromeDriverVersion */
	public static final String CHROME_DRIVER_VERSION = "chromeDriverVersion";

	/** Constant for version no */
	public static final String VERSION = "2.28";

	/** Configuration page loading time in seconds. */
	public static final int MODEL_CONFIG_PAGE_LOAD_WAIT = 2;

	/** Model delete completion wait in seconds. */
	public static final int DELETE_MODEL_COMPLETION_WAIT = 2;

	/** Model addition completion timeout in seconds. */
	public static final int ADD_MODEL_COMPLETION_TIMEOUT = 3;

	/** Map for dawg roles */
	public static final HashSet<String> DAWG_ROLES = Sets.newHashSet(
			"ROLE_COLLAR", "ROLE_HOUSE", "ROLE_POUND", "ROLE_SHOW");

	/**
	 * Test STB capability.
	 */
	public static enum Capability {

		AVAILABLE_TEST_CAP, NEW_TEST_CAP1, NEW_TEST_CAP2, NEW_TEST_CAP
	}

	/**
	 * Test STB families.
	 */
	public static enum Family {

		AVAILABLE_TEST_FAMILY, NEW_TEST_FAMILY1, NEW_TEST_FAMILY2, NEW_TEST_FAMILY
	}
}
