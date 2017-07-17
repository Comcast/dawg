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
package com.comcast.dawg.glue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants.Capability;
import com.comcast.dawg.constants.TestConstants.Family;
import com.comcast.dawg.helper.DawgModelPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.utils.DawgStbModelUIUtils;
import com.comcast.zucchini.TestContext;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Glue file for checking Dawg-house's STB model page UI behaviors
 * @author priyanka.sl 
 */
public class DawgStbModelPageGlue {
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgStbModelPageGlue.class);

    /**
     * Verify model overlay displayed or not
     * OnZukeStep:"I am on model overlay page"    
     * @throws DawgTestException 
     */
    @Given("^I am on model overlay$")
    public void verifyModelOverlay() throws DawgTestException {
        DawgModelPageHelper.getInstance().loadModelOverlay();
        //Verify model overlay is displayed 
        Assert.assertTrue(DawgModelPageHelper.getInstance().isModelOverlayDisplayed(),
            "Failed to load add model overlay page");
    }

    /**
     * Verify model properties displayed (Capability list and family name)on the model overlay page
     * OnZukeStep:"I should see (.*) list displayed in model overlay"    
     * @throws DawgTestException 
     */
    @Then("^I should see (.*) list displayed in model overlay$")
    public void verifyCapabilities(String property) throws DawgTestException {
        List<String> propertyList = DawgModelPageHelper.getInstance().getConfigPropertyList(property);
        Assert.assertTrue(!propertyList.isEmpty(), property + "not listed in model overlay page");
        try {
            if (DawgHouseConstants.FAMILY.contains(property)) {
                // Verify one family name get selected in drop down list
                RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
                Select select = new Select(driver.findElementById(DawgHousePageElements.FAMILY_SELECT_ELEMENT_ID));
                Assert.assertTrue(null != select.getFirstSelectedOption().getText(),
                    "Family name is not get selected in model overlay");
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_PROPERTY_LIST, propertyList);

    }

    /**
     * Add a new Family/Capability to model overlay
     * OnZukeStep:"I add a new capability to the model overlay"    
     * @throws DawgTestException 
     */
    @When("^I add a new (.*) name to the model overlay$")
    public void addNewCapability(String propertyName) throws DawgTestException {
        String addProperty = null;
        if (DawgHouseConstants.FAMILY.contains(propertyName)) {
            addProperty = Family.NEW_TEST_FAMILY1.name();
        } else if (DawgHouseConstants.CAPABILITY.contains(propertyName)) {
            addProperty = Capability.NEW_TEST_CAP1.name();
        } else {
            LOGGER.error("Invalid Property {}", propertyName);
        }
        // Verifying the new property chosen to add is not available in the corresponding property list
        List<String> propertyList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_PROPERTY_LIST);
        if (null == propertyList) {
            propertyList = DawgModelPageHelper.getInstance().getConfigPropertyList(propertyName);
        }
        Assert.assertTrue(!propertyList.contains(addProperty),
            "New " + propertyName + " choosen to add is present in the " + propertyName + " list");
        DawgModelPageHelper.getInstance().addCapabilityOrFamily(addProperty, propertyName);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_NEW_PROPERTY_ADDED, addProperty);
    }

    /**
     * Verify the family added displayed on model overlay
     * OnZukeStep:"I should see the family name added to model overlay"    
     * @throws DawgTestException 
     */

    @Then("^the (.*) name added displayed in model overlay$")
    public void verifyFamilyNameAdded(String property) throws DawgTestException {
        List<String> propertyList = DawgModelPageHelper.getInstance().getConfigPropertyList(property);
        Assert.assertTrue(!propertyList.isEmpty(), "Failed to find property " + property + "from model overlay");
        String newPropertyAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_NEW_PROPERTY_ADDED);
        // Validating the new capability/family added available on the model overlay        
        Assert.assertTrue(propertyList.contains(newPropertyAdded),
            "Failed to add new " + property + "  on model overlay page.");
        if (DawgHouseConstants.FAMILY.contains(property)) {
            // Verify family name added get selected in the dropdown list      
            Assert.assertTrue(DawgModelPageHelper.getInstance().isFamilyNameSelected(newPropertyAdded),
                property + " name added not get selected in dropdownlist ");
        } else if (DawgHouseConstants.CAPABILITY.contains(property)) {
            // Verify capability added displayed as checked in model overlay
            Assert.assertTrue(DawgModelPageHelper.getInstance().isCapabilitiesSelected(newPropertyAdded),
                "Added " + property + " not displayed as selected");
        } else {
            throw new DawgTestException("Invalid property " + property);
        }
    }

    /**
     * Re-open the model overlay page
     * OnZukeStep:"I reopen the model overlay page"    
     * @throws DawgTestException 
     */
    @Then("^I reopen the model overlay page$")
    public void reOpenModelOverlay() throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            // Close the model overlay and verify it is closed 
            WebElement closeImgElement = driver.findElementByXPath(DawgHousePageElements.CLOSE_IMAGE_IMG_XPATH);
            Assert.assertTrue(closeImgElement.isDisplayed(), "Failed to find the close img icon in model overlay");
            closeImgElement.click();
            //Verify model overlay closed
            Assert.assertFalse(DawgModelPageHelper.getInstance().isModelOverlayDisplayed(),
                "Failed to close  model overlay page");
            // re-open the overlay
            DawgModelPageHelper.getInstance().loadModelOverlay();
            Assert.assertTrue(DawgModelPageHelper.getInstance().isModelOverlayDisplayed(),
                "Failed to reopen model overlay page");
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Verify family name persists after reopening the model overlay page
     * OnZukeStep:"I should see the family name persists on model overlay page"    
     * @throws DawgTestException 
     */
    @Then("^I should see the (.*) name persists on model overlay page$")
    public void verifyFamilyNameAddedPersists(String property) throws DawgTestException {
        List<String> propertyList = DawgModelPageHelper.getInstance().getConfigPropertyList(property);
        Assert.assertTrue(!propertyList.isEmpty(), "Failed to find property " + property + "from model overlay");
        String newPropertyAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_NEW_PROPERTY_ADDED);
        // Validating the new capability/family added available on the model overlay        
        Assert.assertTrue(propertyList.contains(newPropertyAdded),
            "Failed to add new " + property + "  on model overlay page.");

        if (DawgHouseConstants.FAMILY.contains(property)) {
            // Verify family name added get selected in the dropdown list      
            Assert.assertTrue(DawgModelPageHelper.getInstance().isFamilyNameSelected(newPropertyAdded),
                property + " name added not get selected in dropdownlist ");
        } else if (DawgHouseConstants.CAPABILITY.contains(property)) {
            // Verify the capability lists displayed are not selected
            Assert.assertFalse(DawgModelPageHelper.getInstance().isCapabilitiesSelected(newPropertyAdded),
                "Added " + property + " displayed as selected");
        } else {
            throw new DawgTestException("Invalid property " + property);
        }


    }

    /**
     * An an existing model property to model overlay page
     * OnZukeStep:"I add an already existing family/capability/model name to model overlay page"    
     * @throws DawgTestException 
     */
    @When("^I add an already existing (.*) name to model overlay page$")
    public void addAnExistingConfigProperty(String property) throws DawgTestException {
        List<String> propertyList = DawgModelPageHelper.getInstance().getConfigPropertyList(property);
        Assert.assertTrue(!propertyList.isEmpty(), "Failed to find property " + property + "from model overlay");
        // Get an already existing property
        String alreadyExistingProperty = propertyList.get(0);
        if (DawgHouseConstants.MODEL.contains(property)) {
            // Add a family name and capability for this model            
            String familyName = Family.NEW_TEST_FAMILY3.name();
            String capabilityName = Capability.NEW_TEST_CAP3.name();
            DawgModelPageHelper.getInstance().addModelViaModelOverlay(alreadyExistingProperty, familyName,
                capabilityName);
        } else if (DawgHouseConstants.FAMILY.contains(property) || DawgHouseConstants.CAPABILITY.contains(property)) {
            DawgModelPageHelper.getInstance().addCapabilityOrFamily(alreadyExistingProperty, property);
        } else {
            throw new DawgTestException("Invalid property" + property);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_ALREADY_EXISTING_PROPERTY, alreadyExistingProperty);
    }

    /**
     * verify the alert message while adding an already existing property
     * OnZukeStep:"I should see the alert message (.*) name added"    
     * @throws DawgTestException 
     */

    @Then("^I should see the alert message (.*) name added (.*)$")
    public void verifyAlertMessage(String property, String alertMessage) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String addedProperty = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_ALREADY_EXISTING_PROPERTY);
        Assert.assertTrue(DawgModelPageHelper.getInstance().isAlertPresent(),
            "No alert message displayed while trying to add a duplicate " + property + " entry.");
        Alert alert = driver.switchTo().alert();
        // Validates the alert message text. 
        Assert.assertEquals(alert.getText(), addedProperty + " " + alertMessage.replaceAll("\'", "").trim(),
            "Failed to see the expected alert messsage while adding duplicate property  " + property);
        // Accept the alert message to avoid the 'UnhandledAlertException'
        alert.accept();

    }

    /**
     * Add a new model with new capability and family name
     * OnZukeStep:"I add a new model with capability and family name"    
     * @throws DawgTestException 
     */
    @When("^I add a new model with capability and family name$")
    public void addNewModel() throws DawgTestException {
        String newTestModel = DawgStbModelUIUtils.getInstance().createNewTestModel();
        String newCapability = Capability.NEW_TEST_CAP2.name();
        String newFamily = Family.NEW_TEST_FAMILY2.name();
        DawgModelPageHelper.getInstance().addModelViaModelOverlay(newTestModel, newFamily, newCapability);

        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_NEW_STB_MODEL, newTestModel);
        // keeping the model properties in text context for further validations.
        Map<String, String> modelProperties = new HashMap<String, String>();
        modelProperties.put(DawgHouseConstants.CAPABILITY, newCapability);
        modelProperties.put(DawgHouseConstants.FAMILY, newFamily);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_MODEL_PROPERTIES, modelProperties);
    }

    /**
     * Verify model config page is loaded successfully
     * OnZukeStep:"I should see model page is loaded"    
     * @throws DawgTestException 
     */

    @Then("^I should see model page is loaded$")
    public void verifyModelPageLoaded() throws DawgTestException {
        String modelAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_NEW_STB_MODEL);
        // verify model page is loaded and model overlay is dismissed
        Assert.assertTrue(DawgModelPageHelper.getInstance().isModelPageDisplayed(),
            "Failed to load model configuration page");
        // Verify Model name displayed in model config page
        List<String> modelList = DawgModelPageHelper.getInstance().getConfigPropertyList(DawgHouseConstants.MODEL);
        Assert.assertTrue((!modelList.isEmpty()) && (modelList.contains(modelAdded)),
            String.format("Newly added model(%s)is not displayed in model config page", modelAdded));
    }

    /**
     * Verify the added model properties are available in the model config page
     * OnZukeStep:"I should verify the capabilities and famility name is added to the model page"    
     * @throws DawgTestException 
     */
    @Then("^I should verify model properties added to the model page$")
    public void verifyModelAdded() throws DawgTestException {
        String modelAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_NEW_STB_MODEL);
        Map<String, String> contextModelProperties = TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_MODEL_PROPERTIES);
        // Validates the capabilities/Family names added get reflected against the model added in the UI.
        Map<String, String> modelPropertiesFmUI = DawgModelPageHelper.getInstance().getPropertiesOfStbModel(modelAdded);
        Assert.assertTrue(contextModelProperties.equals(modelPropertiesFmUI),
            "Capabilities/Family names added are not reflected against the model added in the config page ");
    }

    /**
     * Verify alert message while deleting a model from model config page
     * OnZukeStep:"an alert message (.*)should be displayed"    
     * @throws DawgTestException 
     */
    @Then("^an alert message (.*)should be displayed$")
    public void verifyDeleteModel(String message) {
        String modelToRemove = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        // Fail the test if no alert message displayed.  
        Assert.assertTrue(DawgModelPageHelper.getInstance().isAlertPresent(),
            "No alert message displayed on clicking on delete model button.");
        Alert deleteAlert = driver.switchTo().alert();
        // Validates the alert message.
        Assert.assertEquals(deleteAlert.getText(), message.replaceAll("\'", "").trim() + " '" + modelToRemove + "'?",
            "The alert message on deletion of model is different from what expected.");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_ALERT, deleteAlert);
    }

    /**
     * Perform actions in alert message displayed
     * OnZukeStep:"I select(.*) on the alert box"    
     * @throws DawgTestException 
     */

    @Then("^I select (.*) on the alert box$")
    public void selectOkInAlertBox(String button) throws DawgTestException {
        Alert deleteAlert = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_ALERT);
        if ("OK".equals(button.toUpperCase())) {
            // Accepting the alert to proceed with deletion.
            deleteAlert.accept();
        } else {
            throw new DawgTestException("Invalid button " + button);
        }

    }

    /**
     * Verify the selected model removed from model config page
     * OnZukeStep:"the model will be removed from the configuration page"    
     * @throws DawgTestException 
     */
    @Then("^the model will be removed from the configuration page$")
    public void verifyModelRemoved() throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String modelRemoved = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
        SeleniumImgGrabber.addImage();
        List<WebElement> modelListElements = driver.findElementsByXPath(DawgHousePageElements.MODEL_NAME_TD_XPATH);
        boolean isModelRemoved = modelListElements.isEmpty() ? true : !(modelListElements.contains(modelRemoved));

        // Verify model removed from model config page       
        Assert.assertTrue(isModelRemoved,
            String.format("Model(%s)is not deleted from model config page", modelRemoved));
    }

    /**
     * View the edit dialogue box
     * OnZukeStep:"I click on model row to view the edit dialog box"    
     * @throws DawgTestException 
     */
    @When("^I click on model row to view the edit dialog box$")
    public void selectModelRowForEdit() throws DawgTestException {
        String modelName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
        DawgModelPageHelper.getInstance().selectSTBModelFromModelPage(modelName);
        // verify model overlay is displayed
        verifyModelOverlayPage();
        // Get the family and capabilities of selected model and keep in text context for further validation
        Map<String, String> properties = DawgModelPageHelper.getInstance().getPropertiesOfStbModel(modelName);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_MODEL_PROPERTIES, properties);

    }

    /**
     * Verify mode name filed is disabled while trying to edit a model via model overlay page
     * OnZukeStep:"I should see the model name field is disabled"    
     * @throws DawgTestException 
     */
    @Then("^I should see the model name field is disabled$")
    public void modelNameFieldDisabled() throws DawgTestException {
        // Verify model name field is disabled;
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            WebElement modelNameInputElement = driver.findElementByXPath(
                DawgHousePageElements.MODEL_NAME_TEXT_INPUT_XPATH);
            Assert.assertTrue(modelNameInputElement.isDisplayed(), "Model name text field is enabled state");
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Modify all the editable text fields in edit model overlay
     * OnZukeStep:"I modify all the editable text fields in the dialog box"    
     * @throws DawgTestException 
     */

    @Then("^I modify all the editable text fields in the dialog box$")
    public void modifyProperties() throws DawgTestException {
        // Add new capability
        String newCapability = Capability.NEW_TEST_CAP3.name();
        String newFamily = Family.NEW_TEST_FAMILY3.name();
        DawgModelPageHelper.getInstance().editModelProperties(newFamily, newCapability);
        // keeping the model properties in text context for further validations.
        Map<String, String> modelProperties = new HashMap<String, String>();
        modelProperties.put(DawgHouseConstants.CAPABILITY, newCapability);
        modelProperties.put(DawgHouseConstants.FAMILY, newFamily);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_MODEL_PROPERTIES, modelProperties);
    }

    /**
     * Select test model from model config page
     * OnZukeStep:"I choose a model from the model configuration page"    
     * @throws DawgTestException 
     */

    @Given("^I choose a model from the model configuration page$")
    public void selectModel() throws DawgTestException {
        List<String> modelList = DawgModelPageHelper.getInstance().getConfigPropertyList(DawgHouseConstants.MODEL);
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(!modelList.isEmpty(), "Failed to find model from model overlay");
        String modelToSelect = modelList.get(0);
        DawgModelPageHelper.getInstance().selectSTBModelFromModelPage(modelToSelect);
        // Get the family and capabilities of selected model
        Map<String, String> properties = DawgModelPageHelper.getInstance().getPropertiesOfStbModel(modelToSelect);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_MODEL_PROPERTIES, properties);
    }

    /**
     *  Verify model overlay page is displayed
     * OnZukeStep:"model overlay page is displayed"    
     * @throws DawgTestException 
     */
    @Then("^model overlay page is displayed$")
    public void verifyModelOverlayPage() throws DawgTestException {
        //Verify model overlay is displayed 
        Assert.assertTrue(DawgModelPageHelper.getInstance().isModelOverlayDisplayed(),
            "Failed to load add model overlay page");
    }

    /**
     *  Verify the model properties (capability and family)are displayed as selected 
     * OnZukeStep:"I should see model properties are displayed as selected"
     * @throws DawgTestException 
     */
    @Then("^I should see model properties are displayed as selected$")
    public void verifyCapabilitiesChecked() throws DawgTestException {
        // Verify the model properties (capability and family)are displayed as checked      
        Map<String, String> properties = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_MODEL_PROPERTIES);

        // Verify family name is displayed as checked
        String familyName = properties.get(DawgHouseConstants.FAMILY);
        Assert.assertTrue(DawgModelPageHelper.getInstance().isFamilyNameSelected(familyName),
            "Family name" + familyName + "is not displayed as selected in model overlay");
        // Verify capabilities as selected   
        StringBuilder caps = new StringBuilder();
        // cap list from model overlay     
        List<String> capabilitiesInModelPage = Arrays.asList(
            properties.get(DawgHouseConstants.CAPABILITY).split(DawgHouseConstants.SPLIT_REGEX));
        List<String> checkedCapabiities = DawgModelPageHelper.getInstance().selectedCapablities();
        for (String capability : capabilitiesInModelPage) {
            if (!checkedCapabiities.contains(capability)) {
                caps.append(capability).append(",");
            }
        }
        Assert.assertTrue(0 == caps.length(),
            "Capabilities " + caps.toString() + " not displayed as checked in model overlay page");
    }

    /**
     *  Verify the modified fields get reflected in model overlay 
     * OnZukeStep:"I should see the modified fields get reflected in the model page"
     * @throws DawgTestException 
     */

    @Then("^I should see the modified fields get reflected in the model page$")
    public void verifyPropertiesUpdated() throws DawgTestException {
        String modelEdited = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
        Map<String, String> contextModelProperties = TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_MODEL_PROPERTIES);
        // Validates the capabilities/Family names updated get reflected against the model added in the UI.
        Map<String, String> modelProperties = DawgModelPageHelper.getInstance().getPropertiesOfStbModel(modelEdited);

        // cap list from model overlay
        List<String> capabilities = Arrays.asList(
            modelProperties.get(DawgHouseConstants.CAPABILITY).split(DawgHouseConstants.SPLIT_REGEX));

        String familyNameInUI = modelProperties.get(DawgHouseConstants.FAMILY);
        Assert.assertTrue(capabilities.contains(contextModelProperties.get(DawgHouseConstants.CAPABILITY)),
            "Failed to update the capability for model " + modelEdited);
        Assert.assertTrue(familyNameInUI.equals(contextModelProperties.get(DawgHouseConstants.FAMILY)),
            "Failed to update the family name for model " + modelEdited);

    }


}
