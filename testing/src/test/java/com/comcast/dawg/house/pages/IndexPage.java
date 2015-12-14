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
package com.comcast.dawg.house.pages;

import java.util.ArrayList;
import java.util.List;

import com.comcast.dawg.TestServers;
import com.comcast.dawg.selenium.SeleniumWaiter;

import org.apache.log4j.Logger;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Holds element constants for the index.jsp page for dawg-house.
 *
 * @author  Kevin Pearson
 */
public class IndexPage {

    /** Index page logger. */
    private static final Logger LOGGER = Logger.getLogger(IndexPage.class);

    /** Replaceable element for attribute values. */
    private static final String REPLACEABLE_ELEMENT = "#@#";

    /** Xpath for tag div element in tag cloud. */
    private static final String TAG_ID_DIV_XPATH = "//div[@tagval='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for tag parent div element in tag cloud. */
    private static final String TAG_ID_PARENT_DIV_XPATH = TAG_ID_DIV_XPATH + "/..";

    /** Xpath for STB filter checkbox row element. */
    private static final String STB_FILTER_DIV_XPATH = "//div[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for STB filter checkbox row element. */
    private static final String STB_FILTER_CHECKBOX_XPATH = "//input[@data-deviceid='" + REPLACEABLE_ELEMENT + "']";

    /** Xpath for toggle button element. */
    private static final String TOGGLE_BUTTON_XPATH = "//input[@class='toggle-all-checkbox']";

    /** Xpath for getting the filter table div element. */
    private static final String FILTERED_TABLE_DIV_ELEMENT_XPATH = "//div[@class='filteredTable']";

    /** Xpath for getting the filtered table successor div elements. */
    private static final String FILTERED_TABLE_SUCCESSOR_DIV_ELEMENTS_XPATH = FILTERED_TABLE_DIV_ELEMENT_XPATH + "/div";

    /** Class attribute value for tag highlight. * */
    private static final String TAG_HIGHLIGHT_CLASS_ATTRIBUTE_VALUE = "tagContainerSelected";

    /** Name of value attribute element. */
    private static final String VALUE_ATTRIBUTE_ELEMENT = "value";

    /** Beginning char for opacity. */
    private static final char OPACITY_BEGINNING_CHAR = ' ';

    /** Ending char for opacity. */
    private static final char OPACITY_ENDING_CHAR = ';';

    /** Time out in second for index page load. * */
    private static final int INDEX_PAGE_LOAD_TIMEOUT = 120;

    /** Waiting time in second for bulk tag select or deselect to happen. * */
    private static final int BULK_TAG_CLICK_WAIT = 4;

    /** Time out in second for tag element to get removed. */
    private static final int TAG_DELETION_TIMEOUT = 10;

    /** Waiting time in second for toggle all check box selection/deselection. */
    private static final int TOGGLE_ALL_CHECKBOX_WAIT = 1;

    /** The class of the &lt;section&gt; tag that holds the user information. */
    public static final String USER_SECTION_CLASS = "user";

    /** The class of the &lt;checkbox&gt; that is used to select for tagging. */
    public static final String BULK_CHECK_BOX = "bulk-checkbox";

    /** Filtered table device id attribute name. */
    private static final String FILTERED_TABLE_DEVICE_ID_ATTRIBUTE = "data-deviceid";

    /** Tag delete option style attribute name. */
    private static final String TAG_DELETE_OPTION_STYLE_ATTRIBUTE = "style";

    /** Tag delete div element identifier attribute value. */
    private static final String TAG_DELETE_DIV_ELEMENT_IDENTIFIER = "tagDelete";

    /** Class attribute name of parent element of the tag div element in the tag cloud. */
    private static final String TAG_CLOUD_PARENT_TAG_DIV_CLASS_ATTRIBUTE = "class";

    /** Tag cloud tag text element identifier attribute value. */
    private static final String TAG_CLOUD_TAG_TEXT_ELEMENT_IDENTIFIER = "tagText";

    /** Bulk check box identifier. */
    private static final String BULK_CHECKBOX_INPUT_IDENTIFIER = ".//input[@class='bulk-checkbox']";

    /** The tag cloud id. */
    public static final String TAG_CLOUD_ID = "tagCloud";

    /** The bulk tag text box id. */
    public static final String BULK_TAG_TEXT_ID = "bulkTagInput";

    /** The bulk tag button id. */
    public static final String BULK_TAG_BTN_ID = "bulkTagButton";

    /** The check box attribute name that uniquely identifies the check box. */
    public static final String DEVICE_ID_ATTR = "data-deviceId";

    public static final String ADV_SEARCH_BUTTON = "advSearchBtn";
    public static final String FIELD_SELECT = "fieldSelect";
    public static final String OP_SELECT = "opSelect";
    public static final String FIELD_VALUE_INPUT = "fieldValueInput";
    public static final String BTN_ADD_CONDITION = "btnAddCondition";
    public static final String CONDITION_LIST = "conditionList";
    public static final String CONDITION_CONTROL = "conditionControl";
    public static final String CONDITION_CHECK_BOX = "cbCondition";
    public static final String BTN_AND = "btnAnd";
    public static final String BTN_OR = "btnOr";
    public static final String BTN_NOT = "btnNot";
    public static final String BTN_DEL = "btnDel";
    public static final String BTN_BREAK = "btnBreak";
    public static final String BTN_SEARCH = "btnSearch";
    public static final String FILTERED_TABLE = "filteredTable";
    public static final String COLLAPSABLE_ROW = "collapsableRow";
    public static final String CONDITION_TEXT = "conditionText";

    /** Wait time in second for STB checkbox selection. */
    private static final int STB_CHECKBOX_SELECTION_WAIT = 2;

    /** Wait time in second for STB checkbox element to be displayed. */
    private static final int STB_CHECKBOX_ELEMENT_WAIT = 8;

    /** Predefined wait in second for ensuring the load of index page. */
    private static final int INDEX_PAGE_LOAD_COMPLETION_WAIT = 5;

    /** Timeout in second for verifying the presence of alert during deletion of tag. */
    private static final int TAG_DELETION_ALERT_PRESENCE_TIMEOUT = 2;

    /** Browser specific web driver. */
    private RemoteWebDriver driver = null;

    /** Driver specific wait. */
    private SeleniumWaiter seleniumWaiter = null;

    /** Login user. */
    private String userName = null;

    /**
     * Constructor to initialize login and browser info.
     *
     * @param  driver    Browser specific web driver.
     * @param  userName  Login user.
     */
    public IndexPage(RemoteWebDriver driver, String userName) {
        this.driver = driver;
        this.seleniumWaiter = new SeleniumWaiter(driver);
        this.userName = userName;
    }

    /**
     * Load the DAWG house index page.
     */
    public void load() {
        driver.get(TestServers.getHouse() + userName + "/");
        seleniumWaiter.waitForPresence(By.className(IndexPage.FILTERED_TABLE), INDEX_PAGE_LOAD_TIMEOUT);

        // Wait to ensure the index page load completely.
        SeleniumWaiter.waitTill(INDEX_PAGE_LOAD_COMPLETION_WAIT);
    }

    /**
     * Click the bulk tag based on the tag name provided.
     *
     * @param  tagName  tag name to be clicked.
     */
    public void clickOnTagCloudTagElement(String tagName) {
        WebElement tagCloudDivElement = getTagCloudTagDivElement(tagName);
        tagCloudDivElement.click();
        SeleniumWaiter.waitTill(BULK_TAG_CLICK_WAIT);
    }

    /**
     * Return the tag cloud div tag element class details which can be used for validating the
     * selection of tag.
     *
     * @param   tagName  tag name.
     *
     * @return  div tag elements class attribute value.
     */
    public String getTagCloudTagParentDivClassElement(String tagName) {
        return getTagCloudTagParentDivElement(tagName).getAttribute(TAG_CLOUD_PARENT_TAG_DIV_CLASS_ATTRIBUTE);
    }

    /**
     * Identify whether tag is not selected or not.
     *
     * @param   tagName  tag name which need to verified.
     *
     * @return  true if tag selected, false otherwise.
     */
    public boolean isTagSelectedInTagCloud(String tagName) {
        String selectedTagClassAttributeValue = getTagCloudTagParentDivClassElement(tagName);
        LOGGER.info("Class attribute of tag div element is : " + selectedTagClassAttributeValue);

        return selectedTagClassAttributeValue.contains(TAG_HIGHLIGHT_CLASS_ATTRIBUTE_VALUE);
    }

    /**
     * Is delete option displayed in tag element on tag cloud.
     *
     * @param   tagName  Name of the tag.
     *
     * @return  true if delete option is available on tag element.
     */
    public boolean isDeleteOptionDisplayedInTag(String tagName) {
        WebElement tagDeleteDivElement = getTagDeleteDivElement(tagName);
        String styleAtrributeValue = tagDeleteDivElement.getAttribute(TAG_DELETE_OPTION_STYLE_ATTRIBUTE);
        LOGGER.info("Style option of delete div : " + styleAtrributeValue);

        int opacityBeginningIndex = styleAtrributeValue.lastIndexOf(OPACITY_BEGINNING_CHAR) + 1;
        int opacityEndingIndex = styleAtrributeValue.lastIndexOf(OPACITY_ENDING_CHAR);
        String opacityStr = styleAtrributeValue.substring(opacityBeginningIndex, opacityEndingIndex);
        LOGGER.info("String Opacity value : " + opacityStr);

        double opacity = Double.parseDouble(opacityStr);

        return opacity > 0;
    }

    /**
     * Return the tag delete div element.
     *
     * @param   tagName  Name of the tag element.
     *
     * @return  delete div element of the tag.
     */
    public WebElement getTagDeleteDivElement(String tagName) {
        WebElement tagDivElement = getTagCloudTagDivElement(tagName);

        return tagDivElement.findElement(By.className(TAG_DELETE_DIV_ELEMENT_IDENTIFIER));
    }

    /**
     * Click on delete tag option and wait for the tag element to get removed.
     *
     * @param  tagName  Name of the tag element.
     */
    public void clickOnTagDeleteOptionAndConfirmDeletion(String tagName) {
        WebElement deleteDivTagElement = getTagDeleteDivElement(tagName);
        deleteDivTagElement.click();
        seleniumWaiter.waitForAlertPresence(TAG_DELETION_ALERT_PRESENCE_TIMEOUT);

        // Switching to alert and accepting it.
        Alert alert = driver.switchTo().alert();
        alert.accept();
        seleniumWaiter.waitForStaleness(deleteDivTagElement, TAG_DELETION_TIMEOUT);
    }

    /**
     * Get tag cloud tag div element corresponding to the tag name passed.
     *
     * @param   tagName  tag name.
     *
     * @return  web element
     */
    public WebElement getTagCloudTagDivElement(String tagName) {
        String tagCloudDivXpath = TAG_ID_DIV_XPATH.replace(REPLACEABLE_ELEMENT, tagName);

        return driver.findElementByXPath(tagCloudDivXpath);
    }

    /**
     * Get tag cloud tag parent div element.
     *
     * @param   tagName  tag name.
     *
     * @return  web element
     */
    public WebElement getTagCloudTagParentDivElement(String tagName) {
        String tagCloudParentDivXpath = TAG_ID_PARENT_DIV_XPATH.replace(REPLACEABLE_ELEMENT, tagName);

        return driver.findElementByXPath(tagCloudParentDivXpath);
    }

    /**
     * Return the tag cloud text corresponding to tag name passed.
     *
     * @param   tagName  tag name.
     *
     * @return  tag element text.
     */
    public String getTagCloudTagText(String tagName) {
        WebElement tagCloudDivElement = getTagCloudTagDivElement(tagName);

        return tagCloudDivElement.findElement(By.className(TAG_CLOUD_TAG_TEXT_ELEMENT_IDENTIFIER)).getText();
    }

    /**
     * Return the tag cloud tag element count.
     *
     * @param   tagName  tag name.
     *
     * @return  the tag count.
     */
    public int getTagCloudTagCount(String tagName) {
        String tagText = getTagCloudTagText(tagName);
        int countBeginningIndex = tagText.lastIndexOf('(') + 1;
        int countEndingIndex = tagText.lastIndexOf(')');
        String countStr = tagText.substring(countBeginningIndex, countEndingIndex);
        LOGGER.info("Tag cloud tag element count : " + countStr);

        return Integer.parseInt(countStr);
    }

    /**
     * Click the set-top filter row.
     *
     * @param  stbId  tag name to be clicked.
     */
    public void clickOnStbFilterTableRow(String stbId) {

        WebElement settopFilterRow = getStbFilterDivRowElement(stbId);
        settopFilterRow.click();

    }

    /**
     * Returns the stb filter row div element.
     *
     * @param   stbId  Set-top Id.
     *
     * @return  div tag element of the stb.
     */
    private WebElement getStbFilterDivRowElement(String stbId) {
        String settopFilterRowDivXpath = STB_FILTER_DIV_XPATH.replace(REPLACEABLE_ELEMENT, stbId);
        LOGGER.info("Resolved settop filter row div Xpath : " + settopFilterRowDivXpath);

        return driver.findElementByXPath(settopFilterRowDivXpath);
    }

    /**
     * Return the list of all div tag under filter table div element.
     *
     * @return  List of all div tags.
     */
    public List<WebElement> getStbFilteredTableSuccessorDivElements() {
        return driver.findElementsByXPath(FILTERED_TABLE_SUCCESSOR_DIV_ELEMENTS_XPATH);
    }

    /**
     * Return the list of all device id div element under filtered table div element.
     *
     * @return  the list of device Ids.
     */
    public List<String> getStbFilteredTableDivElementsDeviceIdAttribute() {
        List<String> deviceIds = new ArrayList<String>();

        for (WebElement div : getStbFilteredTableSuccessorDivElements()) {
            deviceIds.add(div.getAttribute(FILTERED_TABLE_DEVICE_ID_ATTRIBUTE));
        }

        return deviceIds;
    }

    /**
     * Select the set-top checkbox.
     *
     * @param  stbIds  ID of Set-top to be selected.
     */
    public void selectStbCheckboxes(String... stbIds) {

        if (null == stbIds) {
            throw new IllegalArgumentException("STB id is null. No STB id is provided for selection.");
        }

        WebElement stbCheckbox = null;

        for (String stbId : stbIds) {

            stbCheckbox = getStbCheckboxElement(stbId);

            // For scrolling.
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + stbCheckbox.getLocation().y + ")");
            SeleniumWaiter.waitTill(STB_CHECKBOX_ELEMENT_WAIT);

            if (!stbCheckbox.isSelected()) {
                stbCheckbox.click();
                SeleniumWaiter.waitTill(STB_CHECKBOX_SELECTION_WAIT);
            }
        }
    }

    /**
     * Provide the set-top check box element.
     *
     * @param   stbId  Set-top Id.
     *
     * @return  set-top checkbox element.
     */
    private WebElement getStbCheckboxElement(String stbId) {
        String stbCheckboxXpath = STB_FILTER_CHECKBOX_XPATH.replace(REPLACEABLE_ELEMENT, stbId);
        LOGGER.info("Resolved stb check box Xpath : " + stbCheckboxXpath);

        return driver.findElementByXPath(stbCheckboxXpath);
    }

    /**
     * Provides the selection status of STB check box element.
     *
     * @param   stbId  Set-top id for which the selection status to be returned.
     *
     * @return  true if the check box is selected, false otherwise.
     */
    public boolean isStbCheckboxElementSelected(String stbId) {
        return getStbCheckboxElement(stbId).isSelected();
    }

    /**
     * Validate all the STB check box displayed to user is in selected state.
     *
     * @return  true if all the displayed STB check boxes are checked, false if any of the displayed
     *          STB check box is not checked or none of the STB displayed.
     */
    public boolean isAllStbCheckboxesInFilteredTableSelected() {
        boolean isAllSelected = true;

        List<WebElement> listOfCheckboxes = getAllStbCheckboxesInFilteredTable();

        // Validation to see devices are displayed.
        if (null != listOfCheckboxes && listOfCheckboxes.size() > 0) {

            for (WebElement checkbox : listOfCheckboxes) {

                // If any of the check box not selected set false and break the loop.
                if (!checkbox.isSelected()) {
                    isAllSelected = false;

                    break;
                }
            }
        } else {

            // In case of no device displayed.
            isAllSelected = false;
        }

        return isAllSelected;
    }

    /**
     * Validate all the STB check box displayed to user is in deselected state.
     *
     * @return  true if all the displayed STB check boxes are checked, false if any of the displayed
     *          STB check box is in checked or no STB displayed.
     */
    public boolean isAllStbCheckboxesInFilteredTableDeselected() {
        boolean isAllDeselected = true;

        List<WebElement> listOfCheckboxes = getAllStbCheckboxesInFilteredTable();

        // Validation to see devices are displayed.
        if (null != listOfCheckboxes && listOfCheckboxes.size() > 0) {

            for (WebElement checkbox : listOfCheckboxes) {

                // If any of the check box is in selected state return false and break the loop.
                if (checkbox.isSelected()) {
                    isAllDeselected = false;

                    break;
                }
            }
        } else {

            // In case of no device displayed return false
            isAllDeselected = false;
        }

        return isAllDeselected;
    }

    /**
     * Provides all the list of STB check box element displayed.
     *
     * @return  list of bulk checkbox displayed
     */
    public List<WebElement> getAllStbCheckboxesInFilteredTable() {
        WebElement filterTableElement = driver.findElementByXPath(FILTERED_TABLE_DIV_ELEMENT_XPATH);

        return filterTableElement.findElements(By.xpath(BULK_CHECKBOX_INPUT_IDENTIFIER));
    }

    /**
     * Clears any of the Stb check box selection.
     */
    public void clearAnyStbCheckboxSelection() {
        selectAllStbCheckbox();
        clickToggleAllCheckbox();
    }

    /**
     * Select all of the stb check boxes.
     */
    public void selectAllStbCheckbox() {
        WebElement toggleButton = getToggleAllCheckboxElement();

        if (!toggleButton.isSelected()) {
            toggleButton.click();
        }
    }

    /**
     * Return the index page toggle button element.
     *
     * @return  The Index page toggle button.
     */
    private WebElement getToggleAllCheckboxElement() {
        return driver.findElementByXPath(TOGGLE_BUTTON_XPATH);
    }

    /**
     * Provide the status of selection on toggle all check box.
     *
     * @return  true if toggle all check box is checked, false otherwise.
     */
    public boolean isToggleAllCheckboxSelected() {
        return getToggleAllCheckboxElement().isSelected();
    }

    /**
     * Toggle the selection on toggle all check box element.
     */
    public void clickToggleAllCheckbox() {
        getToggleAllCheckboxElement().click();
        SeleniumWaiter.waitTill(TOGGLE_ALL_CHECKBOX_WAIT);
    }

    /**
     * Provides the set-top meta data span text content.
     *
     * @param   stbId             tag name to be clicked.
     * @param   spanCssClassName  Class name of span element.
     *
     * @return  Meta data span content.
     */
    public String getStbMetaDataSpanTextContent(String stbId, String spanCssClassName) {

        WebElement settopFilterRow = getStbFilterDivRowElement(stbId);
        LOGGER.info("Data Device ID : " + settopFilterRow.getAttribute("data-deviceId"));

        WebElement metaDataDivTag = settopFilterRow.findElement(By.className("metadata"));
        WebElement tagNameSpan = metaDataDivTag.findElement(By.cssSelector("span." + spanCssClassName));

        return tagNameSpan.getAttribute("textContent");
    }

    /**
     * Provides the stb meta tag text content.
     *
     * @param   stbId  Device Id.
     *
     * @return  the stb meta tag text content.
     */
    public String getStbMetaDataTagSpanTextContent(String stbId) {
        return getStbMetaDataSpanTextContent(stbId, "stbTags");
    }

    /**
     * Load index page and add the tag name provided with the list of device id passed.
     *
     * @param  tagName  tag name to be added.
     * @param  stbIds   stb Ids.
     */
    public void loadIndexPageAndAddTag(String tagName, String... stbIds) {
        load();
        addTag(tagName, stbIds);
    }

    /**
     * Creates tag with given list of device Ids.
     *
     * @param  tagName  Tag name to be added.
     * @param  stbIds   List of stb to be added together.
     */
    public void addTag(String tagName, String... stbIds) {
        selectStbCheckboxes(stbIds);
        enterTagNameAndClickSubmit(tagName);
    }

    /**
     * Types the tag name and clicks the bulk tag button.
     *
     * @param  tagName  the tag names
     */
    public void enterTagNameAndClickSubmit(String tagName) {

        // Enter the tag name.
        typeOnBulkTagTextElement(tagName);

        /**
         * If already tag is present in the tag cloud the element modification need to be evaluated for correct wait
         * time.
         **/
        String tagCloudElementText = getTagCloudElementText();
        boolean isTagPresent = false;
        List<WebElement> listOfContainedTag = new ArrayList<WebElement>();

        for (String tag : tagName.split(" ")) {

            if (tagCloudElementText.contains(tag)) {
                isTagPresent = true;
                listOfContainedTag.add(driver.findElementByXPath(TAG_ID_DIV_XPATH.replace(REPLACEABLE_ELEMENT, tag)));
            }
        }

        // Clicking on bulk tag add button.
        clickOnBulkTagAddButton();

        WebDriverWait wait = new WebDriverWait(driver, 20);

        // If tag is present waiting for it to get refreshed.
        if (isTagPresent) {

            for (WebElement element : listOfContainedTag) {
                wait.until(ExpectedConditions.stalenessOf(element));
            }
        }

        // Since multi tag addition is supported.
        for (String tag : tagName.split(" ")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(TAG_ID_DIV_XPATH.replace(
                                REPLACEABLE_ELEMENT,
                                tag))));
        }
    }

    /**
     * Type the tag name on text input element field.
     *
     * @param  tagName  Name of tag to be entered on text input field.
     */
    public void typeOnBulkTagTextElement(String tagName) {
        WebElement elementTagText = getBulkTagTextElement();

        // Clearing the text area before entering.
        elementTagText.clear();
        elementTagText.sendKeys(tagName);
    }

    /**
     * Provides the bulk tag text element.
     *
     * @return  BulK tag text element field.
     */
    private WebElement getBulkTagTextElement() {
        return driver.findElement(By.id(BULK_TAG_TEXT_ID));
    }

    /**
     * Provides the bulk tag text element attribute value.
     *
     * @return  Attribute value of the text element.
     */
    public String getBulkTagTextElementValue() {
        return getBulkTagTextElement().getAttribute(VALUE_ATTRIBUTE_ELEMENT);
    }

    /**
     * Click on bulk tag add button.
     */
    public void clickOnBulkTagAddButton() {
        driver.findElement(By.id(BULK_TAG_BTN_ID)).click();
    }

    /**
     * Refresh the index page and return the tag cloud element.
     *
     * @return  the tag cloud web element
     */
    public WebElement refreshPageAndGetTagCloudElement() {
        load();

        WebElement element = driver.findElement(By.id(TAG_CLOUD_ID));

        return element;
    }

    /**
     * Return the tag cloud element.
     *
     * @return  tag cloud element text.
     */
    public String getTagCloudElementText() {
        WebElement element = driver.findElement(By.id(TAG_CLOUD_ID));

        return element.getText();
    }

    /**
     * Refresh the index page and return the tag cloud element.
     *
     * @param   user  login user name.
     *
     * @return  tag cloud element text.
     */
    public String refreshPageAndGetTagCloudElementText() {
        return refreshPageAndGetTagCloudElement().getText();
    }

}
