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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.comcast.dawg.TestServers;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.video.dawg.house.DawgHouseClient;

/**
 * Handles some common navigation with the advanced filter overlay
 * @author Kevin Pearson
 *
 */
public class AdvanceFilterNavigator {
    public RemoteWebDriver driver;
    WebElement conditionControl;
    public DawgHouseClient client = new DawgHouseClient(TestServers.getHouse());

    public AdvanceFilterNavigator(RemoteWebDriver driver) {
        this.driver = driver;
        this.conditionControl = driver.findElementByClassName(IndexPage.CONDITION_CONTROL);
    }

    /**
     * Adds a condition to the condition list. By putting the given values in the
     * html fields and clicking the add condition button
     * @param field The field to select in the select list
     * @param op The operator to select in the select list
     * @param value The value to put in the text input field
     */
    public void addCondition(String field, String op, String value) {
        Select fieldSelect = new Select(driver.findElementByClassName(IndexPage.FIELD_SELECT));
        fieldSelect.selectByVisibleText(field);
        Select opSelect = new Select(driver.findElementByClassName(IndexPage.OP_SELECT));
        opSelect.selectByVisibleText(op);
        WebElement fvInput = driver.findElementByClassName(IndexPage.FIELD_VALUE_INPUT);
        fvInput.sendKeys(value);
        driver.findElementByClassName(IndexPage.BTN_ADD_CONDITION).click();
    }

    /**
     * Checks the condition list for the given values.
     * @param checked All the conditions that are checked
     * @param unchecked All the conditions that are in the list but not checked
     */
    public void verifyConditions(String[] checked, String[] unchecked) {

        WebElement conditionList = driver.findElementByClassName(IndexPage.CONDITION_LIST);
        List<WebElement> conditions = conditionList.findElements(By.tagName("div"));
        Assert.assertEquals(conditions.size(), checked.length + unchecked.length);
        verifyConditions(conditions, checked, true);
        verifyConditions(conditions, unchecked, false);
    }

    /**
     * Checks if the given condition checks are in the condition list
     * @param conditions The conditions to look through
     * @param conditionTexts The texts to check if they exist
     * @param checked True if all the given conditions are checked
     */
    public void verifyConditions(List<WebElement> conditions, String[] conditionTexts, boolean checked) {
        for (String c : conditionTexts) {
            WebElement found = getCondition(conditions, c);
            Assert.assertNotNull(found);

            WebElement conditionCheckBox = found.findElement(By.className(IndexPage.CONDITION_CHECK_BOX));
            Assert.assertEquals(conditionCheckBox.getAttribute("checked") != null, checked);
        }
    }

    public void checkConditions(boolean check, String... conditionTexts) {
        WebElement conditionList = driver.findElementByClassName(IndexPage.CONDITION_LIST);
        List<WebElement> conditions = conditionList.findElements(By.tagName("div"));

        for (String conditionText : conditionTexts) {
            WebElement condition = getCondition(conditions, conditionText, true);
            WebElement conditionCheckBox = condition.findElement(By.className(IndexPage.CONDITION_CHECK_BOX));
            if (conditionCheckBox.isSelected() != check) {
                conditionCheckBox.click();
            }
        }
    }

    public WebElement getCondition(List<WebElement> conditions, String conditionText) {
        return getCondition(conditions, conditionText, false);
    }

    public WebElement getCondition(List<WebElement> conditions, String conditionText, boolean failOnNull) {
        WebElement found = null;
        for (WebElement condition : conditions) {
            if (condition.findElement(By.className(IndexPage.CONDITION_TEXT)).getText().equals(conditionText)) {
                found = condition;
                break;
            }
        }
        if (failOnNull && (found == null)) {

            Collection<String> possibleConditions = new ArrayList<String>();
            for (WebElement condition : conditions) {
                possibleConditions.add(condition.findElement(By.className(IndexPage.CONDITION_TEXT)).getText());
            }

            Assert.fail("Could not find a condition for text '" + conditionText + "' possible values are : " +
                    StringUtils.join(possibleConditions, ", "));
        }
        return found;
    }

    /**
     * Checks if the buttons are enabled or disabled
     * @param and true if the AND button is enabled
     * @param or true if the OR button is enabled
     * @param not true if the NOT button is enabled
     * @param del true if the DEL button is enabled
     * @param brk true if the BREAK button is enabled
     * @param search true if the Search button is enabled
     */
    public void checkButtonsEnabled(boolean and, boolean or, boolean not, boolean del, boolean brk, boolean search) {
        WebElement conditionControl = driver.findElementByClassName(IndexPage.CONDITION_CONTROL);
        assertEnabled(conditionControl, IndexPage.BTN_AND, and);
        assertEnabled(conditionControl, IndexPage.BTN_OR, or);
        assertEnabled(conditionControl, IndexPage.BTN_NOT, not);
        assertEnabled(conditionControl, IndexPage.BTN_DEL, del);
        assertEnabled(conditionControl, IndexPage.BTN_BREAK, brk);
        assertEnabled(driver, IndexPage.BTN_SEARCH, search);
    }

    /**
     * Helper method to verify if a button is enabled
     * @param parent The parent of the button
     * @param className The class name used to look it up in the parent
     * @param enabled true if the button should be enabled, false if it should be disabled
     */
    private void assertEnabled(SearchContext parent, String className, boolean enabled) {
        WebElement btn = parent.findElement(By.className(className));
        Assert.assertEquals(btn.isEnabled(), enabled);
    }

    /**
     * Helper method to look up a button by classname and then click it
     * @param className The name of the class of the button to click
     */
    private void btn(String className) {
        conditionControl.findElement(By.className(className)).click();
    }

    /**
     * Clicks the AND button
     */
    public void and() {
        btn(IndexPage.BTN_AND);
    }

    /**
     * Clicks the OR button
     */
    public void or() {
        btn(IndexPage.BTN_OR);
    }

    /**
     * Clicks the NOT button
     */
    public void not() {
        btn(IndexPage.BTN_NOT);
    }

    /**
     * Clicks the DEL button
     */
    public void del() {
        btn(IndexPage.BTN_DEL);
    }

    /**
     * Clicks the BREAK button
     */
    public void brk() {
        btn(IndexPage.BTN_BREAK);
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public void setDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }
}
