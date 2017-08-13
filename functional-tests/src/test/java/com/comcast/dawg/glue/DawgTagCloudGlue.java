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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Glue file for checking Dawg-house's Tag Cloud UI behaviors
 * @author priyanka.sl 
 */
public class DawgTagCloudGlue {

    /**
     * Verify tag element is highlighted or not
     */
    @Then("^I should see (.*) tag (?:elements|element) highlighted$")
    public void verifyTagHighlight(String type) {
        StringBuilder tagsNotHighlighted = new StringBuilder();
        List<String> tagsToCheckHighlight = new ArrayList<String>();
        if ("both".equals(type)) {
            tagsToCheckHighlight.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
            tagsToCheckHighlight.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString());
        } else if ("the".equals(type) || "first".equals(type)) {
            tagsToCheckHighlight.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
        }
        SeleniumImgGrabber.addImage();
        // Verify selected tag is highlighted or not
        for (String tag : tagsToCheckHighlight) {
            if (!DawgTagCloudHelper.getInstance().isTagHighLighted(tag)) {
                tagsNotHighlighted.append(tag).append(",");
            }
        }
        Assert.assertTrue(tagsNotHighlighted.toString().length() == 0,
            "Tag/s " + tagsNotHighlighted + " not displayed as highlighted");
    }

    /**
     * De-select the first/second tag element
     * @param type - first/second
     */
    @When("^I de-select (.*) tag element$")
    public void deselectTag(String type) {
        String tagToDeselect = null;
        if (type.contains("first")) {
            tagToDeselect = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1);
        } else if ("second".equals(type)) {
            tagToDeselect = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2);
        }
        Assert.assertTrue(DawgTagCloudHelper.getInstance().deSelectTag(tagToDeselect),
            "Failed to de-select the tag" + tagToDeselect);
    }

    /**
     * Verify highlight remains in first/second/selected tag element
     * @param type - first/second/selected
     */
    @Then("^highlight remains on (.*) tag element$")
    public void highlightRemains(String type) {
        String selectedTag = null;
        if ("second".equals(type)) {
            selectedTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2);
        } else if (type.contains("selected") || "first".equals(type)) {
            selectedTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1);
        }
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(DawgTagCloudHelper.getInstance().isTagHighLighted(selectedTag),
            "Tag " + selectedTag + " is not highlighted");
    }


    /**
     * Verify the STB list displayed for the selected tag in filter table
     * @param type - first/second/both/two/delete
     */
    @Then("^(?:I should see|the) STB list for (.*) (?:selected|tag element remains same)$")
    public void verifyStbLists(String type) throws DawgTestException {
        List<String> stbLists = new ArrayList<String>();
        if (type.contains("first") || type.equals("the tag")) {
            // Get the test STBs added for the first tag
            stbLists = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        } else if (type.contains("second")) {
            // Get the test STBs added for the second tag
            stbLists = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2);
        } else if (type.contains("both") || type.contains("two")) {
            // Get the test STBs added for selected tags( both tags/ two tags)
            List<String> tag1Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
            List<String> tag2Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2);
            stbLists.addAll(tag1Stbs);
            stbLists.addAll(tag2Stbs);
        } else if (type.contains("delete")) {
            // Get the test STBs added for the test tag delete(to perform delete stb content from tag)
            stbLists = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG1_STBS);
        } else {
            throw new DawgTestException("Invalid type" + type);
        }
        SeleniumImgGrabber.addImage();
        List<String> stbListDisplayed = DawgTagCloudHelper.getInstance().getSTBLists();
        //Check whether the test STBs added for the test tag is same as that
        //of the stb list displayed while selecting the specified tag/s       
        Assert.assertTrue(
            !stbListDisplayed.isEmpty() && stbListDisplayed.containsAll(stbLists) && stbLists.size() == stbListDisplayed.size(),
            "Failed to find the STB lists for the selected tag");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE, stbListDisplayed);
    }

    /**
     * Verify the STB list disappears from the filter table 
     */
    @Then("^I should see the STB list for first tag element is removed$")
    public void verifyStbListVanished() {
        StringBuilder stbsDisplayed = new StringBuilder();
        List<String> stbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(!stbLists.isEmpty(), "Failed to find STB lists in the filter table");
        // Verify STB lists does not contain any STBs of tag1
        List<String> tag1Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        for (String stb : tag1Stbs) {
            if (stbLists.contains(stb)) {
                stbsDisplayed.append(stb).append(",");
            }
        }
        Assert.assertTrue(stbsDisplayed.toString().length() == 0,
            "Stb/s " + stbsDisplayed + " not removed from STB list");
    }

    /**
     * Verify all STB details displayed in the filter table
     */
    @Then("^I should see all available STBs in Dawg House$")
    public void verifyAllStbs() {
        // Verify all STBs displayed
        List<String> previousSTBList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE);
        List<String> currentStbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(currentStbLists.size() > previousSTBList.size(), "Failed to see all STBs in dawg house");
    }

    /**
     * Verify highlight removed from specified tag element
     * @param type - first/second/both tag elements
     * @throws DawgTestException 
     */
    @Then("^I should see highlight removed from (.*) tag (?:element|elements)$")
    public void verifyHighLightRemoved(String type) throws DawgTestException {
        StringBuilder tagsHighlighted = new StringBuilder();
        List<String> tagsToCheck = new ArrayList<String>();
        if ("first".equals(type)) {
            tagsToCheck.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
        } else if ("second".equals(type)) {
            tagsToCheck.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString());

        } else if ("both".equals(type)) {
            tagsToCheck.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
            tagsToCheck.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString());
        } else {
            throw new DawgTestException("Invalid type " + type);
        }
        // Verify highlight removed
        for (String tag : tagsToCheck) {
            if (DawgTagCloudHelper.getInstance().isTagHighLighted(tag)) {
                tagsHighlighted.append(tag).append(",");
            }
        }
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(tagsHighlighted.toString().length() == 0,
            "Tag/s " + tagsHighlighted + " displayed as highlighted");
    }

    /**
     * Select a tag which contains same STB list as that of first tag selected 
     */
    @Then("^I select another tag element which has same STB list in first tag$")
    public void selectThirdTag() {
        //Select test tag3 as it contains the same STB list of test tag1
        String tagName = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG3);
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectTag(tagName), "Failed to select the tag" + tagName);
        // Get the STB lists from filter table
        List<String> stbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        // Check whether the tag contains same lists of stbs as in first tag
        //Get stbs in first tag selected
        List<String> tag1Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        Assert.assertTrue(stbLists.equals(tag1Stbs),
            "STB lists of selected tag is not same as that of stb list in first tag");
    }

    /**
     * Select two tag elements with same stb list      
     */
    @Given("^I selected two tag elements having same STB list$")
    public void selectTagsWithSameStbs() {
        List<String> tagNames = new ArrayList<String>();
        List<String> stbLists = new ArrayList<String>();
        StringBuilder message = new StringBuilder();
        tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString());
        //Test tag3 contains same list of stbs as in tag1
        tagNames.add(TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG3).toString());
        // check the tags are selected or not
        for (String tagName : tagNames) {
            if (!DawgTagCloudHelper.getInstance().selectTag(tagName)) {
                message.append(tagName).append(",");
            }
        }
        Assert.assertTrue((0 == message.toString().length()), String.format("Failed to select Tag(%s).", message));

        // Get the STB lists in the tags and keep in text context
        List<String> tag1Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        List<String> tag3Stbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG3_WITH_STB_SET1);
        stbLists.addAll(tag1Stbs);
        stbLists.addAll(tag3Stbs);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE, stbLists);
    }

    /**
     * Verify STB list displayed remains same
     */
    @Then("^(?:I should see|the) STB list (?:displayed remains|of both tags are) same$")
    public void stbRemainsSame() {
        List<String> previousSTBList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE);
        List<String> currentStbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(previousSTBList.containsAll(currentStbLists), "STB lists dsplayed does not remains same");

    }

    /**
     * Delete an stb content from a tag or delete a tag 
     * @param -tagtype    
     */
    @When("^I delete (?:any one of the|the selected STB content from) (.*) tag$")
    public void deleteTagorSTBContent(String tagType) {
        String tagSelected = null;
        if ("first".equals(tagType)) {
            //Here going to delete test tag from tag cloud
            tagSelected = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG1);
        } else if ("selected".equals(tagType)) {
            //Here going to delete stb content from a tag 
            tagSelected = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG3);
        }
        //select the delete option in tag
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectDeleteOptionInTag(tagSelected),
            "Failed to select delete option displayed in tag" + tagSelected);
        DawgTagCloudHelper.getInstance().acceptDeleteTagAlert();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_SELECTED, tagSelected);
       
    }

    /**
     * Select test tag having multiple STBs to perform delete tag operation
     * @param - tagtype   
     */
    @Given("^I selected one tag having multiple tagged STBs$")
    public void selectATagToPerformDeleteTag() {
        String tagOne = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG1);
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectTag(tagOne),
            String.format("Failed to select Tag(%s).", tagOne));
        // Verify more than one STBs present.
        List<String> stbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(1 < stbLists.size(), "Failed to find multiple STBs in tag" + tagOne);
        // Get the tag information from tag cloud and keep in text context for further validations.
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_COUNT, tagWithStbCount.get(tagOne));
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE,
            DawgTagCloudHelper.getInstance().getSTBLists());
    }

    /**
     * Select N no.of STBs to tag to a new tag    
     */
    @Given("^I tag (\\d+) STB to a new tag$")
    public void tagStbToNewTag(int count) {
        List<String> stbLists = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE);
        // Verify the count to add does not exceed the STB list         
        Assert.assertTrue(
            stbLists.size() >= count,
            "STB tag operation is not possible . Current STB list size " + stbLists.size() + " STB count to tag" + count);
        // Add a new tag via UI
        String newTestTagName = MetaStbBuilder.getUID(TestConstants.TAG_NAME_PREF);
        List<String> stbsToTag = stbLists.subList(0, count);
        // Add new test tag
        DawgTagCloudHelper.getInstance().addTagFromIndexPage(newTestTagName,
            stbsToTag.toArray(new String[stbsToTag.size()]));
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_ADDED, newTestTagName);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_COUNT, count);
    }

    /**
     * Verify STB added to a new tag in tag cloud
     */
    @Then("^I should see the STB added to new tag in tag cloud$")
    public void verifyNewTagAddedInTagCloud() {
        //Get the newly added tag in tag cloud.
        String newTagAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_ADDED);
        int stbCountAdded = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_COUNT);
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        // Verify the newly added tag contains the tagged STBs in the tag cloud 
        Assert.assertTrue(tagWithStbCount.containsKey(newTagAdded),
            "Failed to find newly added tag " + newTagAdded + "in tag cloud");
        Assert.assertTrue(tagWithStbCount.get(newTagAdded) == stbCountAdded,
            "Failed to find STB count added " + stbCountAdded + "for new tag " + newTagAdded + "in tag cloud");
    }

    /**
     * Select a test tag with one STB, which is already tagged in the first tag selected     
     */
    @Then("^I select another tag having one STB which is tagged in the first tag$")
    public void selectTagWithOneSTB() {
        String tag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG2);
        List<String> tagSTbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG2_STBS);
        //Verify only one STB present
        Assert.assertTrue(tagSTbs.size() == 1, "More than one STBs available in the selected tag");
        //select the tag
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectTag(tag),
            String.format("Failed to select Tag(%s).", tag));
        List<String> initialList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE);
        List<String> currentSTBList = DawgTagCloudHelper.getInstance().getSTBLists();
        // Verify the initial STB list is not changed after selecting the tag
        Assert.assertTrue(initialList.containsAll(currentSTBList),
            String.format("selectd tag(%s) doesnot contain an STB which is tagged in the first tag.", tag));

    }

    /**
     * Select the specified stb from the stb list displayed
     * @param type   
     */
    @When("^I select (.*) STB from the STB list displayed$")
    public void selectCommonSTB(String type) {
        List<String> tagSTbs = null;
        if (("common").equals(type)) {
            //Get the common STB
            tagSTbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG2_STBS);
        } else if ("one".equals(type)) {
            tagSTbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        }//select STB
        DawgTagCloudHelper.getInstance().checkOrUncheckSTB(tagSTbs.get(0), DawgHouseConstants.CHECK);
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(DawgTagCloudHelper.getInstance().isStbSelected(tagSTbs.get(0)),
            "Failed to select the STB check box");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_SELECTED, tagSTbs.get(0));
    }

    /**
     * Verify delete option appears in test tags (to perform tag delete)     
     */
    @Then("^I should see delete option appears in both tags$")
    public void verifyDelOptionAppears() {
        String firstTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG1);
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(firstTag),
            "Failed to verify delete option displayed in tag" + firstTag);
        String secondTag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_DELETE_TAG2);
        //Verify delete option appears.
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(secondTag),
            "Failed to verify delete option displayed in tag" + secondTag);
    }

    /**
     * Verify STB content removed from the selected tag in tag cloud 
     */
    @Then("^I should see the STB removed from the tag in tag cloud$")
    public void verifySTBContentRemoved() {
        String tag = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_SELECTED);
        //Verify STB content removed from first tag 
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        // Get the initial tag count
        int initialSTBCount = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_COUNT);
        int currenCount = tagWithStbCount.get(tag);
        Assert.assertTrue(currenCount == initialSTBCount - 1, "Stb content is not removed from the tag" + tag);
    }

    /**
     * Select all the STBs displayed in the filter table
     */

    @When("^I select all the STBs displayed$")
    public void selectAllStbs() {
        StringBuilder stbsNotSelected = new StringBuilder();
        List<String> stbsDisplayed = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_LIST_IN_FILTER_TABLE);
        for (String stb : stbsDisplayed) {
            DawgTagCloudHelper.getInstance().checkOrUncheckSTB(stb, DawgHouseConstants.CHECK);
            if (!DawgTagCloudHelper.getInstance().isStbSelected(stb)) {
                stbsNotSelected.append(stb).append(",");
            }
        }
        Assert.assertEquals(stbsNotSelected.toString().length(), 0, "failed to select STB/s" + stbsNotSelected);
    }

    /**
     * Verify tag removed from tag cloud    
     */
    @Then("^I should see the tag removed from tag cloud$")
    public void veifyTagRemoved() {
        String tagRemoved =  TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG_SELECTED);
        //Get the current list of tags from tag cloud
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        SeleniumImgGrabber.addImage();
        //Verify tag removed from tag cloud.
        Assert.assertFalse(tagWithStbCount.containsKey(tagRemoved),
            "Tag " + tagRemoved + " is not removed from tag cloud");
    }

    /**
     * Delete an STB content from tag      
     */
    @When("^I delete an STB from the tag element$")
    public void deleteSTB() {
        String tagSelected = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1);
        //Before deleting an STB from tag gets the details of tag from tag cloud
        Map<String, Integer> tagWithStbCount = DawgTagCloudHelper.getInstance().getTagsFromTagCloud();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_COUNT, tagWithStbCount.get(tagSelected));

        // Select one STB from the STB list displayed.
        List<String> stbList = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(!stbList.isEmpty(), "STB list not displayed in filter table");
        String stbToSelect = stbList.get(0);
        DawgTagCloudHelper.getInstance().checkOrUncheckSTB(stbToSelect, DawgHouseConstants.CHECK);
        Assert.assertTrue(DawgTagCloudHelper.getInstance().isStbSelected(stbToSelect),
            "Failed to select STB " + stbToSelect);
        // check delete option appears in the tag
        Assert.assertTrue(DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(tagSelected),
            "Delete option not displayed in tag" + tagSelected);
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectDeleteOptionInTag(tagSelected),
            "Failed to select delete option in tag" + tagSelected);
        DawgTagCloudHelper.getInstance().acceptDeleteTagAlert();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_SELECTED, stbToSelect);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_TAG_SELECTED, tagSelected);
    }

    /**
     * Verify the checked status of STB    
     */
    @When("^the checked status of selected STB remains same$")
    public void verifySTBChecked() {
        // Get selected STB
        String stbSelected = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        Assert.assertTrue(DawgTagCloudHelper.getInstance().isStbSelected(stbSelected),
            "STB " + stbSelected + " not displayed as checked");
    }

    /**
     * Verify delete option appears/disappears for test tags
     * @param type 
     * @throws DawgTestException 
     */
    @Then("^delete option (.*) (?:in both tags|for tag1 and tag3|from tag1 and tag3)$")
    public void verifyDeleteOptionDisplayed(String type) throws DawgTestException {
        // verify delete option appears for tag1 and tag3 
        boolean delOptionDisplayed = false;
        String tag1 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1);
        String tag3 = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG3);
        delOptionDisplayed = DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(tag1) && DawgIndexPageHelper.getInstance().isDeleteOptionDisplayedInTag(
            tag3);
        SeleniumImgGrabber.addImage();
        if ("appears".equals(type)) {
            Assert.assertTrue(delOptionDisplayed, "Delete option not displayed in tag");
        } else if ("disappears".equals(type)) {
            Assert.assertFalse(delOptionDisplayed, "Delete option displayed in tag");
        } else {
            throw new DawgTestException("Invalid option" + type);
        }
    }

    /**
     * Verify delete option appears/disappears for test tags
     * @param firstTag
     * @param secondTag
     * @param type 
     * @throws DawgTestException 
     */
    @Then("^I (?:select|selected) (.*) and (.*) (?:which has|with) (.*) STB list$")
    public void tagwithsameSTB(String firstTag, String secondTag, String type) throws DawgTestException {

        String tagOne = null, tagTwo = null;
        if ("tag1".equals(firstTag) && "tag2".equals(secondTag)) {
            // Get tag1 and tag2 with same STB list
            tagOne = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString();
            tagTwo = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG2).toString();
        } else if ("tag1".equals(firstTag) && "tag3".equals(secondTag)) {
            // Get tag1 and tag3 with different STB list
            tagOne = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG1).toString();
            tagTwo = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_TAG3).toString();
        } else {
            throw new DawgTestException("Invalid tag types" + firstTag + "," + secondTag);
        }
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectTag(tagOne), "Failed to select tag " + tagOne);
        List<String> tagOneStbList = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(DawgTagCloudHelper.getInstance().selectTag(tagTwo), "Failed to select tag " + tagTwo);
        // get the STB list displayed
        List<String> stbListDisplayed = DawgTagCloudHelper.getInstance().getSTBLists();
        if ("same".equals(type)) {
            // Verify STB lists of tag1 and tag3 are same           
            Assert.assertTrue(tagOneStbList.containsAll(stbListDisplayed),
                " STB details of tag1" + tagOne + " and tag3" + tagTwo + "are not same");
        } else if ("different".equals(type)) {
            // Verify STB lists of tag1 and tag2 are different 
            List<String> tagTwoStbList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2);
            Assert.assertTrue(stbListDisplayed.containsAll(tagTwoStbList) && !tagTwoStbList.containsAll(tagOneStbList),
                "Same STB details found in tag1" + tagOne + " and tag2" + tagTwo);
        }
    }


    /**
     * Verify STB list displayed for all test tags selected tag1 tag2 tag3 
     */
    @Then("^I should see the STB list displayed for all selected tags$")
    public void verifySTBList() {
        List<String> stbsAdded = new ArrayList<String>();
        // Get the test STBs added for tag1 tag2 tag3
        List<String> tag1STBs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
        stbsAdded.addAll(tag1STBs);
        List<String> tag2STBs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG2_WITH_STB_SET2);
        stbsAdded.addAll(tag2STBs);
        List<String> tag3STBs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG3_WITH_STB_SET1);
        stbsAdded.addAll(tag3STBs);
        //Get the current STB list displayed in filter table after selecting the tag1,tag2 tag3
        List<String> stbLists = DawgTagCloudHelper.getInstance().getSTBLists();
        Assert.assertTrue(stbsAdded.containsAll(stbLists), "Failed to see the STB list displayed ");
    }

    /**
     * Select one STB belongs to the specified tag
     * @param tagType     
     * @throws DawgTestException 
     */
    @When("^I select one STB which belongs to (.*)$")
    public void selectSTB(String tagType) throws DawgTestException {
        List<String> tagStbs = null;
        String stbToSelect = null;
        if ("tag1".equals(tagType)) {
            tagStbs = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TAG1_WITH_STB_SET1);
            //Select 1 STB from tag1
            stbToSelect = tagStbs.get(0);
            DawgTagCloudHelper.getInstance().checkOrUncheckSTB(stbToSelect, DawgHouseConstants.CHECK);
            Assert.assertTrue(DawgTagCloudHelper.getInstance().isStbSelected(stbToSelect),
                "Failed to select tag" + stbToSelect);
        } else {
            throw new DawgTestException("Invalid tag type" + tagType);
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_STB_SELECTED, stbToSelect);
    }

    /**
     * Remove the selection from selected STB 
     */
    @When("^I remove the selection from selected STB$")
    public void removeTagSelection() {
        String stbSelected = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_STB_SELECTED);
        //Remove the selection from STB
        DawgTagCloudHelper.getInstance().checkOrUncheckSTB(stbSelected, DawgHouseConstants.UNCHECK);
        //Verify STB displayed as unchecked
        Assert.assertTrue(!DawgTagCloudHelper.getInstance().isStbSelected(stbSelected),
            "Failed to uncheck STB" + stbSelected);
    }
}
