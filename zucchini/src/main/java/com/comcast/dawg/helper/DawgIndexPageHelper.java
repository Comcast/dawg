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

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Dawg House toggle all UI behaviors
 * 
 * @author priyanka.sl
 */
public class DawgIndexPageHelper {

    private static Object lock = new Object();
    private static DawgIndexPageHelper indexPageHelper = null;

    /**
     * Creates single instance of indexPageHelper
     * 
     * @return indexPageHelper
     */
    public static DawgIndexPageHelper getInstance() {
        synchronized (lock) {
            if (null == indexPageHelper) {
                indexPageHelper = new DawgIndexPageHelper();
            }
        }
        return indexPageHelper;
    }

    /**
     * Enter login credentials to dawg-house login form
     * 
     * @param- username
     * @param -password
     * @throws DawgTestException
     */
    public void enterLoginCredentials(String username, String password) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            driver.findElement(By.name("username")).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Select the tag name element in the tag cloud 
     * @param tagName
     *            tag name to be selected.
     * @throws DawgTestException
     */
    public void selectTagElement(String tagName) throws DawgTestException {
        try {
            WebElement tagCloudDivElement = getTagCloudTagDivElement(tagName);
            tagCloudDivElement.click();
            SeleniumWaiter.waitTill(DawgHousePageElements.BULK_TAG_CLICK_WAIT);
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Select the toggle all button   
     * @return returns true if toggle all button is selected, false otherwise
     * @throws DawgTestException
     */
    public boolean selectToggleAllButton() throws DawgTestException {
        WebElement toggleBtnelement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            toggleBtnelement = driver.findElementByXPath(DawgHousePageElements.TOGGLE_BUTTON_XPATH);
            toggleBtnelement.click();
            SeleniumWaiter.waitTill(DawgHousePageElements.TOGGLE_ALL_CHECKBOX_WAIT);
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
        return toggleBtnelement.isSelected();
    }

    /**
     * Get tag cloud tag div element corresponding to the tag name passed. 
     * @param tagName           
     * @return web element
     * @throws DawgTestException
     */
    public WebElement getTagCloudTagDivElement(String tagName) throws DawgTestException {
        WebElement divElement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            String tagCloudDivXpath = DawgHousePageElements.TAG_ID_DIV_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, tagName);
            divElement = driver.findElementByXPath(tagCloudDivXpath);
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
        return divElement;
    }

    /**
     * Return the tag delete div element.  
     * @param tagName Name of the tag element.
     * @return delete div element of the tag.
     * @throws DawgTestException 
     */
    public WebElement getTagDeleteDivElement(String tagName) throws DawgTestException {
        WebElement delDivElement = null;
        try {
            WebElement tagDivElement = getTagCloudTagDivElement(tagName);
            delDivElement = tagDivElement.findElement(By.className(DawgHousePageElements.TAG_DELETE_DIV_ELEMENT_IDENTIFIER));

        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
        return delDivElement;
    }

    /**
     * Provides all the list of STB check box element displayed. 
     * @return list of bulk checkbox displayed
     * @throws DawgTestException 
     */
    private List<WebElement> getAllStbCheckboxesInFilteredTable() throws DawgTestException {
        List<WebElement> tblElements = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            WebElement filterTableElement = driver.findElementByXPath(DawgHousePageElements.FILTERED_TABLE_DIV_ELEMENT_XPATH);
            tblElements = filterTableElement.findElements(By.xpath(DawgHousePageElements.BULK_CHECKBOX_INPUT_IDENTIFIER));
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
        return tblElements;
    }

    /**
     * Validate all the STB check box displayed to user is in deselected state. 
     * @return true if all the displayed STB check boxes are checked, false if
     *         any of the displayed STB check box is in checked or no STB
     *         displayed.
     * @throws DawgTestException
     */
    public boolean isAllStbCheckboxesAreDeselected() throws DawgTestException {
        boolean isAllDeselected = true;
        List<WebElement> listOfCheckboxes = getAllStbCheckboxesInFilteredTable();
        // Validation to see devices are displayed.
        if (null != listOfCheckboxes && listOfCheckboxes.size() > 0) {
            for (WebElement checkbox : listOfCheckboxes) {
                // If any of the check box is in selected state return false and
                // break the loop.
                if (checkbox.isSelected()) {
                    isAllDeselected = false;
                    break;
                }
            }
        } else {
            throw new DawgTestException("Failed to find STB checkbox element/s in filter table");

        }
        return isAllDeselected;
    }

    /**
     * Validate all the STB check box displayed to user is in selected state. 
     * @return true if all the displayed STB check boxes are checked, false if
     *         any of the displayed STB check box is not checked or none of the
     *         STB displayed.
     * @throws DawgTestException
     */
    public boolean isAllStbCheckboxesAreSelected() throws DawgTestException {
        boolean isAllSelected = true;
        List<WebElement> listOfCheckboxes = getAllStbCheckboxesInFilteredTable();
        // Validation to see devices are displayed.
        if (null != listOfCheckboxes && listOfCheckboxes.size() > 0) {
            for (WebElement checkbox : listOfCheckboxes) {
                // If any of the check box not selected set false and break the
                // loop.
                if (!checkbox.isSelected()) {
                    isAllSelected = false;
                    break;
                }
            }
        } else {
            throw new DawgTestException("Failed to find STB checkbox element/s in filter table");
        }
        return isAllSelected;
    }
}
