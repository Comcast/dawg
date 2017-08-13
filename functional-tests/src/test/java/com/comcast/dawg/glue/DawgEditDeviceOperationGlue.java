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
package com.comcast.dawg.glue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgEditDevicePageHelper;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Verification of edit device UI operations in dawg house 
 * @author priyanka.sl
 **/
public class DawgEditDeviceOperationGlue {

    /**
     * Verify the device properties modified/not modified in edit device overlay page
     * @param propertyKey
     * @param option - modified/not modified     
     */
    @Then("^I should see device (.*) is (.*) in edit device overlay$")
    public void verifyPropertyValueChanged(String propKey, String option) {
        String propValueAdded = DawgHouseConstants.TEST_STB_NAME;
        String propertyValue = DawgEditDevicePageHelper.getInstance().getSTBPropertyValue(propKey);
        boolean isValueModified = propertyValue.equals(propValueAdded) && DawgEditDevicePageHelper.getInstance().isPropertyRowModified();
        if (option.contains("not modified")) {
            Assert.assertFalse(isValueModified, "STB Property " + propKey + " is modified");
        } else if (option.contains("modified")) {
            Assert.assertTrue(isValueModified,
                "Failed to modified the STB device " + propKey + " with value " + propValueAdded);
        }
    }

    /**
     * Edit the property key value 
     * @param propKey
     * @param propVal       
     */
    @When("^I edit the (.*) name to (.*)$")
    public void editSTBProperty(String propKey, String propVal) {
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(propKey, propVal);
    }

    /**
     * Verify the modified STB device properties 
     * @param capability
     * @param capabilityVal  
     * @param family
     * @param familyVal 
     * @param model
     * @param modelVal      
     */
    @Then("^I should see (.*) (.*), (.*) name (.*) and (.*) name (.*) are displayed in model overlay$")
    public void verifyPropertyModified(String capability, String capabilityVal, String family, String familyVal, String model, String modelVal) {
        if ("null".equals(modelVal) || "InvalidModelName".equals(modelVal)) {
            List<String> stbProperties = DawgEditDevicePageHelper.getInstance().getEditDeviceOverlayProperties();
            // if model value is null or "InvalidModelName" then capability and family name value should not appear in model overlay
            Assert.assertFalse(stbProperties.contains(capability) && stbProperties.contains(family),
                "capability, Family are not null");
        } else {
            String capabilityValFmUI = DawgEditDevicePageHelper.getInstance().getSTBPropertyValue(capability);
            capabilityVal = capabilityVal.replaceAll("(?!\\s)\\W", "$0 ");
            Assert.assertEquals(
                capabilityValFmUI,
                capabilityVal,
                "Expected " + capability + " value/s" + capabilityVal + " not found in model overlay. Found" + capabilityValFmUI);
            String familyValFmUI = DawgEditDevicePageHelper.getInstance().getSTBPropertyValue(family);
            Assert.assertEquals(familyValFmUI, familyVal,
                "Expected " + family + " value/s" + familyVal + " not found in model overlay. Found" + familyValFmUI);
        }
        String modelValFmUI = DawgEditDevicePageHelper.getInstance().getSTBPropertyValue(model.toLowerCase());
        Assert.assertEquals(modelValFmUI, modelVal,
            "Expected " + model + " value/s" + modelVal + " not found in model overlay. Found" + modelValFmUI);
    }

    /**
     * Edit the Stb device properties 
     * @param capability
     * @param capabilityVal  
     * @param family
     * @param familyVal 
     * @param model
     * @param modelVa      
     */
    @Given("^I edit STB device properties (.*) (.*), (.*) (.*) and (.*) (.*)$")
    public void modifyProperties(String model, String modelVal, String family, String familyVal, String capability, String capabilityVal) {
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(model, modelVal);
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(family, familyVal);
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(capability, capabilityVal);
    }

    /**
     * Enter device property in the value text field
     * @param keyType       
     */
    @Given("^I entered (?:a|an) (.*) device property in the text field$")
    public void enterDeviceProperty(String keyType) throws DawgTestException {
        String propertyKey = null;
        if ("new".equals(keyType)) {
            propertyKey = "Test Key";
        } else if ("empty".equals(keyType)) {
            propertyKey = " ";
        } else {
            throw new DawgTestException(keyType);
        }
        DawgEditDevicePageHelper.getInstance().addNewPropertyKey(propertyKey);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED, propertyKey);
        int currentRowcount = DawgEditDevicePageHelper.getInstance().editDeviceOverlayRowcount();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EDIT_DEVICE_ROW_COUNT, currentRowcount);
    }

    /**
     * Add new device property by clicking the add button or pressing the 'ENTER' key
     * @param type - choosing add button / pressing 'ENTER' key       
     */
    @Given("^I add the property name by (?:clicking|pressing) the (.*) (?:button|key)$")
    public void clickButton(String type) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement element = null;
        if (DawgHouseConstants.BTN_NAME_ADD.equals(type)) {
            element = driver.findElement(By.className(DawgHousePageElements.BTN_ADD_PROP));
            if (element.isDisplayed()) {
                element.click();
            }
        } else if (DawgHouseConstants.ENTER_KEY.equals(type)) {
            element = driver.findElement(By.className(DawgHousePageElements.NEW_PROP_KEY));
            if (element.isDisplayed()) {
                element.click();
                driver.getKeyboard().pressKey(Keys.ENTER);

            }
        }
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
    }

    /**
     * Verify property name is added/ not added/ removed/not removed/displayed in edit device overlay
     * @param type - added/ not added/ removed/not removed/displayed      
     */
    @Then("^I should see the property name is (.*) (?:in|from) edit device overlay$")
    public void verifyPropertyAdded(String type) throws DawgTestException {
        int initialRowcount = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_EDIT_DEVICE_ROW_COUNT);
        String property = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        List<String> getSTBProperties = DawgEditDevicePageHelper.getInstance().getEditDeviceOverlayProperties();
        int currentRowcount = DawgEditDevicePageHelper.getInstance().editDeviceOverlayRowcount();
        boolean isFound = getSTBProperties.contains(property);
        SeleniumImgGrabber.addImage();
        if ("not added".equals(type)) {
            Assert.assertTrue(!isFound && currentRowcount == initialRowcount,
                "Property key '" + property + "' added in edit device overlay");
        } else if ("displayed".equals(type)) {
            Assert.assertTrue(isFound && currentRowcount == initialRowcount + 1,
                "Newly added property '" + property + "' is not " + type + " in edit device overlay");
        } else if ("removed".equals(type)) {
            Assert.assertTrue(!isFound && currentRowcount == initialRowcount - 1,
                "Property key '" + property + "' is not  " + type + " in edit device overlay");
        } else if ("not removed".equals(type)) {
            Assert.assertTrue(isFound && currentRowcount == initialRowcount,
                "Property key '" + property + "' is  " + type + " in edit device overlay");
        } else {
            throw new DawgTestException("Invalid type" + type);
        }
    }

    /**
     * Select the set checkbox in edit device overlay
     * @throws DawgTestException 
     */
    @And("^I select Set checkbox$")
    public void selectChkBox() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement setCb = driver.findElement(By.className(DawgHousePageElements.CB_NEW_PROP_SET));
        if (!setCb.isSelected()) {
            setCb.click();
        }
    }

    /**
     * Add specified value for the property name
     * @param propertyVal
     */
    @Given("^I add value (.*) for the property name$")
    public void addValueForNewProperty(String propertyVal) {
        String propertyKeyAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(propertyKeyAdded, propertyVal);
    }

    /**
     * Verify property name with specified value displayed in edit device overlay
     * @param valueAdded
     */
    @Then("^I should see property name and corresponding value (.*) displayed in edit device overlay$")
    public void isPropertyDisplayed(String valueAdded) {
        String propertyKey = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        String propertyValueFromUI = DawgEditDevicePageHelper.getInstance().getSTBPropertyValue(propertyKey);
        Assert.assertEquals(
            valueAdded,
            propertyValueFromUI,
            "Failed to view newly added property" + propertyKey + " with value " + valueAdded + "in edit device overlay");
    }

    /**
     * Verify alert message displayed 
     * @param message
     */
    @Then("^an alert pop up with message \"([^\"]*)\" is displayed$")
    public void verifyAlertMessage(String message) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isAlertPresent(), "Alert message is not displayed");
        DawgIndexPageHelper.getInstance().isAlertPresent();
        Alert deleteAlert = driver.switchTo().alert();
        // Validates the alert message.
        Assert.assertTrue(deleteAlert.getText().contains(message),
            "Expected alert message." + message + "is not displayed on the pop up");
    }

    /**
     * Select buttons OK/Cancel in alert pop up displayed 
     * @param button to select 
     */
    @When("^I select '(.*)' button on alert message$")
    public void selectButtonOnAlert(String button) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        Alert deleteAlert = driver.switchTo().alert();
        if ("OK".equals(button)) {
            deleteAlert.accept();
        } else if ("Cancel".equals(button)) {
            deleteAlert.dismiss();
        } else {
            throw new DawgTestException("Invalid button" + button);
        }
    }

    /**
     * Verify alert message dismissed    
     */
    @Then("^I should see the pop up message is dismissed$")
    public void verifyAlertMessageDismissed() {
        Assert.assertFalse(DawgIndexPageHelper.getInstance().isAlertPresent(), "Pop up message is not dismissed");
    }

    /**
     * Select delete button on any STB device property    
     */
    @Given("^I select delete button on any STB device property$")
    public void deleteDeviceProperty() {
        List<String> stbProperties = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_PROPERTIES);
        String propKey = stbProperties.get(0);
        int currentRowcount = DawgEditDevicePageHelper.getInstance().editDeviceOverlayRowcount();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EDIT_DEVICE_ROW_COUNT, currentRowcount);
        DawgEditDevicePageHelper.getInstance().deleteDeviceProperty(propKey.toLowerCase());
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED, propKey);

    }

    /**
     * Select/De-select modified icon of any STB device property
     * @param operation - select/De-select       
     * @throws DawgTestException 
     */
    @Given("^I (.*) modified icon of (?:one|same) device property$")
    public void selectOrDeselectIcon(String operation) throws DawgTestException {
        List<String> stbProperties = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_PROPERTIES);
        String propKey = stbProperties.get(0);
        String selectOperation = null;
        if (operation.equals("selected")) {
            selectOperation = DawgHouseConstants.CHECK;
        } else if (operation.equals("de-select")) {
            selectOperation = DawgHouseConstants.UNCHECK;
        } else {
            throw new DawgTestException("Invalid operation" + operation);
        }
        DawgEditDevicePageHelper.getInstance().checkOrUncheckModifiedIcon(propKey.toLowerCase(), selectOperation);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED, propKey);
    }

    /**
     * Verify the status(highlighted/not highlighted/highlight remains) of modified icon
     * @param option - highlighted/not highlighted
     * @status - remains/ removed       
     * @throws DawgTestException 
     */
    @Then("^(?:I should see modified icon (.*) highlighted|the highlight (.*) (?:on|from) the modified icon)$")
    public void verifyIconHightLighted(String option, String status) throws DawgTestException {
        String propKey = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        if (null != status && status.equals("remains") || null != option && option.contains("displayed")) {
            Assert.assertTrue(DawgEditDevicePageHelper.getInstance().isModifiedIconHighlighted(propKey.toLowerCase()),
                "Modified icon for property key" + propKey + "is not highligted");
        } else if (null != status && status.equals("removed") || null != option && option.contains("not")) {
            Assert.assertFalse(DawgEditDevicePageHelper.getInstance().isModifiedIconHighlighted(propKey.toLowerCase()),
                "Modified icon for property key" + propKey + "is displayed as highligted");
        } else {
            throw new DawgTestException("invalid option" + option);
        }
    }

    /**
     * Modify STB properties based an the count
     * @param count            
     * @throws DawgTestException 
     */
    @Given("^I modified any (\\d+) device properties$")
    public void modifySTBProperties(int count) {
        List<String> stbProperties = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_PROPERTIES);
        Map<String, String> propertyWithModifiedVal = new HashMap<String, String>();
        for (int i = 0; i < count; i++) {
            String propertyKey = stbProperties.get(i);
            String propValue = "test1" + propertyKey;
            DawgEditDevicePageHelper.getInstance().enterPropertyValue(propertyKey.toLowerCase(), propValue);
            propertyWithModifiedVal.put(propertyKey, propValue);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED, propertyWithModifiedVal);
    }

    /**
     * Add an existing device property key   
     */
    @When("^I enter an existing device property in the text field$")
    public void addExistingProperty() {
        List<String> getSTBProperties = DawgEditDevicePageHelper.getInstance().getEditDeviceOverlayProperties();
        //Get the any property key to add
        String propertyKeyToAdd = getSTBProperties.get(0);
        DawgEditDevicePageHelper.getInstance().addNewPropertyKey(propertyKeyToAdd);
        clickButton(DawgHouseConstants.BTN_NAME_ADD);
    }

    /**
     * Verify the edited/selected device properties modified/unmodified
     * @param action -modified/un-modified 
     */
    @Then("^I should see (?:edited|selected) device properties displayed as (.*)$")
    public void verifyPropertiesEdited(String action) {
        //Verify edited device properties are displayed as modified
        Map<String, String> propertyWithModifiedVal = TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        //Verify modified icon highlighted for edited values        
        StringBuilder propsNotEdited = DawgEditDevicePageHelper.getInstance().verifyModifyIconHighLighted(
            propertyWithModifiedVal, action);
        SeleniumImgGrabber.addImage();

        Assert.assertTrue(propsNotEdited.toString().length() == 0,
            "Device properties are not displayed as edited" + propsNotEdited);
    }

    /**
     * De-select the modified icon of selected device properties     
     */
    @Then("^I de-select the modified icon of selected device properties$")
    public void deSelectDevicePropeties() {
        //Get the properties edited
        Map<String, String> propertyWithModifiedVal = TestContext.getCurrent().get(
            DawgHouseConstants.CONTEXT_DEVICE_PROPERTY_SELECTED);
        //De-select the modified icon        
        for (Map.Entry<String, String> entry : propertyWithModifiedVal.entrySet()) {
            DawgEditDevicePageHelper.getInstance().checkOrUncheckModifiedIcon(entry.getKey().toLowerCase(),
                DawgHouseConstants.UNCHECK);
        }
        StringBuilder propsNotEdited = DawgEditDevicePageHelper.getInstance().verifyModifyIconHighLighted(
            propertyWithModifiedVal, "unmodified");
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(propsNotEdited.toString().length() == 0,
            "Modified icon displayed as de-selected for properties " + propsNotEdited);
    }
}
