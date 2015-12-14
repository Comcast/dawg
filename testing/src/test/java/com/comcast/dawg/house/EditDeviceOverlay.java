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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Abstracts the interaction between the edit device overlay web elements
 * @author Kevin Pearson
 *
 */
public class EditDeviceOverlay {
    public static final String NEW_PROP_KEY = "newPropKey";
    public static final String CB_NEW_PROP_SET = "cbNewPropSet";
    public static final String BTN_ADD_PROP = "btnAddProp";
    public static final String BTN_SAVE = "btnSave";
    public static final String CLOSE_ICON = "ui-dialog-titlebar-close";

    private WebElement overlayUI;
    private RemoteWebDriver driver;

    /**
     * Creates an EditDeviceOverlay
     * @param driver The web driver
     * @param overlayUI The element representing the editDevice div
     */
    public EditDeviceOverlay(RemoteWebDriver driver, WebElement overlayUI) {
        this.driver = driver;
        this.overlayUI = overlayUI;
    }

    /**
     * Adds a property by inputing the key and then clicking add
     * @param key The key to add
     * @param set true if the Set check mark should be checked
     */
    public void addProp(String key, boolean set) {
        addProp(key, set, false);
    }

    /**
     * Adds a property by inputing the key and then clicking add
     * @param key The key to add
     * @param set true if the Set check mark should be checked
     * @param byButton true if the property is added by clicking the button, false if it is by pushing enter
     */
    public void addProp(String key, boolean set, boolean byButton) {
        WebElement keyInp = overlayUI.findElement(By.className(NEW_PROP_KEY));
        keyInp.sendKeys(key);
        WebElement setCb = overlayUI.findElement(By.className(CB_NEW_PROP_SET));
        if (setCb.isSelected() != set) {
            setCb.click();
        }
        if (byButton) {
            overlayUI.findElement(By.className(BTN_ADD_PROP)).click();
        } else {
            keyInp.click();
            driver.getKeyboard().pressKey(Keys.ENTER);
        }
    }

    /**
     * Clicks the Save button
     */
    public void save() {
        overlayUI.findElement(By.className(BTN_SAVE)).click();
    }

    /**
     * Clicks the X icon to close the overlay
     */
    public void close() {
        WebElement dialog = overlayUI.findElement(By.xpath(".."));
        dialog.findElement(By.className(CLOSE_ICON)).click();
    }

    /**
     * Gets a row for the given key
     * @param propKey The key to get the row for
     * @return
     */
    public EditDeviceRow getRow(String propKey) {
        WebElement row = overlayUI.findElement(By.xpath(".//tr[@data-key='" + propKey + "']"));
        return new EditDeviceRow(driver, row);
    }

    /**
     * Counts the number of rows are in the table
     * @return
     */
    public int numRows() {
        List<WebElement> rows = overlayUI.findElements(By.className("editRow"));
        return rows.size();
    }

    public WebElement getOverlayUI() {
        return overlayUI;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }
}
