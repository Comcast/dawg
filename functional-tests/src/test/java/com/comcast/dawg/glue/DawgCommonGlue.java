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

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTML.Tag;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgAdvancedFilterPageHelper;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.helper.DawgModelPageHelper;
import com.comcast.dawg.helper.DawgTagCloudHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.response.Response;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation for Dawg house common scenarios 
 * @author priyanka.sl
 */
public class DawgCommonGlue {

    /**
     * Login to dawg-house portal   
     * OnZukeStep:"I am on the Log In page of dawg house portal"
     */
    @Given("^I am on the Log In page of dawg house portal$")
    public void launchDawgLoginPage() {       
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isLoginFormDisplayed(),
            "Failed to display dawg house login page");
    }

    /**
     * Enter login credentials to dawg-house login form   
     * OnZukeStep:"I enter correct username and password" 
     */
    @When("^I enter correct username and password$")
    public void enterUserNamePassword() {
        DawgIndexPageHelper.getInstance().enterLoginCredentials(TestServerConfig.getUsername(),
            TestServerConfig.getPassword());
    }

    /**
     * Verify dawg-home page displayed  
     * OnZukeStep:"I should see the Dawg House home page"    
     * @throws DawgTestException 
     */
    @Then("^I should see the Dawg House home page$")
    public void verifyHomePageDisplayed() throws DawgTestException {

        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String expectedNewPageUrl = TestServerConfig.getHouse() + TestServerConfig.getUsername();
        //Verify dawg home page displayed       
        Assert.assertEquals(driver.getCurrentUrl(), expectedNewPageUrl);
        try {
            WebElement userInfo = driver.findElementByClassName(DawgHousePageElements.USER_SECTION_CLASS);
            List<WebElement> spans = userInfo.findElements(By.tagName(Tag.SPAN.toString()));
            Assert.assertFalse(spans.isEmpty());
            WebElement userDisplaySpan = spans.get(0); // the first span should show the user name
            Assert.assertEquals(userDisplaySpan.getText(), TestServerConfig.getUsername());
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Verify dawg-home page displayed  
     * OnZukeStep:"I am on the dawg house home page"    
     * @throws DawgTestException 
     */
    @Given("^I am on the dawg house home page$")
    public void launchDawgHomePage() throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        //Launching dawg login page and verify login form displayed
        launchDawgLoginPage();
        //enter valid credentials
        enterUserNamePassword();
        driver.getKeyboard().pressKey(Keys.ENTER);
        //Verify dawg home page displayed
        verifyHomePageDisplayed();
        // Uncheck mine reservation if it is selected
        DawgIndexPageHelper.getInstance().checkOrUncheckMineBtn(DawgHouseConstants.UNCHECK);
    }


    /**
    * Step definition to verify the status code received in xDawg response.
    * @param statCode - Expected status code to verify
    */
    @Then("I should receive status code (\\d+)$")
    public void verifyStatusCode(int statCode) {
        Response response = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_REST_RESPONSE);     
        Assert.assertNotNull(response, "Received null response from TestContext");
        Assert.assertEquals(response.getStatusCode(), statCode,
            "Failed to verify the status code " + "(Status Code Received: " + response.getStatusCode() + ").");
    }

    /**
     * Launch STB model configuration page
     * OnZukeStep:"I am on the model configuration page"    
     * @throws DawgTestException 
     */
    @Given("^I am on the model configuration page$")
    public void launchModelConfigPage() throws DawgTestException {
        // Launch and Login the dawg house page and verify home page displayed
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        launchDawgHomePage();
        String modelURL = TestServerConfig.getHouse() + TestConstants.MODEL_CONFIG_URI;
        // Navigate to model config page       
        driver.get(modelURL);
        // Predefined wait for loading of the model config page.
        SeleniumWaiter.waitTill(TestConstants.MODEL_CONFIG_PAGE_LOAD_WAIT);
        //Verify the model config page loaded successfully       
        Assert.assertTrue(
            modelURL.equals(driver.getCurrentUrl()) && DawgModelPageHelper.getInstance().isModelPageDisplayed(),
            "Model configuration page is not loaded.");
    }

    /**
    * Navigate to dawg house advanced filter overlay         
    * @throws DawgTestException      
    */
    @Given("^I am on advanced filter overlay$")
    public void verifyAdvancedFilterOverlay() throws DawgTestException {
        // Launch and Login the dawg house page and verify home page displayed
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        launchDawgHomePage();
        // Navigate to advanced filter overlay page
        driver.get(TestServerConfig.getHouse() + DawgHouseConstants.FILTER_REQ_PARAM);
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().selectAdvancedBtn(),
            "Failed to select the advanced button to navigate to filter overlay.");
        // Verifying whether search overlay is displayed or not
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().isSearchFilterOverlayDisplayed(),
            "Failed to display search results");
        
        
    }


    /**
     * Add filter conditions to advanced filter overlay   
     */
    @Given("^I added (\\d+) filter (?:value|values|value/s) to advanced filter overlay$")
    public void addFilterValues(int filterCountToAdd) {
        // Add a filter condition  
        List<String> addedFilters = DawgAdvancedFilterPageHelper.getInstance().addFilterValues(filterCountToAdd);
        SeleniumWaiter.waitTill(TestConstants.OVERLAY_LOAD_WAIT);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, addedFilters);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_COUNT, filterCountToAdd);
        SeleniumImgGrabber.addImage();
    }

    /**
     * Select condition buttons(AND,OR,NOT,DEL,BREAK)
     * @throws DawgTestException
     */
    @When("^I select condition (?:button|buttons|button/s) '(.*)'$")
    public void selecButton(List<String> buttons) throws DawgTestException {
        //Select the condition button and verify the specified buttons are selected successfully
        String buttonsNotSelected = DawgAdvancedFilterPageHelper.getInstance().verifyBtnSelected(buttons);
        Assert.assertTrue(0 == buttonsNotSelected.trim().length(),
            "Failed to select condition button/s" + buttonsNotSelected);
    }

    /**
     * Verify filter values added in the filter overlay 
     */
    @Then("^I should see filter (?:value|values|value/s) added in filter overlay$")
    public void verifyFilterValueAdded() {
        StringBuilder filtersNotAdded = new StringBuilder();
        List<String> addedFilters = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_CONDITION);
        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        Assert.assertFalse(filterConditions.isEmpty(), "Failed to find filter conditions in advanced filter overlay");
        // Verify filter values added
        int filterCountAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_COUNT);
        Assert.assertEquals(filterCountAdded, filterConditions.size(),
            "Failed to find the specified filter count in the filter list");
        for (String filter : addedFilters) {
            if (!filterConditions.contains(filter)) {
                filtersNotAdded.append(filter).append(", ");
            }
        }
        SeleniumImgGrabber.addImage();
        // Verify the conditions added in filter overlay
        Assert.assertTrue(0 == filtersNotAdded.toString().trim().length(),
            "Filter condition/s '" + filtersNotAdded + "' not added in the filter overlay");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
    }

    /**
     * Select tag element from tag cloud
     * OnZukeStep:"I select another tag element in tag cloud" 
     * @throws DawgTestException  
     */
    @Then("^I (?:select|selected) (.*) tag (?:element|elements) in tag cloud$")
    public void selectAnotherTag(String type) throws DawgTestException {
        List<String> tagNames = new ArrayList<String>();
        StringBuilder message = new StringBuilder();
        if ("any".equals(type)) {
            tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
        } else if ("another".equals(type)) {
            tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString());
        } else if ("both".equals(type) || "two".equals(type)) {
            tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
            tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString());

        } else {
            throw new DawgTestException("Invalid type" + type);
        }       
        // check the tags are selected or not
        for (String tagName : tagNames) {
            if (!DawgTagCloudHelper.getInstance().selectTag(tagName)) {
                message.append(tagName).append(",");
            }
        }        
        Assert.assertTrue((0 == message.toString().length()), String.format("Failed to select Tag(%s).", message));
        SeleniumImgGrabber.addImage();
    }
}
