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
package com.comcast.dawg.house;

import java.io.IOException;
import java.util.List;

import javax.swing.text.html.HTML.Tag;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.comcast.dawg.TestServers;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.dawg.house.pages.LoginPage;

/**
 * UI Testing for Dawg house's login page
 * @author Kevin Pearson
 *
 */
public class LoginPageIT extends SeleniumTest {

    /**
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(
        description="Tests if a user can input a user name and click enter to log in",
        groups={"uitest", "smoke"}
    )
    public void testLogin() throws IOException {
        String user = "integrationuser";

        RemoteWebDriver driver = drivers.get();
        driver.get(TestServers.getHouse());
        loginWithUserName(driver, user);
        String expectedNewPageUrl = TestServers.getHouse() + user + "/";
        Assert.assertEquals(driver.getCurrentUrl(), expectedNewPageUrl);
        WebElement userInfo = driver.findElementByClassName(IndexPage.USER_SECTION_CLASS);
        List<WebElement> spans = userInfo.findElements(By.tagName(Tag.SPAN.toString()));
        Assert.assertFalse(spans.isEmpty());
        WebElement userDisplaySpan = spans.get(0); // the first span should show the user name
        Assert.assertEquals(userDisplaySpan.getText(), user);
    }

    /**
     * @author Kevin Pearson
     * @throws IOException
     */
    @Test(
        description="Tests if a message explaining that a username was invalid " +
            "becomes visible when the user tries to log in with an invalid username",
            groups={"uitest", "smoke"}
    )
    public void testBadLogin() throws IOException {
        RemoteWebDriver driver = drivers.get();
        String user = "$$$$";

        driver.get(TestServers.getHouse());
        WebElement badLoginMessage = driver.findElementById(LoginPage.BAD_USER_MSG_ID);
        Assert.assertEquals(badLoginMessage.getCssValue("display"), "none");
        loginWithUserName(driver, user);

        String expectedNewPageUrl = TestServers.getHouse();
        Assert.assertEquals(driver.getCurrentUrl(), expectedNewPageUrl);
        badLoginMessage = driver.findElementById(LoginPage.BAD_USER_MSG_ID);
        Assert.assertEquals(badLoginMessage.getCssValue("display"), "block");
    }

    private void loginWithUserName(RemoteWebDriver driver, String user) {
        driver.findElementById(LoginPage.USER_INPUT_ID).click();
        driver.getKeyboard().sendKeys(user);
        driver.getKeyboard().pressKey(Keys.ENTER);
    }
}
