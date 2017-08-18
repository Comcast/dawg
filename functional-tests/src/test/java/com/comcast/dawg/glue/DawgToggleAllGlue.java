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

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Glue file for checking Dawg-house's toggle all UI behaviors
 * @author priyanka.sl 
 */

public class DawgToggleAllGlue {

    /**
     * Select the toggle all checkbox button 
     * OnZukeStep:"I select toggle all checkbox"    
     * @throws DawgTestException 
     */
    @When("^I (.*) toggle all checkbox$")
    public void selectToggleAll(String action) throws DawgTestException {
        boolean isSelected = DawgIndexPageHelper.getInstance().selectToggleAllButton();
        //Verify toggle all check box selected or not
        if ("uncheck".equals(action)) {
            Assert.assertFalse(isSelected, "Toggle all check box is selected");
        } else if ("select".equals(action)) {
            Assert.assertTrue(isSelected, "Toggle all check box not selected");
        } else {
            throw new DawgTestException("Invalid action" + action);
        }
    }

    /**
     * Verify toggle all checkbox button selected
     * OnZukeStep:"I should see all STB checkboxes as checked/unchecked"    
     * @throws DawgTestException 
     */
    @Then("^I should see all STB checkboxes as (.*)$")
    public void verifyCheckBoxSelected(String action) throws DawgTestException {
        //Verify all stb's in the filter table are selected
        SeleniumImgGrabber.addImage();
        if ("checked".equals(action)) {
            Assert.assertTrue(DawgIndexPageHelper.getInstance().isAllStbCheckboxesAreSelected(),
                "All the STB check boxes are not displayed as selected.");
        } else if ("unchecked".equals(action)) {
            Assert.assertTrue(DawgIndexPageHelper.getInstance().isAllStbCheckboxesAreDeselected(),
                "Failed to uncheck all STB check boxes.");
        } else {
            throw new DawgTestException("Invalid action" + action);
        }
    }

    /**
     * Verify toggle all check box as unchecked
     * OnZukeStep:"I should see the toggle all checkbox as unchecked"    
     */
    @Then("^I should see the toggle all checkbox as unchecked$")
    public void verifyToggleUncheck() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement toggleButton = driver.findElementByXPath(DawgHousePageElements.TOGGLE_BUTTON_XPATH);
        SeleniumImgGrabber.addImage();
        Assert.assertFalse(toggleButton.isSelected(), "Toggle all check box is selected");
    }

    /**
     * Verify STB checkboxes of first tag remains checked
     * OnZukeStep:"the STB checkboxes of first tag remains as checked"    
     */
    @Then("^the STB checkboxes of first tag remains as checked$")
    public void verifyStbCheckBoxChecked() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<String> stbsForTag1 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        StringBuilder stbs = new StringBuilder();
        for (String stbId : stbsForTag1) {
            String stbCheckboxXpath = DawgHousePageElements.STB_FILTER_CHECKBOX_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, stbId);
            WebElement checkBoxElement = driver.findElementByXPath(stbCheckboxXpath);
            if (!checkBoxElement.isSelected()) {
                stbs = stbs.append(stbId).append(",");
            }
        }
        Assert.assertTrue((0 == stbs.toString().length()),
            String.format("Check box corresponding to STB(%s) is not checked.", stbs));
    }

    /**
     * Verify the delete option display in tag
     * OnZukeStep:"the delete option of tag should be displayed"
     */
    @Then("^the delete option of tag should be displayed$")
    public void deleteOptionDisplayed() {
        String tag1 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1);
        SeleniumImgGrabber.addImage();
        //Verify delete option appears.      
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(tag1),
            "Failed to verify delete option displayed");
    }
}
