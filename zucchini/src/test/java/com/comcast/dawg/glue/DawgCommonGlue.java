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

import javax.swing.text.html.HTML.Tag;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgLoginHelper;
import com.comcast.dawg.utils.SeleniumImgGrabber;
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
     * @throws InterruptedException 
     */
    @Given("^I am on the Log In page of dawg house portal$")
    public void launchDawgLoginPage() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        driver.get(DawgLoginHelper.getDawgUrl());
        SeleniumImgGrabber.addImage();
        // Verify login page displayed
        WebElement loginFormElement = driver.findElementByXPath(DawgHousePageElements.LOGIN_FORM_XPATH);
        Assert.assertNotNull(loginFormElement, "Login form element is null");
        Assert.assertTrue(loginFormElement.isDisplayed(), "Failed to display dawg house login page");
    }

    /**
     * Enter login credentials to dawg-house login form   
     * OnZukeStep:"I enter correct username and password"    
     */
    @When("^I enter correct username and password$")
    public void enterUserNamePassword() {
        DawgLoginHelper.getInstance().enterLoginCredentials(DawgLoginHelper.getDawgUserName(),
            DawgLoginHelper.getDawgPassword());
    }

    /**
     * Verify dawg-home page displayed  
     * OnZukeStep:"I should see the Dawg House home page"    
     */
    @Then("^I should see the Dawg House home page$")
    public void verifyHomePageDisplayed() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String expectedNewPageUrl = DawgLoginHelper.getDawgUrl() + DawgLoginHelper.getDawgUserName();
        //Verify dawg home page displayed       
        Assert.assertEquals(driver.getCurrentUrl(), expectedNewPageUrl);
        WebElement userInfo = driver.findElementByClassName(DawgHousePageElements.USER_SECTION_CLASS);
        List<WebElement> spans = userInfo.findElements(By.tagName(Tag.SPAN.toString()));
        Assert.assertFalse(spans.isEmpty());
        WebElement userDisplaySpan = spans.get(0); // the first span should show the user name
        Assert.assertEquals(userDisplaySpan.getText(), DawgLoginHelper.getDawgUserName());
    }

}
