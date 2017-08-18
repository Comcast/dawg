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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Dawg House edit device UI operations
 * 
 * @author priyanka.sl
 */
public class DawgEditDevicePageHelper {

    private static Object lock = new Object();
    private static DawgEditDevicePageHelper editDevicePageHelper = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgEditDevicePageHelper.class);

    /**
     * Creates single instance of editDevicePageHelper    
     * @return editDevicePageHelper
     */
    public static DawgEditDevicePageHelper getInstance() {
        synchronized (lock) {
            if (null == editDevicePageHelper) {
                editDevicePageHelper = new DawgEditDevicePageHelper();
            }
        }
        return editDevicePageHelper;
    }

    /**
     * Verify if edit device overlay is displayed over index page
     * @return boolean
     *          true if edit device overlay displayed, false otherwise     
     */
    public boolean isEditDeviceOverlayDisplayed() {
        WebElement overlayTable = null;
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            overlayTable = driver.findElementByXPath(DawgHousePageElements.EDIT_DEVICE_OVERLAY_XPATH);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to load edit device overlay");
        }
        return overlayTable.isDisplayed();
    }

    /**
     * Get the properties in the edit device overlay page
     * @return List<String>
     *          List of available STB properties in edit device overlay    
     */
    public List<String> getEditDeviceOverlayProperties() {
        List<String> deviceProperties = new ArrayList<String>();
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            List<WebElement> overlayPropWebElems = driver.findElementsByXPath(DawgHousePageElements.EDIT_DEVICE_OVERLAY_PROPS_TD_XPATH);
            for (WebElement webElement : overlayPropWebElems) {
                String propertyText = webElement.getText();
                if (!propertyText.isEmpty()) {
                    deviceProperties.add(propertyText);
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get edit device overlay properties");
        }
        return deviceProperties;
    }


    /**
     * Method to select buttons(Close/Save) in edit device overlay
     * @param button
     *          Edit device overlay button
     * @return boolean
     *          true if edit device overlay button is selected, false otherwise 
     * @throws DawgTestException 
     */
    public boolean selectButton(String button) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            By element = null;
            if (DawgHouseConstants.BTN_CLOSE.equalsIgnoreCase(button)) {
                element = By.xpath(DawgHousePageElements.CLOSE_BTN_XPATH);
            } else if (DawgHouseConstants.BTN_SAVE.equalsIgnoreCase(button)) {
                element = By.className(DawgHousePageElements.SAVE_BTN_CLASS);
            } else {
                throw new DawgTestException("Invalid " + button + " button is passed");
            }            
            // Checking if button displayed or not
            for (WebElement ele : driver.findElements(element)) {
                if (ele.isDisplayed()) {
                    ele.click();
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to inspect " + button + "button in the edit device overlay");
        }
        return false;
    }

    /**
     * Enter property value in corresponding property key specified
     * @param - propKey
     * @param - propVal     
     */
    public void enterPropertyValue(String propKey, String propVal) {
        try {
            WebElement valueInput = this.getPropertyValueElement(propKey);
            valueInput.clear();
            valueInput.sendKeys(propVal);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to enter property value " + propVal + " for given property key" + propKey);
        }
    }

    /**
     * Get STB property value
     * @param - propKey     
     * @return - Stb property value for the given property key   
     */
    public String getSTBPropertyValue(String propKey) {
        String value = "";
        try {
            WebElement element = this.getPropertyValueElement(propKey);
            value = element.getAttribute("value");
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get property value for given property key" + propKey);
        }
        return value;
    }

    /**
     * Delete device property 
     * @param - propKey to be deleted 
     * @return -true if stb property deleted, false otherwise  
     */
    public boolean deleteDeviceProperty(String propKey) {
        try {
            WebElement propElement = DawgEditDevicePageHelper.getInstance().getPropertyRowElement(propKey);
            WebElement deleteBtnEle = propElement.findElement(By.className("btnDelProp"));
            if (deleteBtnEle.isDisplayed()) {
                deleteBtnEle.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get delete button element for the property!" + propKey);
        }
        return false;
    }

    /**
     * Verify modified icon highlighted 
     * @param - propKey to be checked 
     * @return - true if modified icon highlighted, false otherwise  
     */
    public boolean isModifiedIconHighlighted(String propKey) {
        boolean ishighlighted = false;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            List<WebElement> overlayPropWebElems = driver.findElementsByXPath(DawgHousePageElements.EDIT_DEVICE_OVERLAY_PROPS_TD_XPATH);
            for (WebElement webElement : overlayPropWebElems) {
                String propertyText = webElement.getText();
                if (propKey.equalsIgnoreCase(propertyText)) {
                    WebElement iconElement = webElement.findElement(By.xpath(DawgHousePageElements.MODIFIED_CELL_XPATH));
                    String classVal = iconElement.getAttribute("class");
                    Collection<String> classes = Arrays.asList(classVal.split(" "));
                    ishighlighted = classes.contains(DawgHousePageElements.MODIFIED_ICON_CLASS);
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify modified icon highlighted for property key!" + propKey);
        }
        return ishighlighted;
    }

    /**
     * Check or Uncheck modified icon. 
     * @param - propKey to be checked 
     * @operation - Check or Uncheck
     * @return - true if modified icon highlighted, false otherwise  
     */
    public boolean checkOrUncheckModifiedIcon(String propKey, String operation) {
        WebElement propElement = DawgEditDevicePageHelper.getInstance().getPropertyRowElement(propKey);
        boolean isIconHightLighted = isModifiedIconHighlighted(propKey);
        try {
            if (isIconHightLighted && operation.equals(DawgHouseConstants.UNCHECK) || !isIconHightLighted && operation.equals(DawgHouseConstants.CHECK)) {
                WebElement iconElement = propElement.findElement(By.xpath(DawgHousePageElements.MODIFIED_ICON_XPATH));
                if (iconElement.isDisplayed()) {
                    iconElement.click();
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to " + operation + " property key" + propKey);
        }
        return false;
    }

    /**
     * Adds a new property in edit device overlay
     * @param propertyKey The key to be add         
     */
    public void addNewPropertyKey(String propertyKey) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement keyInp = driver.findElement(By.className(DawgHousePageElements.NEW_PROP_KEY));
            keyInp.sendKeys(propertyKey);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to add new property" + propertyKey);
        }
    }

    /**
     * Select check box in edit device overlay
     * @return return true, if check box selected, false otherwise     
     */
    public boolean selectCheckBoxInDeviceOverlay() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement setCbElement = driver.findElement(By.className(DawgHousePageElements.CB_NEW_PROP_SET));
            if (!setCbElement.isSelected()) {
                setCbElement.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select checkbox in edit device overlay");
        }
        return false;
    }

    /**
     * Get edit device overlay row count
     * @return row count     
     */
    public int editDeviceOverlayRowcount() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<WebElement> rows = new ArrayList<WebElement>();
        try {
            rows = driver.findElements(By.className("editRow"));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select checkbox in edit device overlay");
        }
        return rows.size();
    }

    /**
     * Verify modified icon is highlighted, after performing check or uncheck operation
     * @param propertyWithModifiedVal
     * @param action - select/ de-select
     * @return String builder - returns the property keys in which ' modified icon' selected or de-selected    
     */
    public StringBuilder verifyModifyIconHighLighted(Map<String, String> propertyWithModifiedVal, String action) {
        StringBuilder properties = new StringBuilder();
        for (Map.Entry<String, String> entry : propertyWithModifiedVal.entrySet()) {
            String propKey = entry.getKey().toLowerCase();
            if (!isModifiedIconHighlighted(propKey) && action.equals("modified") || isModifiedIconHighlighted(propKey) && action.equals("unmodified")) {
                properties.append(entry.getKey()).append(",");
            }
        }
        return properties;
    }

    /**
     * Verify property row modified or not
     * @return return true, if row modified, false otherwise     
     */
    public boolean isPropertyRowModified() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String classVal = driver.findElement(By.className(DawgHousePageElements.MODIFIED_ICON)).getAttribute("class");
        Collection<String> classes = Arrays.asList(classVal.split(" "));
        return classes.contains(DawgHousePageElements.MODIFIED_ICON_CLASS);
    }

    /**
     * Get property value web element for given property key
     * @param - propKey     
     * @return - WebElement for property value 
     */

    private WebElement getPropertyValueElement(String propKey) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//textarea[@name='" + propKey + "']"));
            return element;
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get property value element for given property key" + propKey);
        }
        return element;
    }

    /**
     * Get property row web element for given property key
     * @param - propKey     
     * @return - WebElement for property row 
     */

    private WebElement getPropertyRowElement(String propKey) {
        WebElement row = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            row = driver.findElement(By.xpath(".//tr[@data-key='" + propKey + "']"));
            return row;
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get property row element for given property key" + propKey);
        }
        return row;
    }
}
