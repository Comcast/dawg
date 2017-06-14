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

import com.comcast.dawg.config.TestServerConfig;

/**
 * Holds all page element constants for dawg house UI
 *
 * @author  Kevin Pearson
 */
public class DawgHousePageElements {

    /** Replaceable element for attribute values. */
    public static final String REPLACEABLE_ELEMENT = "#@#";

    /** Xpath for tag div element in tag cloud. */
    public static final String TAG_ID_DIV_XPATH = "//div[@tagval='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for tag parent div element in tag cloud. */
    public static final String TAG_ID_PARENT_DIV_XPATH = TAG_ID_DIV_XPATH + "/..";

    /** Xpath for STB filter checkbox row element. */
    public static final String STB_FILTER_DIV_XPATH = "//div[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for STB filter checkbox row element. */
    public static final String STB_FILTER_CHECKBOX_XPATH = "//input[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for toggle button element. */
    public static final String TOGGLE_BUTTON_XPATH = "//input[@class='toggle-all-checkbox']";

    /** Xpath for getting the filter table div element. */
    public static final String FILTERED_TABLE_DIV_ELEMENT_XPATH = "//div[@class='filteredTable']";

    /** Xpath for getting the filtered table successor div elements. */
    public static final String FILTERED_TABLE_SUCCESSOR_DIV_ELEMENTS_XPATH = FILTERED_TABLE_DIV_ELEMENT_XPATH + "/div";

    /** Class attribute value for tag highlight. * */
    public static final String TAG_HIGHLIGHT_CLASS_ATTRIBUTE_VALUE = "tagContainerSelected";

    /** Name of value attribute element. */
    public static final String VALUE_ATTRIBUTE_ELEMENT = "value";

    /** Beginning char for opacity. */
    public static final char OPACITY_BEGINNING_CHAR = ' ';

    /** Ending char for opacity. */
    public static final char OPACITY_ENDING_CHAR = ';';

    /** Time out in second for index page load. * */
    public static final int INDEX_PAGE_LOAD_TIMEOUT = 120;

    /** Waiting time in second for bulk tag select or deselect to happen. * */
    public static final int BULK_TAG_CLICK_WAIT = 4;

    /** Time out in second for tag element to get removed. */
    public static final int TAG_DELETION_TIMEOUT = 10;

    /** Waiting time in second for toggle all check box selection/deselection. */
    public static final int TOGGLE_ALL_CHECKBOX_WAIT = 1;

    /** The class of the &lt;section&gt; tag that holds the user information. */
    public static final String USER_SECTION_CLASS = "user";

    /** The class of the &lt;checkbox&gt; that is used to select for tagging. */
    public static final String BULK_CHECK_BOX = "bulk-checkbox";

    /** Filtered table device id attribute name. */
    public static final String FILTERED_TABLE_DEVICE_ID_ATTRIBUTE = "data-deviceid";

    /** Tag delete option style attribute name. */
    public static final String TAG_DELETE_OPTION_STYLE_ATTRIBUTE = "style";

    /** Tag delete div element identifier attribute value. */
    public static final String TAG_DELETE_DIV_ELEMENT_IDENTIFIER = "tagDelete";

    /** Class attribute name of parent element of the tag div element in the tag cloud. */
    public static final String TAG_CLOUD_PARENT_TAG_DIV_CLASS_ATTRIBUTE = "class";

    /** Tag cloud tag text element identifier attribute value. */
    public static final String TAG_CLOUD_TAG_TEXT_ELEMENT_IDENTIFIER = "tagText";

    /** Bulk check box identifier. */
    public static final String BULK_CHECKBOX_INPUT_IDENTIFIER = ".//input[@class='bulk-checkbox']";

    /** The tag cloud id. */
    public static final String TAG_CLOUD_ID = "tagCloud";

    /** The bulk tag text box id. */
    public static final String BULK_TAG_TEXT_ID = "bulkTagInput";

    /** The bulk tag button id. */
    public static final String BULK_TAG_BTN_ID = "bulkTagButton";

    /** The check box attribute name that uniquely identifies the check box. */
    public static final String DEVICE_ID_ATTR = "data-deviceId";

    public static final String ADV_SEARCH_BUTTON = "advSearchBtn";
    public static final String FIELD_SELECT = "fieldSelect";
    public static final String OP_SELECT = "opSelect";
    public static final String FIELD_VALUE_INPUT = "fieldValueInput";
    public static final String BTN_ADD_CONDITION = "btnAddCondition";
    public static final String CONDITION_LIST = "conditionList";
    public static final String CONDITION_CONTROL = "conditionControl";
    public static final String CONDITION_CHECK_BOX = "cbCondition";
    public static final String BTN_AND = "btnAnd";
    public static final String BTN_OR = "btnOr";
    public static final String BTN_NOT = "btnNot";
    public static final String BTN_DEL = "btnDel";
    public static final String BTN_BREAK = "btnBreak";
    public static final String BTN_SEARCH = "btnSearch";
    public static final String FILTERED_TABLE = "filteredTable";
    public static final String COLLAPSABLE_ROW = "collapsableRow";
    public static final String CONDITION_TEXT = "conditionText";

    /** Wait time in second for STB checkbox selection. */
    public static final int STB_CHECKBOX_SELECTION_WAIT = 2;

    /** Wait time in second for STB checkbox element to be displayed. */
    public static final int STB_CHECKBOX_ELEMENT_WAIT = 8;

    /** Predefined wait in second for ensuring the load of index page. */
    public static final int INDEX_PAGE_LOAD_COMPLETION_WAIT = 5;

    /** Timeout in second for verifying the presence of alert during deletion of tag. */
    public static final int TAG_DELETION_ALERT_PRESENCE_TIMEOUT = 2;

    /** Identifier for family TD element. */
    public static final String MODEL_FAMILY_TD_IDENTIFIER = "tModelFamily";

    /** Identifier for capabilities TD element. */
    public static final String MODEL_CAPABILITIES_TD_IDENTIFIER = "tModelCaps";

    /** Identifier for model save button element. */
    public static final String SAVE_MODEL_BUTTON_IDENTIFIER = "bSave";

    /** Time out in second for completion of family addition. */
    public static final int FAMILY_ADDITION_TIMEOUT = 2;

    /** Time out in second for capability addition. */
    public static final int CAPABILITY_ADDITION_TIMEOUT = 2;

    /** Family select element. */
    public static final String FAMILY_SELECT_ELEMENT_ID = "modelFamily";

    /** Xpath for model family option elements. */
    public static final String MODEL_FAMILY_OPTIONS_XPATH = "//select[@id='modelFamily']/option";

    /** Xapth for add family button element. */
    public static final String ADD_FAMILY_BUTTON_INPUT_XPATH = "//input[@class='bAddFam']";

    /** Xpath for family text input element. */
    public static final String ADD_FAMILY_TEXT_INPUT_XPATH = "//input[@class='addFamInp']";

    /** Capability div element list. */
    public static final String CAPABILITY_LIST_DIV_ID = "capList";

    /** DIV tag name. */
    public static final String DIV_TAG_NAME = "div";

    /** Xpath of model name text element. */
    public static final String MODEL_NAME_TEXT_INPUT_XPATH = "//input[@class='modelName']";

    /** Xpath of model name TD element. */
    public static final String MODEL_NAME_TD_XPATH = "//td[@class='tModelName']";

    /** Capability input element identifier. */
    public static final String ADD_CAP_INPUT_IDENTIFIER = "addCapInp";

    /** Model dialog box loading wait time in second. */
    public static final int MODEL_DIALOG_BOX_WAIT = 5;

    /** Xpath of add capability button. */
    public static final String ADD_CAP_BUTTON_INPUT_XPATH = "//input[@class='bAddCap']";

    /** Xpath of add capability text input element. */
    public static final String ADD_CAP_TEXT_INPUT_XPATH = "//input[@class='addCapInp']";

    /** Model delete button identifier. */
    public static final String DELETE_BUTTON_TD_IDENTIFIER = "input.bDeleteModel";

    /** Close button element Xpath. */
    public static final String CLOSE_IMAGE_IMG_XPATH = "//img[@class='closeImage']";

    /** Xpath of add model button. */
    public static final String ADD_MODEL_BUTTON_INPUT_XPATH = "//input[@class='bAddModel']";
    /**
     * Partial Xpath for identifying the STB model TR element. Model name need to be placed for
     * completion of Xpath.
     */
    public static final String STB_MODEL_TR_PAR_XPATH = "//td[text()='" + REPLACEABLE_ELEMENT + "']/..";

    /** Configuration page loading time in seconds. */
    public static final int MODEL_CONFIG_PAGE_LOAD_WAIT = 5;

    /** Model configuration page URL. */
    public static final String MODEL_CONFIG_URL = TestServerConfig.getHouse() + "modelsConfig/";

    /** Model delete completion wait in seconds. */
    public static final int DELETE_MODEL_COMPLETION_WAIT = 2;

    /** Model addition completion timeout in seconds. */
    public static final int ADD_MODEL_COMPLETION_TIMEOUT = 3;

    /**
     * Partial Xpath for identifying the capability check box element. Capability name to be placed
     * for completion of Xpath.
     */
    public static final String CAPABILITY_CHECK_BOX_PAR_XPATH = "//input[@id='" + REPLACEABLE_ELEMENT + "']";

    /**
     * Partial Xpath for identifying the capability div element. Capability name to be placed for
     * completion of Xpath.
     */
    public static final String CAPABILITY_DIV_PAR_XPATH = "//div[text()='" + REPLACEABLE_ELEMENT + "']";

    /**
     * Partial Xpath for identifying the family option element. Family name need to be placed for
     * completion of Xpath.
     */
    public static final String FAMILY_OPTION_PAR_XPATH = "//option[text()='" + REPLACEABLE_ELEMENT + "']";

    /** Login user. */
    public String userName = null;

    /** The id of the text field that the user inputs the name of the user they want to use to log in to for dawg-house */
    public static final String USER_INPUT_ID = "changeUserInput";
    /** The id of the div that appears when the user inputs an invalid user name */
    public static final String BAD_USER_MSG_ID = "badlogin";
    
    /** xpath for login form **/
    public static final String LOGIN_FORM_XPATH = "//form[@name='loginForm']";
    
    /** xpath for invalid login message **/
    public static final String INVALID_LOGIN_MSG_XPATH = "//div[@class='alert alert-danger']";

}
