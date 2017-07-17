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
import java.util.NoSuchElementException;
import java.util.Random;

import javax.swing.text.html.HTML.Tag;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgAdvancedFilterPageHelper;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.helper.DawgModelPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;
import com.jayway.restassured.response.Response;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

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
     * @throws DawgTestException 
     * @throws InterruptedException 
     */
    @Given("^I am on the Log In page of dawg house portal$")
    public void launchDawgLoginPage() throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            driver.get(TestServerConfig.getHouse());
            SeleniumImgGrabber.addImage();
            // Verify login page displayed
            WebElement loginFormElement = driver.findElementByXPath(DawgHousePageElements.LOGIN_FORM_XPATH);
            Assert.assertTrue(loginFormElement.isDisplayed(), "Failed to display dawg house login page");
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }

    /**
     * Enter login credentials to dawg-house login form   
     * OnZukeStep:"I enter correct username and password"    
     * @throws DawgTestException 
     */
    @When("^I enter correct username and password$")
    public void enterUserNamePassword() throws DawgTestException {
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
        WebElement searchBtnElement = driver.findElementByClassName(DawgHousePageElements.ADV_SEARCH_BUTTON);
        if (searchBtnElement.isDisplayed()) {
            searchBtnElement.click();
        } else {
            throw new DawgTestException("Failed to find the seach button in advanced filter overlay");
        }
    }    

    /**
     * Add filter conditions to advanced filter overlay
     * @throws DawgTestException
     */
    @Given("^I added (\\d+) filter (?:value|values|value/s) to advanced filter overlay$")
    public void addFilterValues(int filterCountToAdd) throws DawgTestException {
        // Add a filter condition        
        int filterCountAdded = 0;
        int maxRetry = 0;
        List<String> filters = new ArrayList<String>();
        // Add filter conditions
        while (filterCountToAdd != filterCountAdded || maxRetry >= TestConstants.VALID_FILTER_CONDITIONS.length * 2) {
            //Randomly pick valid conditions from valid filter condition array
            int random = new Random().nextInt(TestConstants.VALID_FILTER_CONDITIONS.length);
            String filterToAdd = (TestConstants.VALID_FILTER_CONDITIONS[random]);
            List<String> filterList = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();            
            if (!filterList.contains(filterToAdd)) {
                filters.add(filterToAdd);
                String[] conditions = filterToAdd.split(" ");
                DawgAdvancedFilterPageHelper.getInstance().addFilterCondition(conditions[0], conditions[1],
                    conditions[2]);
                filterCountAdded++;
            }
        }
        SeleniumWaiter.waitTill(TestConstants.OVERLAY_LOAD_WAIT);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filters);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_COUNT, filterCountToAdd);
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
     * @throws DawgTestException 
     */
    @Then("^I should see filter (?:value|values|value/s) added in filter overlay$")
    public void verifyFilterValueAdded() throws DawgTestException {
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
        // Verify the conditions added in filter overlay
        Assert.assertTrue(0 == filtersNotAdded.toString().trim().length(),
            "Filter condition/s '" + filtersNotAdded + "' not added in the filter overlay");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
    }

}
