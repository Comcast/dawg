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

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.helper.DawgModelPageHelper;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains implementation for Dawg house common scenarios 
 * @author priyanka.sl
 */
public class DawgLoginPageGlue {
    /**
     * Select the button 
     * OnZukeStep:"I select (.*) button"    
     * @throws DawgTestException 
     */
    @When("^I select (.*) button$")
    public void selectButton(String button) throws DawgTestException {
        try {
            RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
            if (DawgHouseConstants.SIGN_IN_BUTTON.equals(button)) {
                driver.getKeyboard().pressKey(Keys.ENTER);
            } else if (DawgHouseConstants.DELETE_MODEL_BUTTON.equals(button)) {
                String stbModelToDelete = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_MODEL);
                WebElement modelTrElement = DawgModelPageHelper.getInstance().getModelRowElement(stbModelToDelete);
                Assert.assertTrue(modelTrElement.isDisplayed(), "Failed to locate model row element");
                WebElement deleteButton = modelTrElement.findElement(By.cssSelector(DawgHousePageElements.DELETE_BUTTON_TD_IDENTIFIER));
                Assert.assertTrue(deleteButton.isDisplayed(), "Failed to find delete model button in config page");
                deleteButton.click();
            } else {
                throw new DawgTestException("Invalid button " + button);

            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }


    /**
     * Enter invalid credentials 
     * OnZukeStep:"I enter incorrect credentials"    
     * @throws DawgTestException 
     */
    @When("^I enter incorrect credentials$")
    public void enterIncorrectCredentails() throws DawgTestException {
        DawgIndexPageHelper.getInstance().enterLoginCredentials(DawgHouseConstants.INVALID_USERNAME,
            DawgHouseConstants.INVALID_PASSWORD);
    }

    /**
     *  Verify the error message while use enters invalid login credentials
     * OnZukeStep:"I should see the error message"    
     * @throws DawgTestException 
     */
    @Then("^I should see the error message (.*)$")
    public void verifyLoginFailed(String msg) throws DawgTestException {
        try{
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        String invalidLoginMsg = driver.findElementByXPath(DawgHousePageElements.INVALID_LOGIN_MSG_XPATH).getText();
        Assert.assertEquals(invalidLoginMsg, DawgHouseConstants.INVALID_LOGIN_MSG,
            "Failed to verify invalid login error message");
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect Web element :" + e.getMessage());
        }
    }
}
