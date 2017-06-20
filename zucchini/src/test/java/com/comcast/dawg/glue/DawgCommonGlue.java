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
import java.util.NoSuchElementException;

import javax.swing.text.html.HTML.Tag;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.config.TestServerConfig;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.helper.DawgModelPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation for Dawg house common scenarios
 *
 * @author priyanka.sl
 *
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


}
