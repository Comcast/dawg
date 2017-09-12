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
package com.comcast.dawg.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.dawg.utils.DawgCommonUIUtils;
import com.comcast.zucchini.TestContext;

/**
 * Helper functionalities for Dawg house Tag Cloud UI behaviors
 * 
 * @author priyanka.sl
 */
public class DawgTagCloudHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DawgModelPageHelper.class);
    private static Object lock = new Object();
    private static DawgTagCloudHelper tagHelper = null;

    /**
     * Creates single instance of tagHelper
     * 
     * @return tagHelper
     */
    public static DawgTagCloudHelper getInstance() {
        synchronized (lock) {
            if (null == tagHelper) {
                tagHelper = new DawgTagCloudHelper();
            }
        }
        return tagHelper;
    }

    /**
     * Select the specified tag
     * 
     * @param tagName
     *            tag name to be selected.
     * @return true if tag is selected, false otherwise
     */
    public boolean selectTag(String tagName) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            if (isTagHighLighted(tagName)) {
                //If the tag is already selected
                return true;
            }
            WebElement element = driver.findElementByXPath(this.getTagCloudDiv(tagName));
            if (element.isDisplayed()) {
                element.click();
                SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to De-select tag", e.getMessage());
            return false;

        }
        return false;

    }

    /**
     * Identify whether tag is not highlighted or not.
     *
     * @param tagName
     *            tag name which need to verified.
     *
     * @return true if tag selected, false otherwise.
     */
    public boolean isTagHighLighted(String tagName) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement element = driver.findElementByXPath(this.getTagCloudDiv(tagName));
            String getHightColor = element.getCssValue("background-color");
            return getHightColor.contains(DawgHousePageElements.TAG_HIGHLIGHT_COLOUR);
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed verify tag highlight", e.getMessage());
            return false;
        }
    }

    /**
     * Add a new tag from dawg home page
     * @param tagName -tag name to add
     * @param stbIds to tag in new tag      
     */
    public void addTagFromIndexPage(String tagName, String... stbIds) {
        for (String stbId : stbIds) {
            checkOrUncheckSTB(stbId, DawgHouseConstants.CHECK);
            if (isStbSelected(stbId)) {
                enterTagNameAndSubmitBulkTagBtn(tagName);
                DawgCommonUIUtils.getInstance().bufferTheTagsToBeCleared(tagName, stbId);
            }
        }
    }

    /**
     * Verify stb selected or not
     * @param stbId to select
     * @return true if stb is selected, false otherwise 
     */
    public boolean isStbSelected(String stbId) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement stbCheckbox = null;
            stbCheckbox = driver.findElementByXPath(DawgHousePageElements.STB_FILTER_CHECKBOX_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, stbId));
            if (stbCheckbox.isSelected()) {
                return true;
            }

        } catch (NoSuchElementException e) {
            LOGGER.error("Failed verify STB selected or not", e.getMessage());
        }
        return false;
    }

    /**
     * Check/Uncheck an the specified STB
     * @param stbId to check/uncheck
     * @param operation check/uncheck     
     */
    public void checkOrUncheckSTB(String stbId, String operation) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            WebElement chckBoxEle = driver.findElementByXPath(DawgHousePageElements.STB_FILTER_CHECKBOX_XPATH.replace(
                DawgHousePageElements.REPLACEABLE_ELEMENT, stbId));
            // For scrolling down.
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + chckBoxEle.getLocation().y + ")");
            SeleniumWaiter.waitTill(DawgHousePageElements.STB_CHECKBOX_ELEMENT_WAIT);
            //If check box is not selected then select it
            if (chckBoxEle.isSelected() && DawgHouseConstants.UNCHECK.equals(operation) || !chckBoxEle.isSelected() && DawgHouseConstants.CHECK.equals(operation)) {
                chckBoxEle.click();
                SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
            }

        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to check or uncheck STBs:" + e.getMessage());
        }
    }

    /**
     * Get the available tag details from tag cloud
     * @return - Map with tag name with STB count tagged     
     */
    public Map<String, Integer> getTagsFromTagCloud() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        Map<String, Integer> tagWithStbCount = new HashMap<String, Integer>();
        try {
            WebElement element = driver.findElement(By.id(DawgHousePageElements.TAG_CLOUD_ID));
            for (WebElement ele : element.findElements(By.className("tagText"))) {
                String[] tagWithSTBCount = ele.getText().split(" ");
                String tagName = tagWithSTBCount[0];
                int stbCount = 0;
                if (tagName.contains("(")) {
                    // There might be chances to found tag count alone with out have any tag name               
                    stbCount = Integer.parseInt(tagName.replaceAll(DawgHouseConstants.REPLACE_REGEX, ""));
                    tagName = null;
                } else {
                    stbCount = Integer.parseInt(tagWithSTBCount[1].replaceAll(DawgHouseConstants.REPLACE_REGEX, ""));
                }
                tagWithStbCount.put(tagName, stbCount);
            }

        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get tag with tag count from tag cloud", e.getMessage());
        }
        return tagWithStbCount;
    }

    /**
     * Get the STB lists from filter table
     * 
     * @return List of STBs   
     */
    public List<String> getSTBLists() {
        return DawgAdvancedFilterPageHelper.getInstance().getStbIdsFromFilterTable();
    }

    /**
     * Click on delete tag option and wait for the tag element to get removed.
     *
     * @param  tagName  Name of the tag element.
     * @return true if delete option clicked, false otherwise     
     */
    public boolean selectDeleteOptionInTag(String tagName) {
        try {
            WebElement deleteDivTagElement = DawgIndexPageHelper.getInstance().getTagDeleteDivElement(tagName);
            if (deleteDivTagElement.isDisplayed()) {
                deleteDivTagElement.click();
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to select delete option in tag", e.getMessage());
        }
        return false;
    }

    /**
     * Accept the tag delete confirmation alert  
     */
    public void acceptDeleteTagAlert() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        // Switching to alert and accepting it.      
        Alert alert = driver.switchTo().alert();
        alert.accept();
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
    }

    /**
     * Get tag cloud div element
     * @return String tag cloud div element     
     */
    private String getTagCloudDiv(String tagName) {
        return DawgHousePageElements.TAG_ID_PARENT_DIV_XPATH.replace(DawgHousePageElements.REPLACEABLE_ELEMENT, tagName);
    }

    /**
     * Enter tag name and submit bulk tag button for adding a new tag
     * @param tagname to add  
     */
    public boolean enterTagNameAndSubmitBulkTagBtn(String tagName) {

        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            // Enter the tag name.
            WebElement elementTagText = driver.findElement(By.id(DawgHousePageElements.BULK_TAG_TEXT_ID));
            // Clearing the text area before entering.
            elementTagText.clear();
            elementTagText.sendKeys(tagName);
            WebElement bulkTagBtn = driver.findElement(By.id(DawgHousePageElements.BULK_TAG_BTN_ID));
            if (bulkTagBtn.isDisplayed()) {
                bulkTagBtn.click();
                SeleniumWaiter.waitTill(3);
                return true;
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to enter tag name and submit bulk tag button", e.getMessage());
        }
        return false;
    }


    /**
     * Get the tags in which delete option appears.     
     * @return List of tags     
     */
    public List<String> getTagsInDeleteOptionAppears() {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        List<String> tagWithDelOption = new ArrayList<String>();
        try {
            WebElement element = driver.findElement(By.id(DawgHousePageElements.TAG_CLOUD_ID));
            for (WebElement ele : element.findElements(By.className("tagDelete"))) {
                if (ele.getAttribute("style").equals("opacity: 1;")) {
                    tagWithDelOption.add(ele.getAttribute("tagval"));
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to get tags having delete option appears", e.getMessage());
        }
        return tagWithDelOption;
    }

    /**
     * De-select the tag in tag cloud based on the tag name provided.
     * 
     * @param tagName
     *            tag name to be de-select.
     * @return true if tag is de-selected, false otherwise
     */
    public boolean deSelectTag(String tagName) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        try {
            if (isTagHighLighted(tagName)) {
                WebElement element = driver.findElementByXPath(this.getTagCloudDiv(tagName));
                if (element.isDisplayed()) {
                    element.click();
                    SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
                    return true;
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Failed to De-select tag", e.getMessage());
        }
        return false;
    }

}
