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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Abstracts the interaction between the context menu web elements
 * @author Kevin Pearson
 *
 */
public class ContextMenu {
    private RemoteWebDriver driver;
    private WebElement menuUI;

    public static final String EDIT_DEVICE_OVERLAY = "editDeviceOverlay";

    /**
     * Creates a new ContextMenu
     * @param driver The web driver
     * @param menuUI The actual elements representing the context menu div
     */
    public ContextMenu(RemoteWebDriver driver, WebElement menuUI) {
        this.driver = driver;
        this.menuUI = menuUI;
    }

    /**
     * Finds the given menu item and then clicks it
     * @param cmi The item to click
     */
    public void clickMenuItem(ContextMenuItem cmi) {
        List<WebElement> items = menuUI.findElements(By.tagName("a"));
        for (WebElement item : items) {
            if (item.getText().equals(cmi.getDisp())) {
                item.click();
                return;
            }
        }
    }

    /**
     * Clicks the Edit menu item and then waits for the Edit Overlay to be visible
     * @return
     */
    public EditDeviceOverlay launchEditDeviceOverlay() {
        clickMenuItem(ContextMenuItem.edit);
        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement overlay = driver.findElement(By.className(EDIT_DEVICE_OVERLAY));
        wait.until(ExpectedConditions.visibilityOf(overlay));
        return new EditDeviceOverlay(driver, overlay);
    }
}
