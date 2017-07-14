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

import java.util.Arrays;
import java.util.Collection;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Abstracts the interaction between the web elements of a row in the edit overlay
 * @author Kevin Pearson
 *
 */
public class EditDeviceRow {
    public static final String EDIT_KEY_CELL = "editKeyCell";
    public static final String EDIT_VALUE_INPUT = "editValueInput";
    public static final String MODIFIED_ICON = "modIcon";
    public static final String BTN_DEL_PROP = "btnDelProp";

    public static final String MODIFIED_ICON_CLASS = "modifiedIcon";

    private RemoteWebDriver driver;
    private WebElement rowUI;

    /**
     * Creates an EditDeviceRow
     * @param driver The web driver
     * @param rowUI The actual web element representing the &lt;tr&gt;
     */
    public EditDeviceRow(RemoteWebDriver driver, WebElement rowUI) {
        this.driver = driver;
        this.rowUI = rowUI;
    }

    /**
     * Gets the string that is displayed in the cell for the property key
     * @return
     */
    public String getPropDisp() {
        return rowUI.findElement(By.className(EDIT_KEY_CELL)).getText();
    }

    /**
     * Gets the text in the &lt;textarea&gt; element
     * @return
     */
    public String getValueText() {
        return valueInput().getAttribute("value");
    }

    /**
     * Changes the value in the &lt;textarea&gt; element
     * @param newValue
     */
    public void changeValue(String newValue) {
        WebElement valueInput = valueInput();
        valueInput.clear();
        valueInput.sendKeys(newValue);
    }

    /**
     * Gets the &lt;textarea&gt; element
     * @return
     */
    public WebElement valueInput() {
        return rowUI.findElement(By.className(EDIT_VALUE_INPUT));
    }

    /**
     * Checks if the modified icon is highlihgted
     * @return
     */
    public boolean isModified() {
        String classVal = rowUI.findElement(By.className(MODIFIED_ICON)).getAttribute("class");
        Collection<String> classes = Arrays.asList(classVal.split(" "));
        return classes.contains(MODIFIED_ICON_CLASS);
    }

    /**
     * Toggles the state of the modified icon
     */
    public void toggleModified() {
        rowUI.findElement(By.className(MODIFIED_ICON)).click();
    }

    /**
     * Clicks the delete button and then confirms the alert
     */
    public void delete() {
        rowUI.findElement(By.className(BTN_DEL_PROP)).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
}
