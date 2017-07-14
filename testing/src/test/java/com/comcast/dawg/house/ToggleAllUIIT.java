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

import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.house.pages.IndexPage;
import com.comcast.dawg.test.base.IndexPageUITestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Class for UI integration test of toggle all check box in Dawg house index page.
 *
 * @author  Pratheesh TK
 */
public class ToggleAllUIIT extends IndexPageUITestBase {

    /** Existing tag one. */
    private String tagOneName = null;

    /** Existing tag two. */
    private String tagTwoName = null;

    /** Device available on tag one. */
    private String[] tagOneStbIds = null;

    /** Device available on tag two. */
    private String[] tagTwoStbIds = null;

    @BeforeClass(description = "Before method for adding STB and adding tag for the tests.")
    protected void prerequisiteAddition() {

        try {
            tagOneName = createDynamicTestTagNames();
            tagOneStbIds = createRequestedTestStbs(TestConstants.UNIQUE_TAG_ONE_STB_COUNT);
            addTagUsingRestRequest(tagOneName, tagOneStbIds);
            bufferTheTagsToBeCleared(tagOneName, tagOneStbIds);

            tagTwoName = createDynamicTestTagNames();
            tagTwoStbIds = createRequestedTestStbs(TestConstants.UNIQUE_TAG_TWO_STB_COUNT);
            addTagUsingRestRequest(tagTwoName, tagTwoStbIds);
            bufferTheTagsToBeCleared(tagTwoName, tagTwoStbIds);
        } catch (IOException e) {
            Assert.fail(String.format("Failed to add tags(%s,%s) in the precondition of the test.",
                        tagOneName, tagTwoName), e);
        }

    }

    /**
     * Test to validate the behavior of toggle all check box in the DAWG House when no tags
     * selected. This method will execute following steps,
     *
     * <ol>
     *   <li>Load DAWG house index page.</li>
     *   <li>Click on toggle all check box.</li>
     *   <li>Validate whether the toggle all check box get selected.</li>
     *   <li>Validate whether all the STB check box displayed is in selected state.</li>
     *   <li>Click on toggle all check box.</li>
     *   <li>Validate whether the toggle all check box get unchecked.</li>
     *   <li>Validate whether all the STB check boxes displayed get deselected.</li>
     * </ol>
     */
    @Test(description = "Test to validate the behavior of toggle all check box in the DAWG House when no tags selected.")
    protected void testToggleAllCheckboxWhenNoTagsSelected() {

        IndexPage indexPage = getIndexPageLoaded();

        // Select the toggle all check box.
        indexPage.clickToggleAllCheckbox();

        // Validating whether the toggle all check box is displayed as checked.
        Assert.assertTrue(indexPage.isToggleAllCheckboxSelected(),
                "Failed to select toggle all check box after clicking on toggle all check box.");

        Assert.assertTrue(
                indexPage.isAllStbCheckboxesInFilteredTableSelected(),
                "On selection of toggle all check box it does not checked all the displayed STB check boxes.");

        // Deselecting the toggle all check box.
        indexPage.clickToggleAllCheckbox();

        // Validate whether the toggle all check box is displayed as deselected.
        Assert.assertFalse(indexPage.isToggleAllCheckboxSelected(),
                "Failed to deselect toggle all check box after clicking on toggle all check box again.");

        Assert.assertTrue(
                indexPage.isAllStbCheckboxesInFilteredTableDeselected(),
                "On deselection of toggle all check box it does not unchecked all the displayed STB check boxes.");
    }

    /**
     * Test to validate the behavior of toggle all check box in DAWG House when tags selected. This
     * method will execute following steps,
     *
     * <ol>
     *   <li>Load DAWG house index page.</li>
     *   <li>Click on one tag in the tag cloud.</li>
     *   <li>Select toggle all check box.</li>
     *   <li>Validate whether the toggle all check box get selected.</li>
     *   <li>Validate whether all displayed STB check box got selected and the tag one gets delete
     *     option.</li>
     *   <li>Select another tag in tag cloud.</li>
     *   <li>Validate whether the toggle all check box get deselected.</li>
     *   <li>Validate whether the delete option remains on the tag one and all the STB of tag one
     *     remains selected.</li>
     *   <li>Select toggle all check box.</li>
     *   <li>Validate whether all the displayed STB check boxes are in selected state.</li>
     *   <li>Validate whether the delete option appears for the tag two.</li>
     *   <li>Deselect the tag one in the tag cloud.</li>
     *   <li>Validate whether the toggle all check box get deselected.</li>
     *   <li>Validate whether the STB check boxes of tag two remain displayed as selected.</li>
     * </ol>
     */
    @Test(description = "Test to validate the behavior of toggle all check box in DAWG House when tags selected.")
    protected void testToggleAllCheckboxWhenTagsSelected() {

        IndexPage indexPage = getIndexPageLoaded();

        // Selecting a tag to validate the behavior of toggle all check box when tag selected.
        indexPage.clickOnTagCloudTagElement(tagOneName);

        // Selecting the toggle all check box.
        indexPage.clickToggleAllCheckbox();

        // Validating whether the toggle all check box is displayed as checked.
        Assert.assertTrue(
                indexPage.isToggleAllCheckboxSelected(),
                "In tag selected mode: Failed to select toggle all check box after clicking on toggle all check box.");

        // Validating whether all displayed STB check box got checked.
        Assert.assertTrue(indexPage.isAllStbCheckboxesInFilteredTableSelected(),
                "In tag selected mode: On selection of toggle all check box it does" +
                " not checked all the displayed STB check boxes.");

        // Validating the delete option of the tag when toggle all check box is checked.
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(tagOneName),
                "On toggle tag selection all displayed STB got selected but the delete" +
                " option of tag one not get displayed.");

        // Selecting another tag to validate whether the toggle all check box get deselected.
        indexPage.clickOnTagCloudTagElement(tagTwoName);

        // Validating whether the toggle all check box get deselected after a new tag selection in
        // tag cloud.
        Assert.assertFalse(indexPage.isToggleAllCheckboxSelected(),
                "Toggle all check box is not unselected after a new tag selection in tag cloud.");

        // Fail test if any of the tag one STB check box previously selected gets unselected after
        // selection of second tag.
        failIfStbCheckBoxesAreNotChecked(indexPage, tagOneStbIds);

        // Selecting the toggle all check box again.
        indexPage.clickToggleAllCheckbox();

        // Validating whether the toggle all check box selection checked all the STB under tag one
        // and tag two.
        Assert.assertTrue(indexPage.isAllStbCheckboxesInFilteredTableSelected(),
                "In tag selected mode: On selection of toggle all check box it" +
                " does not checked all the displayed STB check boxes of tag one and two.");

        // Validating the delete option of the tag two when toggle all check box is checked.
        Assert.assertTrue(indexPage.isDeleteOptionDisplayedInTag(tagTwoName),
                "On toggle tag selection all displayed STB got selected but the delete" +
                " option of tag two does not get displayed.");

        // Deselecting a tag one.
        indexPage.clickOnTagCloudTagElement(tagOneName);

        // Validating whether the toggle all check box gets deselected after deselecting of tag
        // one in tag cloud.
        Assert.assertFalse(indexPage.isToggleAllCheckboxSelected(),
                "Toggle all check box is not unselected after a tag gets deselected in tag cloud.");

        // Fail test if any of the tag two STB check box previously selected gets unselected after
        // deselection of second one.
        failIfStbCheckBoxesAreNotChecked(indexPage, tagTwoStbIds);
    }

    /**
     * Fail if any of the STB check box field is not selected.
     *
     * @param  indexPage  Index page object.
     * @param  tagStbIds  List of STB ids.
     */
    private void failIfStbCheckBoxesAreNotChecked(IndexPage indexPage, String[] tagStbIds) {

        for (String stbId : tagStbIds) {
            Assert.assertTrue(indexPage.isStbCheckboxElementSelected(stbId),
                    String.format("Check box corresponding to STB(%s) is not checked.", stbId));
        }
    }
}
