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

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.helper.LoginHelper;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation for Dawg house common scenarios
 *
 * @author priyanka.sl
 *
 */
public class DawgLoginPageGlue {

    /**
     * Select the button 
     * OnZukeStep:"I select (.*) button"    
     */
    @When("^I select (.*) button$")
    public void selectButton(String button) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        driver.getKeyboard().pressKey(Keys.ENTER);
    }

    /**
     * Enter invalid credentials 
     * OnZukeStep:"I enter incorrect credentials"    
     */
    @When("^I enter incorrect credentials$")
    public void enterIncorrectCredentails() {
        LoginHelper.getInstance().enterLoginCredentials("$$$$$", "password");
    }

    /**
     *  Verify the error message while use enters invalid login credentials
     * OnZukeStep:"I should see the error message"    
     */
    @Then("^I should see the error message (.*)$")
    public void verifyLoginFailed(String msg) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String invalidLoginMsg = driver.findElementByXPath("//div[@class='alert alert-danger']").getText();
        Assert.assertEquals(invalidLoginMsg, DawgHouseConstants.INVALID_LOGIN_MSG,
            "Failed to verify invalid login error message");

    }
}
