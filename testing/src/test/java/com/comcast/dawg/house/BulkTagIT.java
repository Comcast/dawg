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

import java.util.Set;

import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.constants.TestGroups;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.dawg.test.base.IndexPageUITestBase;

import com.comcast.video.dawg.common.MetaStb;

import org.openqa.selenium.Alert;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.comcast.dawg.constants.TestConstants.USER;

/**
 * The UI Testing for Dawg house's Bulk Tag.
 *
 * @author  TATA
 */
public class BulkTagIT extends IndexPageUITestBase {

    /** Existing Set-top detail for test purpose. */
    private MetaStb existingStb = null;

    /** Existing tag name for test purpose. */
    private String existingTagName = null;

    /**
     * Before method for tag addition for the tests.
     */
    @BeforeTest(groups = TestGroups.EXISTING_TAG_REQUIRED_TEST_GROUP)
    protected void prerequisiteTagAddition() {
        existingStb = createTestStb();
        existingTagName = createDynamicTestTagNames();

        try {
            addTagUsingRestRequest(existingTagName, existingStb.getId());
            bufferTheTagsToBeCleared(existingTagName, existingStb.getId());
        } catch (IOException e) {
            Assert.fail(String.format("Failed to add tag %s as a before test.", existingTagName), e);
        }

        logger.info("STB got added in the before test : " + existingStb.getId());
        logger.info("Tag got created is before test : " + existingTagName);
    }

    @Test(description = "Tests if a user can tag a box that has no tags", groups = "smoke")
    protected void testValidTag() {
        String newTag = createDynamicTestTagNames();

        MetaStb testStb = createTestStb();
        IndexPage indexPage = new IndexPage(driver, USER);

        // Tag the box
        loadIndexPageAndAddTag(indexPage, newTag, testStb.getId());

        // verify UI meta data with out refreshing the page
        String tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb.getId());
        Assert.assertTrue(tagSpanText.contains(newTag),
                "The stb meta data doesn't contain the tag " + newTag + " in " +
                tagSpanText);

        // Validate the tag count.
        validateTagCloudTagCount(indexPage, newTag, 1);

        // Validate bulk tag input text element gets cleared after addition of tag.
        Assert.assertEquals(indexPage.getBulkTagTextElementValue(), TestConstants.EMPTY_STRING,
                "Bulk tag input text element does not get cleared after addition of tag.");

        // Refresh and Verify the tag cloud and confirm the newly created tag
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(newTag),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    newTag));

        // Verify meta data that the tag is added for the given stb
        validateBackendStbMetaDataTags(testStb, newTag);
    }

    @Test(description = "Tests if a user can tag a box that has tags but not the given one", groups = "EXISTING_TAG_REQUIRED_TEST_GROUP")
    protected void testNewTag() {
        String newTag = createDynamicTestTagNames();

        MetaStb testStb = createTestStb();
        IndexPage indexPage = new IndexPage(driver, USER);

        // Add the stb id to already existing tag.
        loadIndexPageAndAddTag(indexPage, existingTagName, testStb.getId());

        // Add new tag by loading the index page
        loadIndexPageAndAddTag(indexPage, newTag, testStb.getId());

        // verify UI meta data with out refreshing the page
        String tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb.getId());
        Assert.assertTrue(tagSpanText.contains(newTag),
                "STB meta data doesn't contain newly added tag : " + newTag +
                " in " + tagSpanText);
        Assert.assertTrue(tagSpanText.contains(existingTagName),
                "STB meta data doesn't contain the already existing tag : " + existingTagName);

        // Validate the tag count.
        validateTagCloudTagCount(indexPage, newTag, 1);

        // Refresh and Verify the tag cloud and confirm the newly created tag
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(newTag),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    newTag));
        Assert.assertTrue(tagCloudElementText.contains(existingTagName),
                String.format("Not able to view the existing tag (%s) in tag cloud after refresh.",
                    existingTagName));

        // Validate the backend to see that tag exist agaist the set-top
        validateBackendStbMetaDataTags(testStb, newTag, existingTagName);
    }

    @Test(description = "Tests if a user can tag a box that already contains the tag", groups = TestGroups.EXISTING_TAG_REQUIRED_TEST_GROUP)
    protected void testExistingTag() {
        String newTag = createDynamicTestTagNames();

        MetaStb existingTestStb = existingStb;

        IndexPage indexPage = new IndexPage(driver, USER);

        // add the existing tag
        loadIndexPageAndAddTag(indexPage, newTag, existingTestStb.getId());

        // verify UI meta data with out refreshing the page
        String stbMetaDataTagSpanContent = indexPage.getStbMetaDataTagSpanTextContent(existingTestStb.getId());
        Assert.assertTrue(stbMetaDataTagSpanContent.contains(newTag),
                "The stb meta data doesn't contain newly added tag : " + newTag + " in " +
                stbMetaDataTagSpanContent);
        Assert.assertTrue(stbMetaDataTagSpanContent.contains(existingTagName),
                "The stb meta data doesn't contain the previously available tag : " + newTag + " in " +
                stbMetaDataTagSpanContent);

        // Validate the tag count.
        validateTagCloudTagCount(indexPage, newTag, 1);

        // Refresh and verify the tag cloud and confirm the newly created tag is
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(newTag),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    newTag) + " in " +
                tagCloudElementText);

        // Verify meta data that the tag is added for the given stb
        validateBackendStbMetaDataTags(existingTestStb, newTag);
    }

    @Test(description = "Tests if a user can tag a box with multiple tags")
    protected void testMultipleTag() {

        String newTag1 = createDynamicTestTagNames();
        String newTag2 = createDynamicTestTagNames();
        String tag = newTag1 + " " + newTag2;

        MetaStb testStb = createTestStb();
        IndexPage indexPage = new IndexPage(driver, USER);

        // Tag the box
        loadIndexPageAndAddTag(indexPage, tag, testStb.getId());

        // verify UI meta data with out refreshing the page
        String tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb.getId());
        Assert.assertTrue(tagSpanText.contains(newTag1),
                "The stb meta data doesn't contain" + newTag1 + " in " +
                tagSpanText);
        Assert.assertTrue(tagSpanText.contains(newTag2),
                "The stb meta data doesn't contain" + newTag2 + " in " +
                tagSpanText);

        // Validate the tag count.
        validateTagCloudTagCount(indexPage, newTag1, 1);
        validateTagCloudTagCount(indexPage, newTag2, 1);

        // Refresh and Verify the tag cloud and confirm the newly created tag
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(newTag1),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    newTag1));
        Assert.assertTrue(tagCloudElementText.contains(newTag2),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    newTag2));

        // Verify backend meta data to see both the tag exist for the STB.
        validateBackendStbMetaDataTags(testStb, newTag1, newTag2);
    }

    @Test(
        description = "Tests if a user can tag a box with multiple tags and the box already contains one of those tags",
        groups = {TestGroups.EXISTING_TAG_REQUIRED_TEST_GROUP, "smoke"}
    )
    protected void testMultipleTagWithExistingTag() {
        String existingTag = existingTagName;
        String additionalTag = createDynamicTestTagNames();
        String tag = existingTag + " " + additionalTag;

        MetaStb testStb = existingStb;

        IndexPage indexPage = new IndexPage(driver, USER);

        // Refresh page and tag with existing and new tag
        loadIndexPageAndAddTag(indexPage, tag, testStb.getId());

        // verify UI meta data with out refreshing the page
        String spanText = indexPage.getStbMetaDataTagSpanTextContent(testStb.getId());
        Assert.assertTrue(spanText.contains(existingTag),
                "The stb meta data doesn't contain the previous tag : " +
                existingTag + " in " + spanText);
        Assert.assertTrue(spanText.contains(additionalTag),
                "The stb meta data doesn't contain the new tag : " +
                additionalTag + " in " + spanText);

        // Validate the tag count.
        validateTagCloudTagCount(indexPage, additionalTag, 1);

        // Refresh and Verify the tag cloud and confirm the newly created tag
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(existingTag),
                String.format("Not able to view the already exisitng tag (%s) in tag cloud after refresh.",
                    existingTag));
        Assert.assertTrue(tagCloudElementText.contains(additionalTag),
                String.format("Not able to view the newly added tag (%s) in tag cloud after refresh.",
                    additionalTag));

        // Verify backend meta data to see both the tag exist for the STB.
        validateBackendStbMetaDataTags(testStb, existingTag, additionalTag);
    }

    @Test(
        description = "Tests if a user can tag a box with a tag that already exists in the tag cloud",
        groups = TestGroups.EXISTING_TAG_REQUIRED_TEST_GROUP
    )
    protected void testExistingTagInCloud() {
        String existingTag = existingTagName;

        MetaStb testStb = createTestStb();

        IndexPage indexPage = new IndexPage(driver, USER);

        // Tag the box
        loadIndexPageAndAddTag(indexPage, existingTag, testStb.getId());

        // verify UI meta data with out refreshing the page
        String tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb.getId());
        Assert.assertTrue(tagSpanText.contains(existingTag),
                "The STB meta data doesn't contain the tag after addition : " + existingTag + " in " +
                tagSpanText);

        // Refresh and Verify the tag cloud and confirm the newly created tag
        // listed.
        String tagCloudElementText = indexPage.refreshPageAndGetTagCloudElementText();
        Assert.assertTrue(tagCloudElementText.contains(existingTag),
                String.format("Not able to view the already existing tag (%s) in tag cloud after refresh.",
                    existingTag));

        // Verify meta data that the tag is added for the given stb
        validateBackendStbMetaDataTags(testStb, existingTag);

    }

    @Test(description = "Tests if a user can tag multiple boxes.", groups = "smoke")
    protected void testMultipleStbTaging() {
        MetaStb testStb1 = createTestStb();
        MetaStb testStb2 = createTestStb();
        MetaStb testStb3 = createTestStb();
        String newTag = createDynamicTestTagNames();

        IndexPage indexPage = new IndexPage(driver, USER);

        // Tag the boxes
        loadIndexPageAndAddTag(indexPage, newTag, testStb1.getId(), testStb2.getId(), testStb3.getId());

        // Looking for tag name in the 1st STB meta data.
        String tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb1.getId());
        Assert.assertTrue(
                tagSpanText.contains(newTag),
                String.format(
                    "The first STB meta data doesn't contain the tag (%s) after addition. The tag span text found is %s.",
                    newTag,
                    tagSpanText));

        // Looking for tag name in the 2nd STB meta data.
        tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb2.getId());
        Assert.assertTrue(
                tagSpanText.contains(newTag),
                String.format(
                    "The second STB meta data doesn't contain the tag (%s) after addition. The tag span text found is %s.",
                    newTag,
                    tagSpanText));

        // Looking for tag name in the 3rd STB meta data.
        tagSpanText = indexPage.getStbMetaDataTagSpanTextContent(testStb3.getId());
        Assert.assertTrue(
                tagSpanText.contains(newTag),
                String.format(
                    "The thrird STB meta data doesn't contain the tag (%s) after addition. The tag span text found is %s.",
                    newTag,
                    tagSpanText));

        // Validating the tag count.
        validateTagCloudTagCount(indexPage, newTag, 3);

        // Add one more stb to tag for validating the count.
        MetaStb testStb4 = createTestStb();
        loadIndexPageAndAddTag(indexPage, newTag, testStb4.getId());

        // Validating the tag count after addition of the stb..
        validateTagCloudTagCount(indexPage, newTag, 4);

        // Validate bulk tag input text element gets cleared after addition of new device to the
        // existing tag.
        Assert.assertEquals(
                indexPage.getBulkTagTextElementValue(), TestConstants.EMPTY_STRING,
                "Bulk tag input text element does not get cleared after addition of a device Id to existing tag.");

        // Verifying the backend stb meta data to validate the tag is present.
        validateBackendStbMetaDataTags(testStb1, newTag);
        validateBackendStbMetaDataTags(testStb2, newTag);
        validateBackendStbMetaDataTags(testStb3, newTag);
    }

    @Test(
        description = "Test to validate the prompt of an alert message when user try to add a tag without selecting any device Id."
    )
    protected void testAlertOnNewTagAdditionWithoutDeviceId() {

        IndexPage indexPage = new IndexPage(driver, USER);
        indexPage.load();

        String newTag = createDynamicTestTagNames();

        // Enter the tag name.
        indexPage.typeOnBulkTagTextElement(newTag);

        // Click on bulk tag add button.
        indexPage.clickOnBulkTagAddButton();

        // Validate the error message is displayed.
        Assert.assertTrue(isAlertPresent(),
                "No alert message displayed when tried to add a tag without selecting any device Ids.");

        Alert noSelectionAlert = driver.switchTo().alert();
        Assert.assertEquals(noSelectionAlert.getText(), TestConstants.NO_DEVICE_SELECTED_ALERT_MSG,
                "Alert message displayed is different from what expected.");

        // Accept the alert to dismiss the message.
        noSelectionAlert.accept();

        // Validate whether the entered tag name on bulk tag element remain uncleared.
        Assert.assertEquals(indexPage.getBulkTagTextElementValue(), newTag,
                "Tag name entered on bulk tag text element got modified.");

        String allTagsInTagCloud = indexPage.getTagCloudElementText();

        // Validate tag presence in tag cloud.
        Assert.assertFalse(allTagsInTagCloud.contains(newTag),
                String.format("Alert message displayed but the tag got added to the tag cloud." +
                    " Tag - %s in present in list of tags(%s) in tag cloud.", newTag,
                    allTagsInTagCloud));
    }

    /**
     * Validate the STB Meta data tags.
     *
     * @param  stb       Set-top object.
     * @param  tagNames  list of tags to be verified in the backend retrieved meta data.
     */
    private void validateBackendStbMetaDataTags(MetaStb stb, String... tagNames) {
        Set<String> stbMetaDataTags = getStbMetaDataTagsFromBackend(stb);

        for (String tagName : tagNames) {
            Assert.assertTrue(stbMetaDataTags.contains(tagName),
                    String.format("Backend stb(%s) meta data doesn't contain tag %s.", stb.getId(), tagName));
        }
    }
}
