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

import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.constants.TestGroups;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.dawg.test.base.IndexPageUITestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The UI Testing for Dawg house's Tag Cloud.
 *
 * @author  Pratheesh T.K.
 */
public class TagCloudUIIT extends IndexPageUITestBase {

    /** First tag required for entire test. */
    private String existingTagOneName = null;

    /** Second tag required for entire test. */
    private String existingTagTwoName = null;

    /** Tag holding STBs of first tag created. */
    private String existingTagThreeWithTagOneStbs = null;

    /** List of unique STB ids belong to tag one. */
    private String[] existingTagOneUniqueStbIds = null;

    /** List of unique STB ids belong to tag two. */
    private String[] existingTagTwoUniqueStbIds = null;

    /** Tag required for deletion of tag test cases. */
    private String existingDeletionTagOneName = null;

    /** Tag required for deletion of tag test cases. */
    private String existingDeletionTagTwoName = null;

    /** Tag required for tag selection and deleting of a STB from tag. */
    private String existingDeletionTagThreeName = null;

    /** Holds list of STB id available on deletion tag one. */
    private String[] existingDeletionTagOneStbIds = null;

    /** Hold one of the STB id shared among deletion tag one and two. */
    private String existingDeletionTagTwoStbId = null;

    /** Holds list of STB id available on deletion tag three. */
    private String[] existingDeletionTagThreeStbIds = null;

    @BeforeTest(
        description = "Before test method for adding prerequisite tags for test.",
        groups = TestGroups.EXISTING_TAG_NEEDED_GROUP
    )
    protected void addRequiredExistingTagData() {

        try {
            existingTagOneName = createDynamicTestTagNames();
            existingTagOneUniqueStbIds = createRequestedTestStbs(TestConstants.UNIQUE_TAG_ONE_STB_COUNT);
            addTagUsingRestRequest(existingTagOneName, existingTagOneUniqueStbIds);
            bufferTheTagsToBeCleared(existingTagOneName, existingTagOneUniqueStbIds);

            existingTagTwoName = createDynamicTestTagNames();
            existingTagTwoUniqueStbIds = createRequestedTestStbs(TestConstants.UNIQUE_TAG_TWO_STB_COUNT);
            addTagUsingRestRequest(existingTagTwoName, existingTagTwoUniqueStbIds);
            bufferTheTagsToBeCleared(existingTagTwoName, existingTagTwoUniqueStbIds);

            // Creation of third tag which hold the STBs of first added tag.
            existingTagThreeWithTagOneStbs = createDynamicTestTagNames();

            // Adding the list of STBs on tag one to new tag.
            addTagUsingRestRequest(existingTagThreeWithTagOneStbs, existingTagOneUniqueStbIds);
            bufferTheTagsToBeCleared(existingTagThreeWithTagOneStbs, existingTagOneUniqueStbIds);
        } catch (IOException e) {
            Assert.fail(String.format("Failed to add tags(%s,%s,%s) in the precondition of the test.",
                        existingTagOneName, existingTagTwoName, existingTagThreeWithTagOneStbs), e);
        }
    }

    @BeforeTest(
        description = "Before test method for adding prerequisite deletion tags for test.",
        groups = TestGroups.DELETION_TAG_REQUIRED_GROUP
    )
    protected void addRequiredDeletionTagData() {

        try {
            existingDeletionTagOneName = createDynamicTestTagNames();
            existingDeletionTagOneStbIds = createRequestedTestStbs(TestConstants.DELETE_TAG_ONE_STB_COUNT);
            addTagUsingRestRequest(existingDeletionTagOneName, existingDeletionTagOneStbIds);
            bufferTheTagsToBeCleared(existingDeletionTagOneName, existingDeletionTagOneStbIds);

            // A new tag creation which hold the STB of previously added tag.
            if (null != existingDeletionTagOneStbIds && existingDeletionTagOneStbIds.length > 0) {
                existingDeletionTagTwoName = createDynamicTestTagNames();
                existingDeletionTagTwoStbId = existingDeletionTagOneStbIds[0];
                addTagUsingRestRequest(existingDeletionTagTwoName, existingDeletionTagTwoStbId);
                bufferTheTagsToBeCleared(existingDeletionTagTwoName, existingDeletionTagTwoStbId);
            } else {
                Assert.fail(
                        String.format(
                            "Failed to create a second tag for deletion as no settop where tagged to the first tag for deletion."));
            }

        } catch (IOException e) {
            Assert.fail(String.format("Failed to add tags for deleting (%s,%s) in the precondition of the test.",
                        existingDeletionTagOneName, existingDeletionTagTwoStbId), e);
        }
    }

    @BeforeTest(
        description = "Before test method for adding prerequisite tag for selecting the tag and deleting the STB.",
        groups = TestGroups.DELETION_SELECTED_TAG_GROUP
    )
    protected void addRequiredDeletionTagForDeleteOfStbOnSelectedTag() {
        existingDeletionTagThreeName = createDynamicTestTagNames();
        existingDeletionTagThreeStbIds = createRequestedTestStbs(TestConstants.DELETE_TAG_ONE_STB_COUNT);

        try {
            addTagUsingRestRequest(existingDeletionTagThreeName, existingDeletionTagThreeStbIds);
            bufferTheTagsToBeCleared(existingDeletionTagThreeName, existingDeletionTagThreeStbIds);
        } catch (IOException e) {
            Assert.fail(String.format("Failed to add tags for deleting (%s,%s) in the precondition of the test.",
                        existingDeletionTagThreeName, existingDeletionTagThreeStbIds), e);
        }

    }

    @Test(
        description = "Test to verify the selection and removal of selection from tags.",
        groups = TestGroups.EXISTING_TAG_NEEDED_GROUP
    )
    protected void testSelectionOnTags() {
        IndexPage indexPage = getIndexPageLoaded();

        // Selecting the first tag element.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Validating the first tag got highlighted.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagOneName), "Failed to select the first tag.");

        // Validating to see that only tagged stbs of highlighted first tag get listed.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagOneUniqueStbIds);

        // Selecting the second tag element to see that both tag got highlighted.
        indexPage.clickOnTagCloudTagElement(existingTagTwoName);

        // Validating the second selected tag got highlighted.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagTwoName), "Failed to select the second tag.");

        // Verifying the first tag remains as selected.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagOneName),
                "First tag selected previously does not reamin selected when the second tag is slected.");

        // Validating to see that only tagged stbs of first and second tag gets listed.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagOneUniqueStbIds, existingTagTwoUniqueStbIds);

        // Clicking on first tag again to remove the selection.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Validating the removal of highlight on first tag.
        Assert.assertFalse(indexPage.isTagSelectedInTagCloud(existingTagOneName),
                "Failed to remove highlight from first tag after clicking again on the higlighted tag.");

        // Validating the availability of highlight on second tag.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagTwoName),
                "Second tag highlight got removed after removal of first tag highlight.");

        // Validating to see that only second tagged stbs only get displayed.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagTwoUniqueStbIds);

        // Clicking on second tag again to remove the selection.
        indexPage.clickOnTagCloudTagElement(existingTagTwoName);

        // Validating the removal of highlight on second tag.
        Assert.assertFalse(indexPage.isTagSelectedInTagCloud(existingTagTwoName),
                "Failed to remove highlight from second tag after clicking again on the higlighted tag.");

    }

    @Test(
        description = "Test to verify the selection and removal of selection from tags with is having common STB list.",
        groups = TestGroups.EXISTING_TAG_NEEDED_GROUP
    )
    protected void testSelectionOnTagsWithCommonStbs() {
        IndexPage indexPage = getIndexPageLoaded();

        // Selecting the first tag element.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Selecting the second tag which is having same list of STBs in first tag selected.
        indexPage.clickOnTagCloudTagElement(existingTagThreeWithTagOneStbs);

        // Validating the second selected tag is in highlighted state.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagThreeWithTagOneStbs),
                "Failed to select the second tag.");

        // Validating the first selected tag also remain highlighted.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagOneName),
                "First tag selected previously does not remain selected when the second tag is selected.");

        // Validating to see that common stbs are not repeated in the filter table.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagOneUniqueStbIds);

        // Click on first tag to remove the tag selection.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Validating the removal of selection from first tag.
        Assert.assertFalse(indexPage.isTagSelectedInTagCloud(existingTagOneName),
                "Failed to remove the selection from first tag.");

        // Validating to see still the tag which is having common STBs remain in the filter table.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagOneUniqueStbIds);

    }

    @Test(
        description = "Test to validate the highlight of tag remains even after addition of a new tag to the tag cloud.",
        groups = TestGroups.EXISTING_TAG_NEEDED_GROUP
    )
    protected void testTagAdditionAfterTagSelectedInCloud() {
        IndexPage indexPage = getIndexPageLoaded();

        String tagSelected = existingTagOneName;
        String[] uniqueStbIdsForSelectedTag = existingTagOneUniqueStbIds;

        // Selecting the tag element.
        indexPage.clickOnTagCloudTagElement(tagSelected);

        // Validating to see that only list of STB available in selected tag is getting displayed
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, uniqueStbIdsForSelectedTag);

        // Selecting one of the STB tagged to tag one.
        if (null != uniqueStbIdsForSelectedTag && uniqueStbIdsForSelectedTag.length > 0) {

            // Getting a STB that is already tagged with the selected tag in tag cloud.
            String stbIdForTagging = uniqueStbIdsForSelectedTag[0];
            String newTestTagName = createDynamicTestTagNames();

            // Adding a new tag with the STB that is available on the selected tag in tag cloud.
            addTagFromIndexPage(indexPage, newTestTagName, stbIdForTagging);

            // Validating the highlight still remain on the first tag after addition of new tag.
            Assert.assertTrue(
                    indexPage.isTagSelectedInTagCloud(tagSelected),
                    "Failed to find highlight on the previously selected tag after the addition of a new tag.");

            // Validating to see that only list of STB available on selected tag is getting
            // displayed after new tag addition.
            failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, uniqueStbIdsForSelectedTag);

            // Validate whether the delete option appears for both the tags since STB id remains
            // in selected mode.
            Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(tagSelected),
                    String.format("Delete option does not appear for the tag (%s) when STB (%s) is selected.",
                        tagSelected,
                        stbIdForTagging));
            Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(newTestTagName),
                    String.format("Delete option does not appear for the tag (%s) when STB (%s) is selected.",
                        newTestTagName,
                        stbIdForTagging));
        } else {
            Assert.fail(String.format("No STBs are available on the tag one(%s)", tagSelected));
        }

    }

    @Test(
        description = "Test to verify the deletion of a STB from tag and a complete deletion of a tag from tag cloud.",
        groups = TestGroups.DELETION_TAG_REQUIRED_GROUP
    )
    protected void testDeletionOfTags() {
        IndexPage indexPage = getIndexPageLoaded();

        // Select check box of common stb that is available in two of the tags.
        indexPage.selectStbCheckboxes(existingDeletionTagTwoStbId);

        // Validate whether the delete option appears for both the tags when common STB id is
        // selected.
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(existingDeletionTagOneName),
                String.format("Delete option does not appear for the tag (%s) when STB (%s) is selected.",
                    existingDeletionTagOneName,
                    existingDeletionTagTwoStbId));
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(existingDeletionTagTwoName),
                String.format("Delete option does not appear for the tag (%s) when STB (%s) is selected.",
                    existingDeletionTagTwoName,
                    existingDeletionTagTwoStbId));

        // Deleting the selected STB content from first tag.
        indexPage.clickOnTagDeleteOptionAndConfirmDeletion(existingDeletionTagOneName);

        // Validating the tag count get reduced by one.
        validateTagCloudTagCount(indexPage, existingDeletionTagOneName, existingDeletionTagOneStbIds.length - 1);

        // Validating delete option on existing tag one got removed.
        Assert.assertFalse(indexPage.isDeleteOptionDisplayedInTag(existingDeletionTagOneName),
                "Deletion option is still getting displayed even after the removal of one tagged STB.");

        // Clicking on delete option on second tag.
        indexPage.clickOnTagDeleteOptionAndConfirmDeletion(existingDeletionTagTwoName);

        // Validate the second tag having single STB gets removed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertFalse(tagCloudElementText.contains(existingDeletionTagTwoName),
                String.format("The deletion of second tag(%s) failed, returned tag cloud texts is [%s].",
                    existingDeletionTagTwoName,
                    tagCloudElementText));
    }

    @Test(
        description = "Test to verify the deletion of a STB from tag when it is highlighted in tag cloud.",
        groups = TestGroups.DELETION_SELECTED_TAG_GROUP
    )
    protected void testDeletionWhileTagIsSelected() {

        IndexPage indexPage = getIndexPageLoaded();

        // Selecting the tag element.
        indexPage.clickOnTagCloudTagElement(existingDeletionTagThreeName);

        // Validating the tag got highlighted.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingDeletionTagThreeName),
                "On click of tag failed to select the tag.");

        // Selecting one of the STB for deletion from tag.
        if (null != existingDeletionTagThreeStbIds && existingDeletionTagThreeStbIds.length > 1) {

            // Select check box of common stb that is available in two of the tags.
            indexPage.selectStbCheckboxes(existingDeletionTagThreeStbIds[0]);

            // Deleting the selected STB content from first tag.
            indexPage.clickOnTagDeleteOptionAndConfirmDeletion(existingDeletionTagThreeName);

            // Validating the tag count get reduced by one to confirm the STB got removed.
            validateTagCloudTagCount(indexPage, existingDeletionTagThreeName,
                    existingDeletionTagThreeStbIds.length - 1);

            // Validating the tag previously selected before deletion of tag remain highlighted.
            Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingDeletionTagThreeName),
                    "The highlight on tag got removed after removal of a STB from highlighted tag.");

        } else {
            String failureMessage =
                (null == existingDeletionTagThreeStbIds)
                ? "No STBs are tagged under the existing tag three (%s) awaiting for deletion."
                : "The total STBs tagged under the existing tag three (%s) awaiting for deletion is only one. It expects more than one for deletion.";
            Assert.fail(String.format(failureMessage,
                        existingDeletionTagThreeName));
        }
    }

    @Test(
        description = "Test to verify that device getting hidden from index page is getting deselected. Selected non hidden device remains selected.", 
        groups = TestGroups.EXISTING_TAG_NEEDED_GROUP
    )
    protected void testDeviceIdDeselectionWhenHidden() {

        IndexPage indexPage = getIndexPageLoaded();

        // Selecting the tag one.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Validating the tag got highlighted or not.
        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagOneName),
                "On click of tag one highlight does not happen.");

        // Continue the test if the tag one have atleast one device ID.
        Assert.assertTrue(((null != existingTagOneUniqueStbIds) && (existingTagOneUniqueStbIds.length > 0)),
                "No devices tagged to tag one. Prerequisite of the test failed.");

        String deviceIdCommonForTagOneAndTwo = existingTagOneUniqueStbIds[0];

        // Select check box of common stb that is available in tag one and tag three.
        indexPage.selectStbCheckboxes(deviceIdCommonForTagOneAndTwo);

        // Validating the delete option is displayed for both tags when the common tagged device
        // id is selected.
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(existingTagOneName),
                String.format(
                    "Delete option does not appear for the existing tag one(%s) when STB (%s) is selected.",
                    existingTagOneName,
                    deviceIdCommonForTagOneAndTwo));
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(existingTagThreeWithTagOneStbs),
                String.format(
                    "Delete option does not appear for the existing tag three(%s) when STB (%s) is selected.",
                    existingTagThreeWithTagOneStbs,
                    deviceIdCommonForTagOneAndTwo));

        // Selecting the second tag inorder to validate that the delete option of previous selected
        // tag and checked status of device id remains
        indexPage.clickOnTagCloudTagElement(existingTagTwoName);

        Assert.assertTrue(indexPage.isTagSelectedInTagCloud(existingTagTwoName),
                "On click of tag two highlight does not happen.");

        Assert.assertTrue(indexPage.isStbCheckboxElementSelected(deviceIdCommonForTagOneAndTwo),
                String.format("Device(%s) check box field selection goes off when second tag got selected.",
                    deviceIdCommonForTagOneAndTwo));

        // Validating whether the delete option still persist when the device id of previously
        // selected tag remains on the display area.
        Assert.assertTrue(
                indexPage.isDeleteOptionDisplayedInTag(existingTagOneName),
                String.format(
                    "After selection of second tag, delete option does not prevails for the existing tag one(%s) when STB (%s) remains as selected.",
                    existingTagOneName,
                    deviceIdCommonForTagOneAndTwo));
        Assert.assertTrue(
                indexPage.isDeleteOptionDisplayedInTag(existingTagThreeWithTagOneStbs),
                String.format(
                    "After selection of second tag, delete option does not prevails for the existing tag three(%s) when STB (%s) remains as selected.",
                    existingTagThreeWithTagOneStbs,
                    deviceIdCommonForTagOneAndTwo));

        // Deselecting tag one so that all the device ID of tag one gets hidden.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        // Validation to see that only device ID of tag two remains displayed.
        failTestIfUniqueStbsNotFoundInFilteredTable(indexPage, existingTagTwoUniqueStbIds);

        // Validating the delete option of tags gets removed when a previously selected device ID
        // become hidden.
        Assert.assertFalse(
                indexPage.isDeleteOptionDisplayedInTag(existingTagOneName),
                String.format(
                    "Delete option still persist for existing tag one(%s) after the deselection of existing tag one.",
                    existingTagOneName));
        Assert.assertFalse(
                indexPage.isDeleteOptionDisplayedInTag(existingTagThreeWithTagOneStbs),
                String.format(
                    "Delete option still persist for existing tag three(%s) after the deselection of existing tag one.",
                    existingTagThreeWithTagOneStbs));

        // Selecting tag one again to see the device Id is not in selected mode.
        indexPage.clickOnTagCloudTagElement(existingTagOneName);

        Assert.assertFalse(indexPage.isStbCheckboxElementSelected(deviceIdCommonForTagOneAndTwo),
                String.format("Device(%s) remains selected when brought to display after hiding.",
                    deviceIdCommonForTagOneAndTwo));
    }

    /**
     * Validation of the STB device ID in the filter table div element.
     *
     * @param  indexPage  Index page object.
     * @param  stbIds     List of independent STB list added to the tags.
     */
    private void failTestIfUniqueStbsNotFoundInFilteredTable(IndexPage indexPage, String[]... stbIds) {
        List<String> deviceIds = indexPage.getStbFilteredTableDivElementsDeviceIdAttribute();
        int uniqueStbCount = 0;

        for (String[] ids : stbIds) {
            uniqueStbCount = uniqueStbCount + ids.length;

            for (String id : ids) {
                logger.info("STB device id on filteredTable : " + id);
                Assert.assertTrue(deviceIds.contains(id),
                        String.format("Failed to find STB device id(%s) in the filter table row.", id));
            }
        }

        // Validates any additional elements are present in the filter table.
        Assert.assertEquals(deviceIds.size(),
                uniqueStbCount,
                String.format("More STBs are listed(%s) in addition to the STBs in selected tags(%s).",
                    deviceIds.size(), uniqueStbCount));
    }
}
