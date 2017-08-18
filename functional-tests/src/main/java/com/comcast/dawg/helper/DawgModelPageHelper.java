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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Model configuration page UI behaviors
 * 
 * @author priyanka.sl
 */
public class DawgModelPageHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DawgModelPageHelper.class);
    private static Object lock = new Object();
    private static DawgModelPageHelper modelPageHelper = null;;

    /**
     * Creates single instance of modelPageHelper
     * 
     * @return modelPageHelper
     */
    public static DawgModelPageHelper getInstance() {
        synchronized (lock) {
            if (null == modelPageHelper) {
                modelPageHelper = new DawgModelPageHelper();
            }
        }
        return modelPageHelper;
    }

    /**
     *  Verify model overlay is displayed over model configuration page
     *  @return true if model overlay displayed, false otherwise
     */

    public boolean isModelOverlayDisplayed() {
        WebElement overlayTable = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            overlayTable = driver.findElementByClassName(DawgHousePageElements.ADD_CAP_INPUT_IDENTIFIER);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify load model overlay displayed", e.getMessage());
        }
        return overlayTable.isDisplayed();
    }

    /**
     * Verify model configuration page  is displayed      
     * @return true if model page is displayed, false otherwise 
     */
    public boolean isModelPageDisplayed() {

        WebElement modelConfigTbl = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            modelConfigTbl = driver.findElementById(DawgHousePageElements.MODEL_CONFIG_TABLE_ID);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to load model page", e.getMessage());
        }
        return modelConfigTbl.isDisplayed() && !isModelOverlayDisplayed();
    }

    /**
     * Load the model overlay page to add various models
     */
    public boolean loadModelOverlay() {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            // Clicking on add button.           
            WebElement modelBtn = driver.findElementByXPath(DawgHousePageElements.ADD_MODEL_BUTTON_INPUT_XPATH);
            if (modelBtn.isDisplayed()) {
                modelBtn.click();
                return true;
            }

        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to load model overlay", e.getMessage());
        }
        return false;
    }

    /**
     * Get the available list of configuration properties(Family/Capability/Model) from model configuration page.
     * @param propertyName need to be listed
     * @return  list of Family/Capability/Model names.
     * @throws DawgTestException 
     */
    public List<String> getConfigPropertyList(String propertyName) throws DawgTestException {
        List<String> propertyList = new ArrayList<String>();
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            List<WebElement> elementList = null;
            if (DawgHouseConstants.FAMILY.contains(propertyName)) {
                elementList = driver.findElementsByXPath(DawgHousePageElements.MODEL_FAMILY_OPTIONS_XPATH);
            } else if (DawgHouseConstants.CAPABILITY.contains(propertyName)) {
                WebElement capabilityListDiv = driver.findElement(By.id(DawgHousePageElements.CAPABILITY_LIST_DIV_ID));
                elementList = capabilityListDiv.findElements(By.tagName(DawgHousePageElements.DIV_TAG_NAME));
            } else if (DawgHouseConstants.MODEL.contains(propertyName)) {
                elementList = driver.findElementsByXPath(DawgHousePageElements.MODEL_NAME_TD_XPATH);
            } else {
                throw new DawgTestException("Invalid Property " + propertyName);
            }
            for (WebElement element : elementList) {
                propertyList.add(element.getText());
            }
            if (propertyList.isEmpty() || null == propertyList) {
                LOGGER.error("Failed to find any {} in model configuration page", propertyName);
                return null;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to inspect Web element :" + e.getMessage());
        }
        return propertyList;
    }

    /**
     * Add the new family/ capability name provided to model overlay   
     * @param  newPropertyToAdd  new family/capability name to be  added.
     * @param property 
     * @throws DawgTestException 
     */
    public void addCapabilityOrFamily(String newPropertyToAdd, String property) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String propertyTextFieldXpath = null;
        String propertyBtnXpath = null;
        try {
            if (DawgHouseConstants.FAMILY.contains(property)) {
                propertyTextFieldXpath = DawgHousePageElements.ADD_FAMILY_TEXT_INPUT_XPATH;
                propertyBtnXpath = DawgHousePageElements.ADD_FAMILY_BUTTON_XPATH;
            } else if (DawgHouseConstants.CAPABILITY.contains(property)) {
                propertyTextFieldXpath = DawgHousePageElements.ADD_CAP_XPATH;
                propertyBtnXpath = DawgHousePageElements.ADD_CAP_BUTTON_XPATH;
            } else {
                throw new DawgTestException("Invalid Property" + property);
            } //Add property to model model overlay
            driver.findElementByXPath(propertyTextFieldXpath).sendKeys(newPropertyToAdd);
            driver.findElementByXPath(propertyBtnXpath).click();
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to add property " + newPropertyToAdd + " to model overlay", e.getMessage());
        }
    }


    /**
     * Add a new model with family name and list of capabilities to model configuration page
     *
     * @param  modelName     Name of the model to be added.
     * @param  family        Family to be add
     * @param  capabilities  Capabilities to be add
     * @throws DawgTestException 
     */
    public void addModelViaModelOverlay(String modelName, String family, String... capabilities) throws DawgTestException {

        if (null == family || null == capabilities) {
            throw new DawgTestException("Family name or capability value should not be null");
        }
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            driver.findElementByXPath(DawgHousePageElements.MODEL_NAME_TEXT_INPUT_XPATH).sendKeys(modelName);
            // Select specified capabilities and family name from model overlay       
            if (!(selectCapabilitiesFromModelOverlay(capabilities) && selectFamilyFromModelOverlay(family))) {
                throw new DawgTestException("Failed to select provided capabilities/Family name from model overlay");
            }
            // Select the save button in model overlay to update the changes        
            driver.findElementByClassName(DawgHousePageElements.SAVE_MODEL_BUTTON_IDENTIFIER).click();
            // Wait for the model details to get saved and config page get refreshed.
            SeleniumWaiter.waitTill(TestConstants.OVERLAY_LOAD_WAIT);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to add properties " + modelName + "," + family + " to model overlay", e.getMessage());
        }
    }

    /**
     * Select specified capabilities from model overlay 
     * @param capabilitiesToSelect capabilities to be select from model overlay
     * @return true if all the specified capabilities are selected, false otherwise
     * @throws DawgTestException 
     */
    public boolean selectCapabilitiesFromModelOverlay(String... capabilitiesToSelect) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        // Get the already existing capabilities from model overlay
        List<String> capabilityList = getConfigPropertyList(DawgHouseConstants.CAPABILITY);
        StringBuilder capabilitiesNotSelectd = new StringBuilder();
        try {
            for (String capabilityToSelect : capabilitiesToSelect) {
                // Validates whether the capability to be select is available in the List 
                if (!capabilityList.contains(capabilityToSelect)) {
                    //if the capabilityToSelect is not available on the list add that capability to model overlay
                    addCapabilityOrFamily(capabilityToSelect, DawgHouseConstants.CAPABILITY);
                }
                WebElement checkBoxElemnt = driver.findElementByXPath("//input[@id='" + capabilityToSelect + "']");
                //Select the check box of capability
                if (!checkBoxElemnt.isSelected()) {
                    checkBoxElemnt.click();
                }
                // Get the capabilities which are not selected
                if (!isCapabilitiesSelected(capabilityToSelect)) {
                    capabilitiesNotSelectd.append(capabilityToSelect).append(",");
                }
            }
            if (0 < capabilitiesNotSelectd.toString().length()) {
                LOGGER.error("Failed to select the capabilities {} from model overlay", capabilitiesNotSelectd);
                return false;
            } else {
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select capabilties from model overlay", e.getMessage());
        }
        return false;
    }

    /**
     * Verify the specified capability is displayed as checked in model overlay page 
     * @param  capabilityToverify  capability to verify
     * @return true if the specified capability is selected, false otherwise     
     */
    public boolean isCapabilitiesSelected(String capabilityToverify) {
        WebElement checkBoxElemnet = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            //Select the check box of capability
            checkBoxElemnet = driver.findElementByXPath("//input[@id='" + capabilityToverify + "']");
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to verify capabilities selected in model overlay", e.getMessage());
        }
        return checkBoxElemnet.isSelected();
    }

    /**
     * Get the list of selected capabilities from model overlay page      
     * @return List of capabilities that are selected    
     */

    public List<String> selectedCapablities() {
        List<String> capabilities = new ArrayList<String>();
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            List<WebElement> ele = driver.findElements(By.xpath(DawgHousePageElements.CAPBILITY_XPATH));
            for (WebElement element : ele) {
                if (!element.getText().isEmpty()) {
                    if (this.isCapabilitiesSelected(element.getText())) {
                        capabilities.add(element.getText());
                    }
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get selected capabilities", e.getMessage());
        }
        return capabilities;
    }

    /**
     * Select specified family from model overlay    
     * @param  FamilyToSelect  FamilyToSelect to be select from model overlay
     * @return true if all the specified FamilyToSelect are selected, false otherwise
     * @throws DawgTestException 
     */
    public boolean selectFamilyFromModelOverlay(String FamilyToSelect) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            Select select = new Select(driver.findElementById(DawgHousePageElements.FAMILY_SELECT_ELEMENT_ID));
            // Get the already existing family list
            List<String> familyList = getConfigPropertyList(DawgHouseConstants.FAMILY);
            // Validates whether the current requested family is available or not.
            if (!familyList.contains(FamilyToSelect)) {
                // Add the family name if it is  not available in the model overlay
                addCapabilityOrFamily(FamilyToSelect, DawgHouseConstants.FAMILY);
            }
            //Select the family option from drop down list  
            select.selectByVisibleText(FamilyToSelect);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get selected family from model overlay", e.getMessage());
        }
        // Verify family name selected
        return isFamilyNameSelected(FamilyToSelect);
    }

    /**
     * Verify family name provided is displayed as selected in model overlay page   
     * @param  familyNameSelected 
     * @return true if family name selected, false otherwise   
     */
    public boolean isFamilyNameSelected(String familyNameSelected) {
        WebElement option = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            Select select = new Select(driver.findElementById(DawgHousePageElements.FAMILY_SELECT_ELEMENT_ID));
            //Verify the family name is get selected in the dropdown list
            option = select.getFirstSelectedOption();
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get verify family selected from model overlay", e.getMessage());
            return false;
        }
        return familyNameSelected.equals(option.getText());
    }

    /**
     * Return the row element corresponding to the model name passed. 
     * @param   modelName  Name of the model.  
     * @return  Row element of STB model.    
     */
    public WebElement getModelRowElement(String modelName) {
        WebElement modelRowElement = null;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            modelRowElement = driver.findElementByXPath(DawgHousePageElements.STB_MODEL_TR_PAR_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, modelName));
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get model row element", e.getMessage());
        }
        return modelRowElement;
    }

    /**
     * Select the specified STB model from model config page
     * @param   modelName  Name of the model  
     */
    public void selectSTBModelFromModelPage(String modelName) {
        try {
            WebElement modelRowElement = getModelRowElement(modelName);
            if (modelRowElement.isDisplayed()) {
                modelRowElement.click();
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select model " + modelName + "from model page", e.getMessage());
        }
    }

    /**
     * Get properties ( family/Capability) from the specified STB model from model config page
     * @param modelName
     * @return Map of family name and capabilities of specified model name    
     */

    public Map<String, String> getPropertiesOfStbModel(String modelName) {
        Map<String, String> getProperties = new HashMap<String, String>();
        try {
            WebElement capabilityElement = getModelRowElement(modelName).findElement(
                By.className(DawgHousePageElements.MODEL_CAPABILITIES_TD_IDENTIFIER));
            getProperties.put(DawgHouseConstants.CAPABILITY,
                capabilityElement.getText().replaceAll("[\\[\\]]", "").trim());
            WebElement familyElement = getModelRowElement(modelName).findElement(
                By.className(DawgHousePageElements.MODEL_FAMILY_TD_IDENTIFIER));
            getProperties.put(DawgHouseConstants.FAMILY, familyElement.getText());
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get " + modelName, e.getMessage());
        }
        return getProperties;
    }

    /**
     * Edit model properties(family name and capabilities) from model configuration page
     *
     * @param  modelName     Name of the model to be added.
     * @param  family        Family to be add
     * @param  capabilities  Capabilities to be add
     * @throws DawgTestException 
     */
    public void editModelProperties(String family, String... capabilities) throws DawgTestException {
        if (null == family || null == capabilities) {
            throw new DawgTestException("Family name or capability value provided to edit a new model is null");
        }
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        // Select specified capabilities and family name from model overlay       
        if (!selectCapabilitiesFromModelOverlay(capabilities) || !selectFamilyFromModelOverlay(family)) {
            throw new DawgTestException("Failed to select provided capabilities/Family name from model overlay");
        }
        try {
            // Select the save button in model overlay to update the changes        
            driver.findElementByClassName(DawgHousePageElements.SAVE_MODEL_BUTTON_IDENTIFIER).click();
            // Wait for the model details to get saved and config page get refreshed.
            SeleniumWaiter.waitTill(TestConstants.MODEL_CONFIG_PAGE_LOAD_WAIT);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to edit model properties " + family + "," + capabilities, e.getMessage());

        }
    }
}
