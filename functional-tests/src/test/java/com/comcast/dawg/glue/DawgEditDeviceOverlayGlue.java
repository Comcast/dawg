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

import java.util.List;

import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgEditDevicePageHelper;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class checks edit device overlay features in dawg house 
 * @author jeeson
 */
public class DawgEditDeviceOverlayGlue {

    /**
     * Step definition to select an STB with given properties
     */
    @Given("^I selected one STB device (?:which has properties\\(Name, Model, Capabilities, Controller Ip Address, Family, Hardware Revision, Ir Blaster Type, Make,Power Type, Program, Rack Name, Remote type, Slot Name, Tags\\)|from dawg house)$")
    public void selectSTBRowForEdit() {
        String stbDeviceId = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE);
        Assert.assertTrue(DawgIndexPageHelper.getInstance().selectSTBRowFromIndexPage(stbDeviceId),
            "Failed to select STB with properties");
    }

    /**
     * Step definition to select edit option via right click
     */
    @When("I select edit option on right click")
    public void selectEditSTBOption() {
        String stbDeviceId = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE);
        // Verifying whether edit option is selected or not
        Assert.assertTrue(DawgIndexPageHelper.getInstance().launchEditDeviceOverlay(stbDeviceId),
            "Failed to edit option on right click");
    }

    /**
     * Verify edit device overlay displayed or not
     * OnZukeStep:"edit device overlay is displayed"    
     */
    @Then("^edit device overlay is (displayed|dismissed)?$")
    public void verifyEditDeviceOverlay(String isDisplayed) {
        //Verify edit device overlay is displayed or dismissed
        if ("displayed".equalsIgnoreCase(isDisplayed)) {
            Assert.assertTrue(DawgEditDevicePageHelper.getInstance().isEditDeviceOverlayDisplayed(),
                "Failed to load edit device overlay page");
        } else if ("dismissed".equalsIgnoreCase(isDisplayed)) {
            SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
            Assert.assertFalse(DawgEditDevicePageHelper.getInstance().isEditDeviceOverlayDisplayed(),
                "Failed to dismiss edit device overlay page");
        }
    }

    /**
     * Step definition to verify the presence of STB properties in the overlay
     * @param editDevProps
     *          Default STB properties available
     */
    @And("^I should see following properties in edit device overlay$")
    public void verifyEditDeviceOverlayProps(DataTable editDevProps) {
        StringBuilder stbProps = new StringBuilder();
        List<String> expStbProps = editDevProps.asList(String.class);
        int propListLen = expStbProps.size();
        List<String> actualStbProps = DawgEditDevicePageHelper.getInstance().getEditDeviceOverlayProperties();
        Assert.assertTrue((null != actualStbProps), "Failed to get stb properties from edit device overlay");
        // Verifying the presence of properties in the overlay
        for (String property : expStbProps.subList(1, propListLen)) {
            if (!actualStbProps.contains(property)) {
                stbProps.append(property).append(",");
            }
        }
        Assert.assertTrue((0 == stbProps.toString().trim().length()),
            "Failed to verify the presence of properties: " + stbProps.toString());
    }

    /**
     * Step definition to select button(Save/Close) from edit device overlay
     * @param button
     *          Button to be selected for edit device overlay.
     * @throws DawgTestException
     */
    @And("^I select '(.*)' button in edit device overlay$")
    public void selectBtnFmOverlay(String button) throws DawgTestException {
        Assert.assertTrue(DawgEditDevicePageHelper.getInstance().selectButton(button),
            "Failed to select button " + button);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_BTN_SELECTED, button);
    }

    /**
     * Step definition to select an STB with given properties
     * @param - List of STB properties
     */
    @Given("^I selected one STB device from dawg house which has properties '(.*)'$")
    public void selectOneStbRow(List<String> stbProperties) {
        String stbDeviceId = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE);
        Assert.assertTrue(DawgIndexPageHelper.getInstance().selectSTBRowFromIndexPage(stbDeviceId),
            "Failed to select STB row from filter table");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_PROPERTIES, stbProperties);
    }

    /**
     * Open/ re-open edit device overlay    
     */
    @And("^I (?:opened|reopen) edit device overlay$")
    public void launchEditDeviceOverlay() {
        String stbDeviceId = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_FOR_EDIT_DEVICE);
        //launch edit device overlay
        Assert.assertTrue(DawgIndexPageHelper.getInstance().launchEditDeviceOverlay(stbDeviceId),
            "Failed to launch edit device overlay");
        //Verify overlay launched or not
        Assert.assertTrue(DawgEditDevicePageHelper.getInstance().isEditDeviceOverlayDisplayed(),
            "Edit device overlay is not launched");
        SeleniumImgGrabber.addImage();
    }

    /**
     * Verify STB properties displayed   
     */
    @Then("^I should see the STB properties$")
    public void verifyAllSTBProperties() {
        //Verify the properties available in edit device overlay
        List<String> stbProperties = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_PROPERTIES);
        List<String> stbPropertiesFromUI = DawgEditDevicePageHelper.getInstance().getEditDeviceOverlayProperties();
        Assert.assertTrue(null != stbPropertiesFromUI && stbProperties.containsAll(stbPropertiesFromUI),
            "Failed to find stb properties from edit device overlay");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_PROPERTIES, stbProperties);
    }

    /**
     * Edit STB device property value  
     * @param  propKey
     */
    @When("^I edit the STB device (.*) value$")
    public void editSTBName(String propKey) {
        String testName = DawgHouseConstants.TEST_STB_NAME;
        DawgEditDevicePageHelper.getInstance().enterPropertyValue(propKey, testName);
    }


}
