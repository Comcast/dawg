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
package com.comcast.dawg.house.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.dawg.TestServers;
import com.comcast.dawg.selenium.SeleniumWaiter;

/**
 * Holds element constants for the models.jsp page for dawg-house.
 *
 * @author  Pratheesh
 */
public class ModelPage extends LoadableComponent<ModelPage> {

    /** Identifier for family TD element. */
    private static final String MODEL_FAMILY_TD_IDENTIFIER = "tModelFamily";

    /** Identifier for capabilities TD element. */
    private static final String MODEL_CAPABILITIES_TD_IDENTIFIER = "tModelCaps";

    /** Identifier for model save button element. */
    private static final String SAVE_MODEL_BUTTON_IDENTIFIER = "bSave";

    /** Time out in second for completion of family addition. */
    private static final int FAMILY_ADDITION_TIMEOUT = 2;

    /** Time out in second for capability addition. */
    private static final int CAPABILITY_ADDITION_TIMEOUT = 2;

    /** Family select element. */
    private static final String FAMILY_SELECT_ELEMENT_ID = "modelFamily";

    /** Xpath for model family option elements. */
    private static final String MODEL_FAMILY_OPTIONS_XPATH = "//select[@id='modelFamily']/option";

    /** Xapth for add family button element. */
    private static final String ADD_FAMILY_BUTTON_INPUT_XPATH = "//input[@class='bAddFam']";

    /** Xpath for family text input element. */
    private static final String ADD_FAMILY_TEXT_INPUT_XPATH = "//input[@class='addFamInp']";

    /** Capability div element list. */
    private static final String CAPABILITY_LIST_DIV_ID = "capList";

    /** DIV tag name. */
    private static final String DIV_TAG_NAME = "div";

    /** Xpath of model name text element. */
    private static final String MODEL_NAME_TEXT_INPUT_XPATH = "//input[@class='modelName']";

    /** Xpath of model name TD element. */
    private static final String MODEL_NAME_TD_XPATH = "//td[@class='tModelName']";

    /** Capability input element identifier. */
    private static final String ADD_CAP_INPUT_IDENTIFIER = "addCapInp";

    /** Model dialog box loading wait time in second. */
    private static final int MODEL_DIALOG_BOX_WAIT = 5;

    /** Xpath of add capability button. */
    private static final String ADD_CAP_BUTTON_INPUT_XPATH = "//input[@class='bAddCap']";

    /** Xpath of add capability text input element. */
    private static final String ADD_CAP_TEXT_INPUT_XPATH = "//input[@class='addCapInp']";

    /** Model delete button identifier. */
    private static final String DELETE_BUTTON_TD_IDENTIFIER = "input.bDeleteModel";

    /** Close button element Xpath. */
    private static final String CLOSE_IMAGE_IMG_XPATH = "//img[@class='closeImage']";

    /** Xpath of add model button. */
    private static final String ADD_MODEL_BUTTON_INPUT_XPATH = "//input[@class='bAddModel']";

    /** Replaceable element for attribute values. */
    private static final String REPLACEABLE_ELEMENT = "#@#";

    /**
     * Partial Xpath for identifying the STB model TR element. Model name need to be placed for
     * completion of Xpath.
     */
    private static final String STB_MODEL_TR_PAR_XPATH = "//td[text()='" + REPLACEABLE_ELEMENT + "']/..";

    /** Configuration page loading time in seconds. */
    private static final int MODEL_CONFIG_PAGE_LOAD_WAIT = 5;

    /** Model configuration page URL. */
    private static final String MODEL_CONFIG_URL = TestServers.getHouse() + "modelsConfig/";

    /** Class level logger object. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelPage.class);

    /** Model delete completion wait in seconds. */
    private static final int DELETE_MODEL_COMPLETION_WAIT = 2;

    /** Model addition completion timeout in seconds. */
    private static final int ADD_MODEL_COMPLETION_TIMEOUT = 3;

    /**
     * Partial Xpath for identifying the capability check box element. Capability name to be placed
     * for completion of Xpath.
     */
    private static final String CAPABILITY_CHECK_BOX_PAR_XPATH = "//input[@id='" + REPLACEABLE_ELEMENT + "']";

    /**
     * Partial Xpath for identifying the capability div element. Capability name to be placed for
     * completion of Xpath.
     */
    private static final String CAPABILITY_DIV_PAR_XPATH = "//div[text()='" + REPLACEABLE_ELEMENT + "']";

    /**
     * Partial Xpath for identifying the family option element. Family name need to be placed for
     * completion of Xpath.
     */
    private static final String FAMILY_OPTION_PAR_XPATH = "//option[text()='" + REPLACEABLE_ELEMENT + "']";

    /** Browser specific web driver. */
    private RemoteWebDriver driver = null;

    /** Driver specific wait. */
    private SeleniumWaiter seleniumWaiter = null;

    /**
     * Constructor to initialize browser info.
     *
     * @param  driver  Browser specific web driver.
     */
    public ModelPage(RemoteWebDriver driver) {
        this.driver = driver;
        this.seleniumWaiter = new SeleniumWaiter(driver);
    }

    /**
     * Validate the model configuration page is already loaded. {@inheritDoc}
     */
    @Override
    public void isLoaded() throws Error {

        String url = driver.getCurrentUrl();
        Assert.assertEquals(url, MODEL_CONFIG_URL, "Model configuration page is not loaded.");

        // If URL matches with config page we need to ensure that model dialog box is not loaded
        // on top.
        try {
            WebElement overlayTable = driver.findElementByClassName(ADD_CAP_INPUT_IDENTIFIER);
            Assert.assertFalse(overlayTable.isDisplayed());

        } catch (NoSuchElementException exc) {
            LOGGER.info("Capability element is not present in the page as expected.", exc);
        }
    }

    /**
     * Load the model configuration page. {@inheritDoc}
     */
    @Override
    public void load() {
        driver.get(MODEL_CONFIG_URL);

        // Predefined wait for loading of the model config page.
        SeleniumWaiter.waitTill(MODEL_CONFIG_PAGE_LOAD_WAIT);
    }

    /**
     * Launches the model overlay and fill in all the fields and click on submit button.
     *
     * @param  modelName     Name of the model to be added.
     * @param  family        Family to which box model belongs.
     * @param  capabilities  List of capabilities available for the box.
     */
    public void addModel(String modelName, String family,
            String... capabilities) {
        clickOnAddModelButton();
        fillElementsOnAlreadyLoadedModelOverlay(modelName, family, capabilities);

        // Click on save button to update the changes.
        clickSaveButtonOnAlreadyLoadedModelOverlay();
    }

    /**
     * Fills all the required elements on model overlay.
     *
     * @param  modelName     Name of the model to be added.
     * @param  family        Family to which model belongs.
     * @param  capabilities  Capabilities available for the model.
     */
    public void fillElementsOnAlreadyLoadedModelOverlay(String modelName, String family,
            String... capabilities) {

        typeModelNameOnAlreadyLoadedModelOverlay(modelName);

        fillFamilyAndCapabilitiesOnAlreadyLoadedModelOverlay(family, capabilities);

    }

    /**
     * Fills the passed in family and capability names on the model overlay.
     *
     * @param  family        Family to chosen.
     * @param  capabilities  Capabilities to be added for the box model.
     */
    public void fillFamilyAndCapabilitiesOnAlreadyLoadedModelOverlay(String family, String... capabilities) {
        List<String> capabilityList = getAllCapabilityNamesOnAlreadyLoadedModelOverlay();

        for (String capability : capabilities) {

            // Validates whether the capability is available or not
            if (!capabilityList.contains(capability)) {

                // Capability addition comes with a selection. So no further selection required.
                addCapabilityOnAlreadyLoadedModelOverlay(capability);
            } else {

                // Select the capability check box if already available.
                selectCapabilityOnAlreadyLoadedModelOverlay(capability);
            }
        }

        List<String> familyList = getAllFamilyNamesOnAlreadyLoadedModelOverlay();

        // Validates whether the current requested family is available or not.
        if (!familyList.contains(family)) {

            // Add the family name if it not available on the select option element on the page.
            addFamilyOnAlreadyLoadedModelOverlay(family);
        }

        // Select the family.
        selectFamilyOnAlreadyLoadedModelOverlay(family);
    }

    /**
     * Return the list of capabilities already available on loaded model overlay.
     *
     * @return  the list of capabilities.
     */
    public List<String> getAllCapabilityNamesOnAlreadyLoadedModelOverlay() {
        List<String> capabilityList = new ArrayList<String>();
        WebElement capabilityListDiv = driver.findElement(By.id(CAPABILITY_LIST_DIV_ID));

        for (WebElement divCapability : capabilityListDiv.findElements(By.tagName(DIV_TAG_NAME))) {
            capabilityList.add(divCapability.getText());
        }

        return capabilityList;
    }

    /**
     * Provide the on load status of the model overlay.
     *
     * @return  true if the model dialog box is displayed, false otherwise.
     */
    public boolean isModelOverlayDisplayed() {

        boolean isModelOverlayDisplayed = false;

        try {
            WebElement overlayTable = driver.findElementByClassName(ADD_CAP_INPUT_IDENTIFIER);
            isModelOverlayDisplayed = overlayTable.isDisplayed();
        } catch (NoSuchElementException exc) {
            LOGGER.info("Capability element is not present. So model overlay is not loaded.", exc);
        }

        return isModelOverlayDisplayed;
    }

    /**
     * Enter the capability name on the capability text element.
     *
     * @param  capability  Capability to be entered on text element.
     */
    public void typeCapabilityOnAlreadyLoadedModelOverlay(String capability) {
        driver.findElementByXPath(ADD_CAP_TEXT_INPUT_XPATH).sendKeys(capability);
    }

    /**
     * Type the model name to text input element in model overlay.
     *
     * @param  modelName  Model name to be added.
     */
    public void typeModelNameOnAlreadyLoadedModelOverlay(String modelName) {
        WebElement modelNameTextInput = getModelNameTextInputElement();

        modelNameTextInput.sendKeys(modelName);
    }

    /**
     * Provides the model name text input element on model overlay.
     *
     * @return  model name text input element.
     */
    public WebElement getModelNameTextInputElement() {
        return driver.findElementByXPath(MODEL_NAME_TEXT_INPUT_XPATH);
    }

    /**
     * Return the disable status of the text element.
     *
     * @return  true of the model text field is disabled.
     */
    public boolean isModelNameTextFieldDisabledOnModelOverlay() {
        return getModelNameTextInputElement().isDisplayed();
    }

    /**
     * Selecting an already available capability check box on loaded model overlay.
     *
     * @param  capability  Capability to be selected.
     */
    private void selectCapabilityOnAlreadyLoadedModelOverlay(String capability) {
        WebElement capabilityCheckBox = getCapabilityElementOnAlreadyLoadedModelOverlay(capability);

        if (capabilityCheckBox.isSelected()) {
            capabilityCheckBox.click();
        }
    }

    /**
     * Provides the capability check box element on loaded model overlay.
     *
     * @param   capability  Name of check box element to be returned.
     *
     * @return  Capability element on loaded model overlay.
     */
    private WebElement getCapabilityElementOnAlreadyLoadedModelOverlay(String capability) {
        return driver.findElementByXPath(CAPABILITY_CHECK_BOX_PAR_XPATH.replace(REPLACEABLE_ELEMENT, capability));
    }

    /**
     * Add the new capability to the already loaded model overlay page.
     *
     * @param  capability  Capability that need to be added.
     */
    public void addCapabilityOnAlreadyLoadedModelOverlay(String capability) {

        typeCapabilityOnAlreadyLoadedModelOverlay(capability);

        clickAddCapabilityButtonOnAlreadyLoadedModelOverlay();
        seleniumWaiter.waitForPresence(By.xpath(CAPABILITY_DIV_PAR_XPATH.replace(REPLACEABLE_ELEMENT, capability)),
                CAPABILITY_ADDITION_TIMEOUT);
    }

    /**
     * Clicking on add capability button on model overlay.
     */
    public void clickAddCapabilityButtonOnAlreadyLoadedModelOverlay() {
        driver.findElementByXPath(ADD_CAP_BUTTON_INPUT_XPATH).click();
    }

    /**
     * Select the family option provided.
     *
     * @param  family  Family to be selected.
     */
    private void selectFamilyOnAlreadyLoadedModelOverlay(String family) {

        Select select = new Select(driver.findElementById(FAMILY_SELECT_ELEMENT_ID));
        select.selectByVisibleText(family);
    }

    /**
     * Provides the list of family names available on model overlay page.
     *
     * @return  List of all already listed family names on model dialog box.
     */
    public List<String> getAllFamilyNamesOnAlreadyLoadedModelOverlay() {
        List<String> familyNames = new ArrayList<String>();

        for (WebElement familyOptionElement : getAllFamilyOptionElementsOnAlreadyLoadedModelOverlay()) {
            familyNames.add(familyOptionElement.getText());
        }

        return familyNames;
    }

    /**
     * Get all the family option elements available on the model overlay page.
     *
     * @return  list of all family option elements.
     */
    private List<WebElement> getAllFamilyOptionElementsOnAlreadyLoadedModelOverlay() {
        return driver.findElementsByXPath(MODEL_FAMILY_OPTIONS_XPATH);
    }

    /**
     * Add the new family provided.
     *
     * @param  family  Family to be newly added.
     */
    public void addFamilyOnAlreadyLoadedModelOverlay(String family) {
        typeFamilyOnAlreadyLoadedModelOverlay(family);

        clickAddFamilyButtonOnAlreadyLoadedModelOverlay();
        seleniumWaiter.waitForPresence(By.xpath(FAMILY_OPTION_PAR_XPATH.replace(REPLACEABLE_ELEMENT, family)),
                FAMILY_ADDITION_TIMEOUT);
    }

    /**
     * Enter the family name on family text element.
     *
     * @param  family  Family name to be keyed in.
     */
    public void typeFamilyOnAlreadyLoadedModelOverlay(String family) {
        driver.findElementByXPath(ADD_FAMILY_TEXT_INPUT_XPATH).sendKeys(family);
    }

    /**
     * Click on add family button.
     */
    public void clickAddFamilyButtonOnAlreadyLoadedModelOverlay() {
        driver.findElementByXPath(ADD_FAMILY_BUTTON_INPUT_XPATH).click();
    }

    /**
     * Provide all the model name elements.
     *
     * @return  the list of model name elements.
     */
    private List<WebElement> getAllModelNameElements() {
        return driver.findElementsByXPath(MODEL_NAME_TD_XPATH);
    }

    /**
     * Provides the list of model names available on the model configuration page.
     *
     * @return  list of model names.
     */
    public List<String> getAllModelNames() {
        String modelName = null;
        List<String> modelNames = new ArrayList<String>();

        for (WebElement element : getAllModelNameElements()) {
            modelName = element.getText();
            modelNames.add(modelName);
        }

        return modelNames;
    }

    /**
     * Delete a model name by identifying it through model name..
     *
     * @param  modelName  Name of the model to be deleted.
     */
    public void deleteModel(String modelName) {

        clickOnModelDeleteElement(modelName);

        Alert deleteConfirmationAlert = driver.switchTo().alert();
        deleteConfirmationAlert.accept();
    }

    /**
     * Click on model delete element.
     *
     * @param  modelName  Name of the model to be clicked for deletion.
     */
    public void clickOnModelDeleteElement(String modelName) {
        WebElement trElement = getStbModelTrElement(modelName);
        WebElement deleteButton = trElement.findElement(By.cssSelector(DELETE_BUTTON_TD_IDENTIFIER));
        deleteButton.click();
    }

    /**
     * Provides the capabilities available for the stb model.
     *
     * @param   modelName  Model name.
     *
     * @return  capabilities of the STB model.
     */
    public String getCapabilitiesOfStbModel(String modelName) {
        WebElement modelCapabiliyElement =
            getStbModelTrElement(modelName).findElement(By.className(MODEL_CAPABILITIES_TD_IDENTIFIER));

        return modelCapabiliyElement.getText();
    }

    /**
     * Provides the family of stb model.
     *
     * @param   modelName  Model name.
     *
     * @return  family of the STB model.
     */
    public String getFamilyOfStbModel(String modelName) {
        WebElement modelFamilyElement =
            getStbModelTrElement(modelName).findElement(By.className(MODEL_FAMILY_TD_IDENTIFIER));

        return modelFamilyElement.getText();
    }

    /**
     * Return the row element corresponding to the model name passed.
     *
     * @param   modelName  Name of the model.
     *
     * @return  Row element of STB model.
     */
    private WebElement getStbModelTrElement(String modelName) {
        return driver.findElementByXPath(STB_MODEL_TR_PAR_XPATH.replace(REPLACEABLE_ELEMENT, modelName));
    }

    /**
     * Click on model name row element to launch model edit page.
     *
     * @param  modelName  Model name which need to be edited.
     */
    public void clickOnStbModelRowElement(String modelName) {
        getStbModelTrElement(modelName).click();
        SeleniumWaiter.waitTill(MODEL_DIALOG_BOX_WAIT);
    }

    /**
     * Wait for the completion of model addition on model config page.
     *
     * @param  modelName  Name of the model that gets added.
     */
    public void waitForStbModelTrElementAddition(String modelName) {
        seleniumWaiter.waitForPresence(By.xpath(STB_MODEL_TR_PAR_XPATH.replace(REPLACEABLE_ELEMENT, modelName)),
                ADD_MODEL_COMPLETION_TIMEOUT);
    }

    /**
     * Wait for the model deletion from the page.
     *
     * @param  modelName  Name of the model that is requested for deletion.
     */
    public void waitForStbModelTrElementDeletion(String modelName) {
        SeleniumWaiter.waitTill(DELETE_MODEL_COMPLETION_WAIT);
    }

    /**
     * Clicks on model overlay close image.
     */
    public void clickOnModelOverlayCloseImage() {
        driver.findElementByXPath(CLOSE_IMAGE_IMG_XPATH).click();
    }

    /**
     * Click on add model button to launch add model page.
     */
    public void clickOnAddModelButton() {
        WebElement addModelButton = driver.findElementByXPath(ADD_MODEL_BUTTON_INPUT_XPATH);

        // Clicking on add button.
        addModelButton.click();

        // Wait for the dialog box to get loaded.
        SeleniumWaiter.waitTill(MODEL_DIALOG_BOX_WAIT);
    }

    /**
     * Click save button on model dialog box.
     */
    public void clickSaveButtonOnAlreadyLoadedModelOverlay() {
        driver.findElementByClassName(SAVE_MODEL_BUTTON_IDENTIFIER).click();

        // Wait for the model details to get saved and config page get refreshed.
        SeleniumWaiter.waitTill(MODEL_CONFIG_PAGE_LOAD_WAIT);
    }

}
