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
package com.comcast.dawg.selenium;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;


/**
 * Enum defined for various button name mapped with corresponding button class name in advanced filter overlay page
 * @author priyanka.sl
 */
public enum FilterPageButtons {

    //@formatter:off
    AND_BUTTON(DawgHouseConstants.BTN_NAME_AND, DawgHousePageElements.BTN_AND),
    OR_BUTTON(DawgHouseConstants.BTN_NAME_OR, DawgHousePageElements.BTN_OR),
    NOT_BUTTON(DawgHouseConstants.BTN_NAME_NOT, DawgHousePageElements.BTN_NOT),
    DELETE_BUTTON(DawgHouseConstants.BTN_NAME_DELETE, DawgHousePageElements.BTN_DEL), 
    BREAK_BUTTON(DawgHouseConstants.BTN_NAME_BREAK, DawgHousePageElements.BTN_BREAK),
    ADD_BUTTON(DawgHouseConstants.BTN_NAME_ADD, DawgHousePageElements.BTN_ADD),
    SEARCH_BUTTON(DawgHouseConstants.BTN_NAME_SEARCH,DawgHousePageElements.BTN_SEARCH);
   //@formatter:on

    private String btnName;

    private String className;

    /**
     * Get the button className for the specified button
     * @param btnName operations associated with various REST URI
     * @return FilterPageButtons
     * @throws DawgTestException
     */
    public static FilterPageButtons getBtnElement(String btnName) throws DawgTestException {
        FilterPageButtons buttonInfo = null;
        for (FilterPageButtons name : FilterPageButtons.values()) {
            if (name.getbuttonName().equals(btnName)) {
                buttonInfo = name;
                break;
            }
        }
        if (null == buttonInfo) {
            throw new DawgTestException(btnName + " not defined in FilterPageButtons");
        }
        return buttonInfo;
    }

    /**
     * Enum constructor
     * 
     * @param btnName button name  
     * @param className button element
     */
    private FilterPageButtons(String btnName, String className) {
        this.btnName = btnName;
        this.className = className;

    }

    /**
     * Method returns button name
     *
     * @return btnName
     */
    public String getbuttonName() {
        return btnName;
    }

    /**
     * Method returns button class 
     *
     * @return className
     */
    public String getBtnClass() {
        return className;
    }
}
