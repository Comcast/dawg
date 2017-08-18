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
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.MetaStbBuilder;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgIndexPageHelper;
import com.comcast.dawg.helper.DawgTagCloudHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.dawg.utils.DawgCommonUIUtils;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Glue file for checking Dawg-house's Bulk Tag UI behaviors
 * @author priyanka.sl 
 */
public class DawgBulkTagGlue {

    /**
     * Select an STB has no tags
     * @param option
     * @throws DawgTestException 
     */
    @Given("^I have a box that has (.*) (?:tags|tag)$")
    public void selectABoxHasNoTags(String option) throws DawgTestException {
        String testStb = null;
        List<String> testSTBTags = null;
        if ("no".equals(option)) {
            //Get the test STB which has no tags.
            testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_WIHTOUT_TAG);
        } else if ("one".equals(option)) {
            //Get the test STB which has 1 tag
            testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_WITH_EXISTING_TAG1);
            testSTBTags = DawgCommonUIUtils.getInstance().getStbMetaDataTags(testStb);
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_SELECTED, testSTBTags.get(0));
        } else {
            throw new DawgTestException("Invalid type" + option);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_SELECTED, testStb);
    }

    /**
     * Add an STB to existing/same tag in tag cloud
     * @param tagType
     * @throws DawgTestException 
     */
    @When("^I add the box to (?:an|the) (.*) tag$")
    public void tagBoxToExistingTag(String tagType) throws DawgTestException {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        String tagName = null;
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        switch (tagType) {
            case "existing":
                tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG1);
                break;
            case "same":
                tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_SELECTED);
                break;
            case "another existing":
                tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG2);
                break;
            default:
                throw new DawgTestException("Invalid tag type" + tagType);
        }
        SeleniumImgGrabber.addImage();
        //Check whether the tag going to add is available in the tag cloud
        Assert.assertTrue(tagWithStbCount.containsKey(tagName),
            "Failed to find " + tagType + " tag" + tagName + "in tag cloud");
        DawgTagCloudHelper.getInstance().addTagFromIndexPage(tagName, testStb);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_ADDED, tagName);
        List<String> testSTBTags = DawgCommonUIUtils.getInstance().getStbMetaDataTags(testStb);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_WITH_STBS, testSTBTags);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_COUNT, tagWithStbCount.get(tagName));
    }

    /**
     * Verify STB added to new/existing tags
     * @param tagType - new/existing
     * @throws DawgTestException 
     */
    @Then("^I should see the box is added to (.*) tag in tag cloud$")
    public void verifyStbTaggedToTags(String tagType) throws DawgTestException {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        String tagAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_ADDED);
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        boolean stbCountCheck = false;
        // Get the back end data   
        int currentStbCountInTag = tagWithStbCount.get(tagAdded);
        List<String> testSTBTags = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_WITH_STBS);
        if ("new".equals(tagType)) {
            stbCountCheck = (currentStbCountInTag == 1);
        } else if ("existing".equals(tagType)) {
            int initialStbCountInTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_COUNT);
            stbCountCheck = (currentStbCountInTag == initialStbCountInTag + 1);
        } else {
            throw new DawgTestException("Invalid tag type" + tagType);
        }
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(
            !testSTBTags.isEmpty() && testSTBTags.contains(tagAdded) && tagWithStbCount.containsKey(tagAdded) && stbCountCheck,
            "Test STB " + testStb + "is not added to " + tagType + " tag in tag cloud");
    }

    /**
    * Add an STB to new tag in tag cloud 
    */
    @When("^I add the box to a new tag$")
    public void addTonewTag() {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        String tagName = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
        SeleniumImgGrabber.addImage();
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        Assert.assertFalse(tagWithStbCount.containsKey(tagName),
            "Selected tag " + tagName + " is not a new one , Tag already present in tag cloud");
        DawgTagCloudHelper.getInstance().addTagFromIndexPage(tagName, testStb);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_ADDED, tagName);
        List<String> testSTBTags = DawgCommonUIUtils.getInstance().getStbMetaDataTags(testStb);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_WITH_STBS, testSTBTags);
    }

    /**
     * Verify box tagged with tags in tag cloud
     * @param count - tag count in which stb tagged    
     */
    @Then("^the box is tagged with (\\d+) (?:tags|tag) in tag cloud$")
    public void verifyBoxTagged(int count) {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        List<String> testSTBTags = DawgCommonUIUtils.getInstance().getStbMetaDataTags(testStb);
        //Verify delete option appears in tags in which stbs added.
        List<String> tagsWithDelOption = DawgTagCloudHelper.getInstance().getTagsInDeleteOptionAppears();
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(
            testSTBTags.size() == count && tagsWithDelOption.size() == count && testSTBTags.containsAll(tagsWithDelOption),
            "Box is not tagged with" + count + "tags");
    }

    /**
     * Tag a box with different tags
     * @param count - tag count in which stb tagged
     */
    @When("I tag the box with (\\d+) different tags$")
    public void tagStbsWithNTags(int count) {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        for (int i = 0; i < count; i++) {
            String tag = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
            DawgTagCloudHelper.getInstance().addTagFromIndexPage(tag, testStb);
        }
        SeleniumImgGrabber.addImage();
    }

    /**
     * Verify box added remains same in tag cloud
     */
    @When("I should see the box added remains same in tag cloud$")
    public void boxRemainsSameInTag() {
        //Get the previously added tags
        List<String> tagsInSTBsAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_WITH_STBS);
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        DawgTagCloudHelper.getInstance().checkOrUncheckSTB(testStb, DawgHouseConstants.CHECK);
        //Verify delete option appears in tags  in which stbs added.
        List<String> tagsWithDelOption = DawgTagCloudHelper.getInstance().getTagsInDeleteOptionAppears();
        Assert.assertTrue(tagsInSTBsAdded.containsAll(tagsWithDelOption), "Box added is not remains same in tag cloud");
    }

    /**
     * Tag multiple boxes to new/existing tag
     * @param - tagType 
     */
    @When("I tag multiple boxes to (?:a|an) (.*) tag$")
    public void tagMultipleBoxes(String tagType) {
        // Get any two test STBs created
        List<String> stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_WITH_EXISTING_TAG2);
        String tagtoAdd = null;
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        if ("new".equals(tagType)) {
            tagtoAdd = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
            //Check the tag going to add is already exists in tag cloud
            Assert.assertFalse(tagWithStbCount.containsKey(tagtoAdd),
                "Tag " + tagtoAdd + " is already exists in tag cloud");
        } else if ("existing".equals(tagType)) {
            tagtoAdd = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG1);
            Assert.assertTrue(tagWithStbCount.containsKey(tagtoAdd),
                "Selected tag " + tagtoAdd + " is not an exisiting tag in tag cloud");
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_COUNT, tagWithStbCount.get(tagtoAdd));
        }
        DawgTagCloudHelper.getInstance().addTagFromIndexPage(tagtoAdd, stbs.toArray(new String[stbs.size()]));
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_SELECTED, stbs);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_ADDED, tagtoAdd);
    }

    /**
     * Verify multiple boxes added to new/existing tags
     * @param - tagType    
     */
    @When("I should see all the boxes added to the (.*) tag$")
    public void verifyBoxesAdded(String tagType) {
        // Verify all STBs are tagged to new tag
        List<String> stbsTagged = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        String tagAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_ADDED);
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        //Verify delete option appears in tag/s  in which stbs added.
        List<String> tagsWithDelOption = DawgTagCloudHelper.getInstance().getTagsInDeleteOptionAppears();
        int currentStbCountInTag = tagWithStbCount.get(tagAdded);
        int initialStbCountInTag = 0;
        if ("existing".equals(tagType)) {
            initialStbCountInTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_COUNT);
        }
        Assert.assertTrue(
            tagsWithDelOption.contains(tagAdded) && currentStbCountInTag == initialStbCountInTag + stbsTagged.size(),
            "Test STBs  not added to " + tagType + " tag in tag cloud");
    }

    /**
     * Add a tag without selecting the box 
     */
    @When("I add a tag without selecting the box$")
    public void addATagWithOutSelectingBox() {
        // Choose a tag name to add
        String tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_EXISTING_TAG1);       
        Assert.assertTrue(DawgTagCloudHelper.getInstance().enterTagNameAndSubmitBulkTagBtn(tagName),
            "Failed to enter tag name and submit bulk tag button");
    }

    /**
     * Verify alert message
     * @param - message alert message to verify 
     */
    @Then("I should see the alert message '(.*)' on screen$")
    public void verifyAlertMessage(String message) {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);       
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isAlertPresent(), "No alert message displayed ");
        Alert deleteAlert = driver.switchTo().alert();
        // Validates the alert message.
        Assert.assertEquals(deleteAlert.getText(), message,
            "The alert message displayed is not an expected one." + message);
        // Accept the alert message to avoid the 'UnhandledAlertException'
        deleteAlert.accept();
    }

}
