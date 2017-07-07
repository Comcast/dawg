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
package com.comcast.dawg.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.selenium.FilterPageButtons;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Dawg house advanced filter UI behaviors
 * 
 * @author priyanka.sl
 */
public class DawgAdvancedFilterPageHelper {
    private static Object lock = new Object();
    private static DawgAdvancedFilterPageHelper filterHelper = null;

    /**
     * Creates single instance of filterHelper
     * 
     * @return filterHelper
     */
    public static DawgAdvancedFilterPageHelper getInstance() {
        synchronized (lock) {
            if (null == filterHelper) {
                filterHelper = new DawgAdvancedFilterPageHelper();
            }
        }
        return filterHelper;
    }

    /**
     * Adds a filter condition to the search list. By putting the given values in the html fields and clicking the add condition button     * 
     * @param field
     *            The field to select in the select list
     * @param operator
     *            The operator to select in the select list
     * @param value
     *            The value to put in the text input field
     * @throws DawgTestException
     */
    public void addFilterCondition(String field, String operator, String value) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            selectFilterCondition(field, operator, value);
            driver.findElementByClassName(DawgHousePageElements.BTN_ADD_CONDITION).click();
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
    }

    /**
     * Select filter conditions by putting the given values
     * 
     * @param field
     *            The field to select in the select list
     * @param operator
     *            The operator to select in the select list
     * @param value
     *            The value to put in the text input field
     * @throws DawgTestException
     */
    public void selectFilterCondition(String field, String operator, String value) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            Select fieldSelect = new Select(driver.findElementByClassName(DawgHousePageElements.FIELD_SELECT));
            fieldSelect.selectByVisibleText(field);
            Select opSelect = new Select(driver.findElementByClassName(DawgHousePageElements.OP_SELECT));
            opSelect.selectByVisibleText(operator);
            WebElement fvInput = driver.findElementByClassName(DawgHousePageElements.FIELD_VALUE_INPUT);
            fvInput.sendKeys(value);
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
    }

    /**
     * Get all the available filter conditions displayed in the filter overlay
     * 
     * @return List of filter conditions
     * @throws DawgTestException
     */
    public List<String> getFilterConditionList() throws DawgTestException {
        List<String> filterConditions = new ArrayList<String>();
        List<WebElement> conditionElements = this.getConditionDivElement();
        try {
            for (WebElement ele : conditionElements) {
                if (!ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT)).getText().isEmpty()) {
                    filterConditions.add(ele.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim());
                }
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return filterConditions;
    }

    /**
     * Get the condition filter div elements
     * 
     * @return List of condition filter elements
     * @throws DawgTestException
     */
    public List<WebElement> getConditionDivElement() throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<WebElement> elementList = null;
        try {
            WebElement divElements = driver.findElementByClassName(DawgHousePageElements.CONDITION_LIST);
            elementList = divElements.findElements(By.tagName("div"));
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return elementList;
    }

    /**
     * Verify the specified button is displayed in advanced filter overlay
     *
     * @param button
     *            to be verify
     * @return true if specified button displayed
     */
    public Boolean verifyButtonDisplay(String button) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement btnElement = null;
        try {
            WebElement conditionControl = driver.findElementByClassName(DawgHousePageElements.CONDITION_CONTROL);
            String btnClass = FilterPageButtons.getBtnElement(button).getBtnClass();
            if (DawgHouseConstants.BTN_NAME_SEARCH.equals(button) || DawgHouseConstants.BTN_NAME_ADD.equals(button)) {
                btnElement = driver.findElement(By.xpath("//input[contains(@class, '" + btnClass + "')]"));
            } else {
                btnElement = conditionControl.findElement(By.className(btnClass));
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return btnElement.isEnabled();
    }

    /**
     * Select specified condition button from filter overlay
     *
     * @param button
     *            to be select
     * @return true if condition button selected, false otherwise
     * @throws DawgTestException
     */
    public boolean selectConditionBtn(String button) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            WebElement conditionControl = driver.findElementByClassName(DawgHousePageElements.CONDITION_CONTROL);
            String btnClass = FilterPageButtons.getBtnElement(button.replaceAll("\'", "")).getBtnClass();
            WebElement btnElement = null;
            if (DawgHouseConstants.BTN_NAME_SEARCH.equals(button) || DawgHouseConstants.BTN_NAME_ADD.equals(button)) {
                btnElement = driver.findElement(By.xpath("//input[contains(@class, '" + btnClass + "')]"));
            } else {
                btnElement = conditionControl.findElement(By.className(btnClass));
            }
            if (btnElement.isDisplayed()) {
                btnElement.click();
                return true;
            }

        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return false;
    }

    /**
     * Verify the specified buttons are selected or not
     *
     * @param List of buttons to be select
     * @return String returns the buttons which are not selected
     * @throws DawgTestException
     */
    public String verifyBtnSelected(List<String> buttons) throws DawgTestException {
        StringBuilder btn = new StringBuilder();
        for (String button : buttons) {
            if (!this.selectConditionBtn(button)) {
                btn.append(button).append(",");
            }
        }
        return btn.toString();
    }

    /**
     * Get all the available filter conditions with checked status in the filter
     * overlay
     * 
     * @return Map storing the filter condition with checked status
     * @throws DawgTestException
     */
    public Map<String, Boolean> getFilterCondnWithCheckedStatus() throws DawgTestException {
        Map<String, Boolean> filterConditions = new HashMap<String, Boolean>();
        List<WebElement> conditionElements = this.getConditionDivElement();
        try {
            for (WebElement ele : conditionElements) {
                if (!ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT)).getText().isEmpty()) {
                    WebElement chckBoxEle = ele.findElement(By.className(DawgHousePageElements.CONDITION_CHECK_BOX));
                    filterConditions.put(ele.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim(),
                        chckBoxEle.isSelected());
                }
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return filterConditions;
    }

    /**
     * Get the STB Ids from the STB filter table
     * 
     * @return List of STB Ids
     * @throws DawgTestException
     */
    private List<String> getStbIdsFromSearchResults() throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<String> deviceIds = new ArrayList<String>();
        try {
            List<WebElement> webelements = driver.findElements(By.xpath(DawgHousePageElements.DEVICE_LIST_XPATH));
            for (WebElement ele : webelements) {
                String deviceId = ele.getAttribute("data-deviceid");
                if (ele.isDisplayed() && null != deviceId) {
                    deviceIds.add(deviceId);
                }
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
        return deviceIds;
    }

    /**
     * Verify the test STB present among the search results displayed or not
     * 
     * @param filterCondtn
     * @param testStbId
     * @throws DawgTestException
     */
    public boolean isResultsDisplayed(String filterCondtn, String testStbId) throws DawgTestException {
        List<String> stbIds = getStbIdsFromSearchResults();
        if (stbIds.isEmpty() || null == stbIds) {
            return false;
        } else { // Check search results by verifying device displayed among
                 // results
            // When NOT condition applied in filter value , verify the test
            // device is not present among the results displayed
            return (filterCondtn.contains("!") && !isDeviceDisplayed(testStbId)) ? true : isDeviceDisplayed(testStbId);
        }
    }

    /**
     * Verify the device displayed based on the search criteria applied
     * 
     * @param deviceId
     *            to find among search results
     * @throws DawgTestException
     */

    private boolean isDeviceDisplayed(String deviceId) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement filteredTable = driver.findElementByClassName(DawgHousePageElements.FILTERED_TABLE);
        try {
            WebElement stbElement = filteredTable.findElement(By.xpath("div[@data-deviceid='" + deviceId + "']"));
            if (stbElement.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException e) {
            // If the element is not available in the UI it will comes to catch
            // block
            return false;
        }
        return false;
    }

    /**
     * Get the expected filter condition
     * 
     * @aram conditionBtn
     * @return addedFilters
     * @throws DawgTestException
     */
    public String getExpectedFilterCondtions(List<String> conditionBtns, List<String> addedFilters) throws DawgTestException {
        StringBuilder expectedCondn = new StringBuilder();
        for (String condition : addedFilters) {
            condition = condition.replaceAll("\\p{P}*", "");
            for (String button : conditionBtns) {
                if (1 == conditionBtns.size() && DawgHouseConstants.BTN_NAME_NOT.equals(button)) {
                    // For single NOT condition
                    expectedCondn.insert(0, "!").append(condition).append(" ").append(button);
                } else if (!DawgHouseConstants.BTN_NAME_NOT.equals(button)) {
                    // For multiple conditions (NOT, AND) or (NOT, OR) and
                    // single conditions AND or OR
                    expectedCondn.append(condition).append(" ").append(button.toLowerCase()).append(" ");
                }
            }
        }
        if (conditionBtns.contains(DawgHouseConstants.BTN_NAME_NOT) && conditionBtns.size() > 1) {
            // Applicable for all conditions expect single NOT condition
            expectedCondn.insert(0, "!");
        }
        String finalCondtn = expectedCondn.toString().replaceAll("\\s+$", "");
        return finalCondtn.substring(0, finalCondtn.lastIndexOf(" "));

    }

    /**
     * Check/Uncheck the specified filter values in the filter overlay   
     * @param filter - Value to check/Uncheck
     * @param operation - Check or uncheck
     * @return Map storing the filter condition with checked status
     * @throws DawgTestException
     */
    public void checkOrUncheckFilterValues(List<String> filter, String operation) throws DawgTestException {
        List<WebElement> conditionElements = this.getConditionDivElement();
        try {
            for (WebElement ele : conditionElements) {
                WebElement element = ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT));
                String conditionText = element.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim();
                if (!conditionText.isEmpty() && filter.contains(conditionText)) {
                    WebElement chckBoxEle = ele.findElement(By.className(DawgHousePageElements.CONDITION_CHECK_BOX));
                    if (chckBoxEle.isSelected() && DawgHouseConstants.UNCHECK_FILTERS.equals(operation) || !chckBoxEle.isSelected() && DawgHouseConstants.CHECK_FILTERS.equals(operation)) {
                        chckBoxEle.click();
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }

    }

}
