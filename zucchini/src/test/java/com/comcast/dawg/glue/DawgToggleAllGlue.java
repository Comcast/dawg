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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Glue file for checking Dawg-house's toggle all UI behaviors
 * @author priyanka.sl 
 */
public class DawgToggleAllGlue {
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgStbModelPageGlue.class);

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
            LOGGER.info("Invalid action" + action);
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
        if ("checked".equals(action)) {
            Assert.assertTrue(DawgIndexPageHelper.getInstance().isAllStbCheckboxesAreSelected(),
                "All the STB check boxes are bot displayed as selected.");
        } else if ("unchecked".equals(action)) {
            Assert.assertTrue(DawgIndexPageHelper.getInstance().isAllStbCheckboxesAreDeselected(),
                "Failed to uncheck all STB check boxes.");
        } else {
            LOGGER.info("Invalid action" + action);
        }
    }

    /**
     * Select any tag element from tag cloud
     * OnZukeStep:"I selected any tag element in tag cloud" 
     * @throws DawgTestException    
     */
    @Given("^I selected any tag element in tag cloud$")
    public void selectTagElement() throws DawgTestException {
        //Get the test tag one added in tag cloud 
        String tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1);
        // Select that test tag one from tag cloud 
        DawgIndexPageHelper.getInstance().selectTagElement(tagName);
    }

    /**
     * Select another test tag added from tag cloud
     * OnZukeStep:"I select another tag element in tag cloud" 
     * @throws DawgTestException  
     */
    @Then("^I select another tag element in tag cloud$")
    public void selectAnotherTag() throws DawgTestException {
        //Get the test tag two added in tag cloud
        String tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG2);
        // select that tag from tag cloud
        DawgIndexPageHelper.getInstance().selectTagElement(tagName);
    }

    /**
     * Verify toggle all check box as unchecked
     * OnZukeStep:"I should see the toggle all checkbox as unchecked"    
     */
    @Then("^I should see the toggle all checkbox as unchecked$")
    public void verifyToggleUncheck() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        WebElement toggleButton = driver.findElementByXPath(DawgHousePageElements.TOGGLE_BUTTON_XPATH);
        Assert.assertFalse(toggleButton.isSelected(), "Toggle all check box is selected");
    }

    /**
     * Verify STB checkboxes of first tag remains checked
     * OnZukeStep:"the STB checkboxes of first tag remains as checked"    
     */
    @Then("^the STB checkboxes of first tag remains as checked$")
    public void verifyStbCheckBoxChecked() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String[] stbsForTag1 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_STBS);
        StringBuilder stbs = new StringBuilder();
        for (String stbId : stbsForTag1) {
            String stbCheckboxXpath = DawgHousePageElements.STB_FILTER_CHECKBOX_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, stbId);
            WebElement checkBoxElement = driver.findElementByXPath(stbCheckboxXpath);
            if (!checkBoxElement.isSelected()) {
                stbs = stbs.append(stbId).append(",");
            }
        }
        Assert.assertTrue(0 < stbs.toString().length(),
            String.format("Check box corresponding to STB(%s) is not checked.", stbs));
    }

    /**
     * Verify the delete option display in tag
     * OnZukeStep:"the delete option of tag should be displayed"    
     * @throws DawgTestException 
     */
    @Then("^the delete option of tag should be displayed$")
    public void deleteOptionDisplayed() throws DawgTestException {
        String tag1 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1);
        WebElement tagDeleteDivElement = DawgIndexPageHelper.getInstance().getTagDeleteDivElement(tag1);
        String styleAtrributeValue = tagDeleteDivElement.getAttribute(DawgHousePageElements.TAG_DELETE_OPTION_STYLE_ATTRIBUTE);
        Assert.assertNotNull(styleAtrributeValue,
            "Style attribute value " + DawgHousePageElements.TAG_DELETE_OPTION_STYLE_ATTRIBUTE + " is getting null");
        int opacityBeginningIndex = styleAtrributeValue.lastIndexOf(DawgHousePageElements.OPACITY_BEGINNING_CHAR) + 1;
        Assert.assertNotNull(opacityBeginningIndex, "opacity beginning index is getting null ");
        int opacityEndingIndex = styleAtrributeValue.lastIndexOf(DawgHousePageElements.OPACITY_ENDING_CHAR);
        Assert.assertNotNull(opacityBeginningIndex, "opacity ending index is getting null ");
        String opacityStr = styleAtrributeValue.substring(opacityBeginningIndex, opacityEndingIndex);
        double opacity = Double.parseDouble(opacityStr);
        Assert.assertTrue(0 < opacity, "Failed to verify delete option displayed");
    }
}
