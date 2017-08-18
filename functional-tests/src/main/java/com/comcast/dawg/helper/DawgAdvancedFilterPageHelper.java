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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgAdvancedFilterPageHelper.class);


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
     * Adds a filter condition to the search list. By putting the given values in the html fields and clicking the add condition button
     * @param field
     *            The field to select in the select list
     * @param operator
     *            The operator to select in the select list
     * @param value
     *            The value to put in the text input field    
     */
    public void addFilterCondition(String field, String operator, String value) {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            selectFilterCondition(field, operator, value);
            driver.findElementByClassName(DawgHousePageElements.BTN_ADD_CONDITION).click();
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to add filter condition/s in advanced filter overlay");
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
     */
    public void selectFilterCondition(String field, String operator, String value) {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            Select fieldSelect = new Select(driver.findElementByClassName(DawgHousePageElements.FIELD_SELECT));
            fieldSelect.selectByVisibleText(field);
            Select opSelect = new Select(driver.findElementByClassName(DawgHousePageElements.OP_SELECT));
            opSelect.selectByVisibleText(operator);
            WebElement fvInput = driver.findElementByClassName(DawgHousePageElements.FIELD_VALUE_INPUT);
            fvInput.sendKeys(value);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select filter condition/s from advanced filter overlay");
        }
    }

    /**
     * Get all the available filter conditions displayed in the filter overlay
     * 
     * @return List of filter conditions     
     */
    public List<String> getFilterConditionList() {
        List<String> filterConditions = new ArrayList<String>();
        List<WebElement> conditionElements = this.getConditionDivElement();
        try {
            for (WebElement ele : conditionElements) {
                if (!ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT)).getText().isEmpty()) {
                    filterConditions.add(ele.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim());
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get filter condition list", e.getMessage());
        }
        return filterConditions;
    }

    /**
     * Get the condition filter div elements
     * 
     * @return List of condition filter elements    
     */
    public List<WebElement> getConditionDivElement() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<WebElement> elementList = null;
        try {
            WebElement divElements = driver.findElementByClassName(DawgHousePageElements.CONDITION_LIST);
            elementList = divElements.findElements(By.tagName("div"));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to inspect condition div element:", e.getMessage());
        }
        return elementList;
    }

    /**
     * Verify the specified button is displayed in advanced filter overlay
     *
     * @param button
     *            to be verify
     * @return true if specified button displayed
     * @throws DawgTestException 
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
            LOGGER.error("Failed to find the button {} in advanced filter overlay", button);
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
            LOGGER.error("Failed to select the button {} in advanced filter overlay", button);
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
     */
    public Map<String, Boolean> getFilterCondnWithCheckedStatus() {
        Map<String, Boolean> filterConditions = new HashMap<String, Boolean>();       
        try {
            for (WebElement ele : this.getConditionDivElement()) {
                if (!ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT)).getText().isEmpty()) {
                    WebElement chckBoxEle = ele.findElement(By.className(DawgHousePageElements.CONDITION_CHECK_BOX));
                    filterConditions.put(ele.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim(),
                        chckBoxEle.isSelected());
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get filter condition with checked status", e.getMessage());
        }
        return filterConditions;
    }

    /**
     * Get the STB Ids from the STB filter table
     * 
     * @return List of STB Ids   
     */
    public List<String> getStbIdsFromFilterTable() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<String> deviceIds = new ArrayList<String>();
        try {
            List<WebElement> elements = driver.findElementsByXPath(DawgHousePageElements.FILTERED_TABLE_DIV_ELEMENT_XPATH);
            for (WebElement div : elements) {
                deviceIds.add(div.getAttribute(DawgHousePageElements.FILTERED_TABLE_DEVICE_ID_ATTRIBUTE));
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get STB Ids from filter table:", e.getMessage());
        }
        return deviceIds;
    }

    /**
     * Verify the test STB present among the search results displayed or not
     * 
     * @param filterCondtn
     * @param testStbId   
     */
    public boolean isResultsDisplayed(String filterCondtn, String testStbId) throws DawgTestException {
        List<String> stbIds = getStbIdsFromFilterTable();
        if (null == stbIds || stbIds.isEmpty()) {
            return false;
        } else { // Check search results by verifying device displayed among
                 // results
            // When NOT condition applied in filter value , verify the test
            // device is not present among the results displayed           
            return (null != filterCondtn && filterCondtn.contains("!") && !stbIds.contains(testStbId) ? true : stbIds.contains(testStbId));
        }
    }

    /**
     * Get the expected filter condition
     * 
     * @aram conditionBtn
     * @return addedFilters     
     */
    public String getExpectedFilterCondtions(List<String> conditionBtns, List<String> addedFilters) {
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
     */
    public void checkOrUncheckFilterValues(List<String> filter, String operation) {
        List<WebElement> conditionElements = this.getConditionDivElement();
        try {
            for (WebElement ele : conditionElements) {
                WebElement element = ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT));
                String conditionText = element.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "").trim();
                if (!conditionText.isEmpty() && filter.contains(conditionText)) {
                    WebElement chckBoxEle = ele.findElement(By.className(DawgHousePageElements.CONDITION_CHECK_BOX));
                    if (chckBoxEle.isSelected() && DawgHouseConstants.UNCHECK.equals(operation) || !chckBoxEle.isSelected() && DawgHouseConstants.CHECK.equals(operation)) {
                        chckBoxEle.click();
                    }
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to " + operation + " filter values " + filter);
        }
    }

    /**
     * Verify filter displayed in filter list
     * @param filter - Filter to verify displayed/selected or not  
     * @return true if filter entry displayed/selected , false otherwise   
     */
    public boolean isFilterDisplayed(String filter) {
        Map<String, Boolean> filterConditions = this.getFilterCondnWithCheckedStatus();
        boolean isFound = false;
        for (Map.Entry<String, Boolean> entry : filterConditions.entrySet()) {
            //Verify the expected filter condition matches with the filter value found also verify the checked status
            if (filter.equals(entry.getKey()) && entry.getValue()) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    /**
     * Select Advanced button from dawg home page     
     * @return true if button click successfully, false otherwise   
     */
    public boolean selectAdvancedBtn() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement searchBtnElement = driver.findElementByClassName(DawgHousePageElements.ADV_SEARCH_BUTTON);
            if (searchBtnElement.isDisplayed()) {
                searchBtnElement.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select the advanced button in filter overlay");
        }
        return false;
    }

    /**
     * Add filter values to filter overlay
     * @param filterCountToAdd    
     * @return List of filters added   
     */
    public List<String> addFilterValues(int filterCountToAdd) {
        int filterCountAdded = 0;
        int maxRetry = 0;
        List<String> filters = new ArrayList<String>();
        // Add filter conditions
        do {
            //Randomly pick valid conditions from valid filter condition array
            int random = new Random().nextInt(TestConstants.VALID_FILTER_CONDITIONS.length);
            String filterToAdd = (TestConstants.VALID_FILTER_CONDITIONS[random]);
            List<String> filterList = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
            if (!filterList.contains(filterToAdd)) {
                filters.add(filterToAdd);
                String[] conditions = filterToAdd.split(" ");
                this.addFilterCondition(conditions[0], conditions[1], conditions[2]);
                filterCountAdded++;
                maxRetry++;
            }
            if (filterCountToAdd == filterCountAdded || maxRetry >= TestConstants.VALID_FILTER_CONDITIONS.length * 2) {
                break;
            }
        } while (true);
        return filters;
    }

    /**
     * Get search History list elements   
     * @return List of web elements   
     */
    private List<WebElement> getHistoryElement() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        return driver.findElementsByXPath(DawgHousePageElements.HISTORY_DIV_XPATH);
    }

    /**
     * Get search history list    
     * @return List of search history details    
     */
    public List<String> getSearchHistoryList() {
        List<WebElement> historyElements = getHistoryElement();
        Set<String> historyList = new HashSet<String>();
        try {
            for (WebElement element : historyElements) {
                if (!element.getText().isEmpty()) {
                    historyList.add(element.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, ""));
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get search history list:", e.getMessage());
        }
        return new ArrayList<String>(historyList);
    }

    /**
     * Delete search history from advanced filter overlay
     * @param historyToDelete   
     * @return true if search history deleted , false otherwise   
     */
    public boolean deleteSearchHistory(String historyToDelete) {
        boolean isDeleted = false;
        try {
            List<WebElement> historyElements = getHistoryElement();
            for (WebElement element : historyElements) {
                String historyText = element.getText().trim();
                if (!historyText.isEmpty() && historyText.contains(historyToDelete)) {
                    WebElement deleteBtn = element.findElement(By.className("searchHistoryDelButton"));
                    if (deleteBtn.isDisplayed()) {
                        deleteBtn.click();
                        isDeleted = true;
                        break;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to delete search history:", e.getMessage());
        }
        return isDeleted;
    }

    /**
     * Select an entry from search history
     * @param entryToSelect - Search history entry to be selected   
     * @return true if search history entry selected , false otherwise   
     */
    public boolean selectEntryFmSearchHistory(String entryToSelect) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        boolean isSelected = false;
        try {           
            for (WebElement ele : driver.findElementsByClassName("searchHistoryConditionText")) {
                if (entryToSelect.equals(ele.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, ""))) {
                    ele.click();
                    isSelected = true;
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select entry from search history:", e.getMessage());
        }
        return isSelected;
    }

    /**
     * Verify search history filter highlighted
     * @param filterToCheckSelected - Search history entry to be selected   
     * @return true if search history entry selected , false otherwise    
     */
    public boolean isHistoryFilterHighlighted(String filterToCheck) {
        boolean isSelected = false;
        try {            
            for (WebElement element : getHistoryElement()) {
                String filterEntry = element.getText().replaceAll(DawgHouseConstants.REPLACE_REGEX, "");
                // Verify filter is highlighted
                if (element.findElement(By.xpath(DawgHousePageElements.SELECTED_SEARCH_HISTORY_ENTRY)).isDisplayed() && !filterEntry.isEmpty() && filterEntry.equals(filterToCheck)) {
                    isSelected = true;
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify history filter highligted:", e.getMessage());
        }
        return isSelected;
    }

    /**
     * Verify if advance search overlay is displayed over index page
     * @return boolean
     *          true if advance search overlay displayed, false otherwise    
     */
    public boolean isSearchFilterOverlayDisplayed() {
        WebElement advSearchOverlay = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            advSearchOverlay = driver.findElementByXPath(DawgHousePageElements.ADVANCE_SEARCH_OVERLAY_XPATH);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to inspect advance search device overlay");
        }
        return advSearchOverlay.isDisplayed();
    }
}