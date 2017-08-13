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
    public static final String STB_FILTER_CHECKBOX_XPATH = "//input[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for toggle button element. */
    public static final String TOGGLE_BUTTON_XPATH = "//input[@class='toggle-all-checkbox']";

    /** Xpath for all the listed capabilities in mode overlay page. */
    public static final String CAPBILITY_XPATH = "//div[@id='capList']//*";

    /** Xpath for getting the filter table div element. */
    public static final String FILTERED_TABLE_DIV_ELEMENT_XPATH = "//div[@class='filteredTable']/*";

    /** Class attribute value for tag highlight color. **/
    public static final String TAG_HIGHLIGHT_COLOUR = "rgba(255, 255, 0, 1)";

    /** Beginning char for opacity. */
    public static final char OPACITY_BEGINNING_CHAR = ' ';

    /** Ending char for opacity. */
    public static final char OPACITY_ENDING_CHAR = ';';

    /** Waiting time in second * */
    public static final int DEFAULT_WAIT = 4;

    /** Waiting time in second for toggle all check box selection/deselection. */
    public static final int TOGGLE_ALL_CHECKBOX_WAIT = 1;

    /** The class of the &lt;section&gt; tag that holds the user information. */
    public static final String USER_SECTION_CLASS = "user";

    /** Filtered table device id attribute name. */
    public static final String FILTERED_TABLE_DEVICE_ID_ATTRIBUTE = "data-deviceid";

    /** Tag delete option style attribute name. */
    public static final String TAG_DELETE_OPTION_STYLE_ATTRIBUTE = "style";

    /** Tag delete div element identifier attribute value. */
    public static final String TAG_DELETE_DIV_ELEMENT_IDENTIFIER = "tagDelete";

    /** Bulk check box identifier. */
    public static final String BULK_CHECKBOX_INPUT_IDENTIFIER = ".//input[@class='bulk-checkbox']";

    /** The tag cloud id. */
    public static final String TAG_CLOUD_ID = "tagCloud";

    /** The bulk tag text box id. */
    public static final String BULK_TAG_TEXT_ID = "bulkTagInput";

    /** The bulk tag button id. */
    public static final String BULK_TAG_BTN_ID = "bulkTagButton";

    /** The check box attribute name that uniquely identifies the check box. */
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
    public static final String BTN_ADD = "btnAddCondition";
    public static final String CONDITION_TEXT = "conditionText";
    public static final String MODIFIED_ICON = "modIcon";
    public static final String MODIFIED_ICON_CLASS = "modifiedIcon";
    public static final String NEW_PROP_KEY = "newPropKey";
    public static final String CB_NEW_PROP_SET = "cbNewPropSet";
    public static final String BTN_ADD_PROP = "btnAddProp";
    public static final String SAVE_BTN_CLASS = "btnSave";

    /** Wait time in second for STB checkbox element to be displayed. */
    public static final int STB_CHECKBOX_ELEMENT_WAIT = 5;

    /** Identifier for family TD element. */
    public static final String MODEL_FAMILY_TD_IDENTIFIER = "tModelFamily";

    /** Identifier for capabilities TD element. */
    public static final String MODEL_CAPABILITIES_TD_IDENTIFIER = "tModelCaps";

    /** Identifier for model save button element. */
    public static final String SAVE_MODEL_BUTTON_IDENTIFIER = "bSave";

    /** Family select element. */
    public static final String FAMILY_SELECT_ELEMENT_ID = "modelFamily";

    /** Xpath for model family option elements. */
    public static final String MODEL_FAMILY_OPTIONS_XPATH = "//select[@id='modelFamily']/option";

    /** Xapth for add family button element. */
    public static final String ADD_FAMILY_BUTTON_XPATH = "//input[@class='bAddFam']";

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

    /** Xpath of add capability button. */
    public static final String ADD_CAP_BUTTON_XPATH = "//input[@class='bAddCap']";

    /** Xpath of add capability text input element. */
    public static final String ADD_CAP_XPATH = "//input[@class='addCapInp']";

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

    /** xpath for login form **/
    public static final String LOGIN_FORM_XPATH = "//form[@name='loginForm']";

    /** xpath for invalid login message **/
    public static final String INVALID_LOGIN_MSG_XPATH = "//div[@class='alert alert-danger']";

    /** Model config table **/
    public static final String MODEL_CONFIG_TABLE_ID = "modelsTable";

    /** Link for editing STB properties displayed on right click at index page STB. */
    public static final String EDIT_DEVICE_LINK = "Edit";

    /** Xpath of mine button selected **/
    public static final String MINE_BTN_XPATH = "//button[contains(@class,'btnMine btnSelected')]";

    /** Xpath of history div **/
    public static final String HISTORY_DIV_XPATH = "//ol[@id='searchHistorySelectable']//*";

    public static final String MODIFIED_ICON_XPATH = "//div[contains(@class,'modIcon')]";
    /**
     * Partial Xpath for identifying STB row element from index webpage. Content inside row need to be placed for
     * completion of Xpath.
     */
    public static final String STB_ROW_PAR_XPATH = "//div[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for getting the edit device overlay div element. */
    public static final String EDIT_DEVICE_OVERLAY_XPATH = "//div[contains(@class, 'editDeviceOverlay ui-dialog-content')]";

    /** Xpath for getting the edit device overlay properties row elements. */
    public static final String EDIT_DEVICE_OVERLAY_PROPS_TD_XPATH = "//td[@class='editKeyCell']";

    /** Xpath for getting the close button in device overlay. */
    public static final String CLOSE_BTN_XPATH = "//button[contains(@class,'ui-button-icon-only ui-dialog-titlebar-close')]";

    /** Selected search history filter xpath **/
    public static final String SELECTED_SEARCH_HISTORY_ENTRY = "//li[contains(@class ,'searchSelected')]";

    /** xpath for advance search overlay **/
    public static final String ADVANCE_SEARCH_OVERLAY_XPATH = "//div[contains(@class ,'advSearchOverlay ui-dialog-content')]";

    /** xpath for modified cell in edit device overlay **/
    public static final String MODIFIED_CELL_XPATH = "//td[@class='modifiedCell']/*";
}
