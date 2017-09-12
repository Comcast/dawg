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

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgModelPageHelper.class);

    /**
     * Creates single instance of indexPageHelper    
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
     * Verify login page displayed or not
     * @return -boolean returns true if login page displayed successfully, false otherwise
     */
    public boolean isLoginFormDisplayed() {
        WebElement loginFormElement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            driver.get(TestServerConfig.getHouse());
            SeleniumImgGrabber.addImage();
            // Verify login page displayed
            loginFormElement = driver.findElementByXPath(DawgHousePageElements.LOGIN_FORM_XPATH);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify dawg login page displayed!");
        }
        return loginFormElement.isDisplayed();
    }

    /**
     * Enter login credentials to dawg-house login form    
     * @param- username
     * @param -password    
     */
    public void enterLoginCredentials(String username, String password) {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            driver.findElement(By.name("username")).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to enter dawg login credentials in text boxes!");
        }
    }

    /**
     * Select the toggle all button
     * @return returns true if toggle all button is selected, false otherwise     
     */
    public boolean selectToggleAllButton() {
        WebElement toggleButton = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            toggleButton = driver.findElementByXPath(DawgHousePageElements.TOGGLE_BUTTON_XPATH);
            toggleButton.click();
            SeleniumWaiter.waitTill(DawgHousePageElements.TOGGLE_ALL_CHECKBOX_WAIT);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select toggle all button");
        }
        return toggleButton.isSelected();

    }

    /**
     * Get tag cloud tag div element corresponding to the tag name passed. 
     * @param tagName           
     * @return web element     
     */
    public WebElement getTagCloudDivElement(String tagName) {
        WebElement divElement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            String tagCloudDivXpath = DawgHousePageElements.TAG_ID_DIV_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, tagName);
            divElement = driver.findElementByXPath(tagCloudDivXpath);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get tag cloud div element", e.getMessage());
        }
        return divElement;
    }

    /**
     * Return the tag delete div element. 
     * @param tagName Name of the tag element.
     * @return delete div element of the tag.    
     */
    public WebElement getTagDeleteDivElement(String tagName) {
        WebElement element = null;
        try {
            WebElement tagDivElement = getTagCloudDivElement(tagName);
            element = tagDivElement.findElement(By.className(DawgHousePageElements.TAG_DELETE_DIV_ELEMENT_IDENTIFIER));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get tag delete div element", e.getMessage());
        }
        return element;
    }

    /**
     * Is delete option displayed in tag element on tag cloud.
     *
     * @param   tagName  Name of the tag.  
     * @return  true if delete option is available on tag element.    
     */
    public boolean isDeleteOptionDisplayedInTag(String tagName) {
        double opacity = 0;
        try {
            WebElement tagDeleteDivElement = getTagDeleteDivElement(tagName);
            String styleAttributeValue = tagDeleteDivElement.getAttribute(DawgHousePageElements.TAG_DELETE_OPTION_STYLE_ATTRIBUTE);
            if (null != styleAttributeValue) {
                int opacityBeginningIndex = styleAttributeValue.lastIndexOf(DawgHousePageElements.OPACITY_BEGINNING_CHAR) + 1;
                int opacityEndingIndex = styleAttributeValue.lastIndexOf(DawgHousePageElements.OPACITY_ENDING_CHAR);
                String opacityStr = styleAttributeValue.substring(opacityBeginningIndex, opacityEndingIndex);
                opacity = Double.parseDouble(opacityStr);
            }

        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify delete option displayed in tag", e.getMessage());
        }
        return opacity > 0;
    }

    /**
     * Provides all the list of STB check box element displayed.
     * @return list of check boxes displayed    
     */
    private List<WebElement> getAllStbCheckboxesInFilteredTable() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<WebElement> elements = null;
        try {
            WebElement filterTableElement = driver.findElementByXPath(DawgHousePageElements.FILTERED_TABLE_DIV_ELEMENT_XPATH);
            elements = filterTableElement.findElements(By.xpath(DawgHousePageElements.BULK_CHECKBOX_INPUT_IDENTIFIER));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get all STB checkboxes from filter table", e.getMessage());
        }
        return elements;
    }

    /**
     * Validate all the STB check box displayed to user is in deselected state. 
     * @return true if all the displayed STB check boxes are checked, false if
     *         any of the displayed STB check box is in checked or no STB
     *         displayed.   
     */
    public boolean isAllStbCheckboxesAreDeselected() {
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
            LOGGER.error("Failed to find STB checkbox element/s in filter table");
        }
        return isAllDeselected;
    }

    /**

     * Validate all the STB check box displayed to user is in selected state. 
     * @return true if all the displayed STB check boxes are checked, false if
     *         any of the displayed STB check box is not checked or none of the
     *         STB displayed.    
     */
    public boolean isAllStbCheckboxesAreSelected() {
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
            LOGGER.error("Failed to find STB checkbox element/s in filter table");
        }
        return isAllSelected;
    }

    /**
     * Validates whether the alert message pops up. 
     * @return  true if alert is present, false otherwise.
     */
    public boolean isAlertPresent() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            LOGGER.error("No alert Message displayed");
            return false;
        }
    }  
    /**
     * Return the row element corresponding to the STB device id passed. 
     * @param  stbDeviceId 
     *          STB device id.  
     * @return STB row element
     *          row element corresponding to the STB device id passed
     */
    private WebElement getSTBRowElement(String stbDeviceId) {
        WebElement stbRowElement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            stbRowElement = driver.findElementByXPath(DawgHousePageElements.STB_ROW_PAR_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, stbDeviceId));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to inspect row element with content " + stbDeviceId);          
        }
        return stbRowElement;
    }

    /**
     * Select the STB row element with specified STB device id from web page
     * @param  stbDeviceId  
     *          STB device id.
     * @return boolean
     *          true if STB row element is selected, false otherwise 
     */
    public boolean selectSTBRowFromIndexPage(String stbDeviceId) {
        WebElement stbRowElement = getSTBRowElement(stbDeviceId);
        if (null != stbDeviceId && (null != stbRowElement) && stbRowElement.isDisplayed()) {
            stbRowElement.click();
            return true;
        }
        return false;
    }

    /**
     * Launch edit device overlay.
     * @param stbDeviceId
     *          STB device id
     * @return boolean
     *          true if edit device overlay button is selected, false otherwise 
     */
    public boolean launchEditDeviceOverlay(String stbDeviceId) {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            WebElement stbRowElement = getSTBRowElement(stbDeviceId);
            if (null != stbDeviceId && (null != stbRowElement) && stbRowElement.isDisplayed()) {
                // Perform right click on STB row element
                Actions rightClickEdit = new Actions(driver).contextClick(stbRowElement);
                rightClickEdit.build().perform();
                // Selecting link text "edit"
                WebElement editOption = driver.findElement(By.linkText(DawgHousePageElements.EDIT_DEVICE_LINK));
                if (editOption.isDisplayed()) {
                    editOption.click();
                    SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to launch edit device overlay");            
        }
        return false;
    }

    /*
         *@param operation - check/uncheck
         * @return  true if mine button not selected, false otherwise.
         */
    public boolean checkOrUncheckMineBtn(String operation) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement mineBtnElement = driver.findElement(By.xpath(DawgHousePageElements.MINE_BTN_XPATH));
            if (mineBtnElement.isSelected() && DawgHouseConstants.UNCHECK.equals(operation) || !mineBtnElement.isSelected() && DawgHouseConstants.CHECK.equals(operation)) {
                mineBtnElement.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to " + operation + " Mine button");
        }
        return false;
    }

}
