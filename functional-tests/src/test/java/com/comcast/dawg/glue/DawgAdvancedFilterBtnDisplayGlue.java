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
package com.comcast.dawg.glue;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgAdvancedFilterPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.zucchini.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains the button display behavior in dawg house advanced filter page 
 * @author priyanka.sl
 */
public class DawgAdvancedFilterBtnDisplayGlue {    
    /**
     * Verify the condition button display       
     * @throws DawgTestException     
     */
    @Then("^I should verify following buttons displayed as below$")
    public void verifyCondtnBtnDisplay(Map<String, String> dataTable) throws DawgTestException {
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {
            String button = entry.getKey();
            Boolean display = Boolean.valueOf(entry.getValue());
            if (!"Button".equals(entry.getKey())) {
                boolean isEnabled = DawgAdvancedFilterPageHelper.getInstance().verifyButtonDisplay(button);
                if (isEnabled != display) {
                    msg.append(button + " enabled-" + isEnabled).append(",");
                }
            }
        }
        Assert.assertTrue(0 == msg.toString().trim().length(), msg + "in advanced filter overlay");
    }

    /**
     * Verify condition button check box selection 
     */
    @Then("^I should verify all filter value checkboxes as selected$")
    public void verifyChkBoxSelected() {
        StringBuilder filters = new StringBuilder();
        SeleniumImgGrabber.addImage();
        //Get the filter conditions with checked status
        Map<String, Boolean> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterCondnWithCheckedStatus();
        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions");
        for (Map.Entry<String, Boolean> entry : filterConditions.entrySet()) {
            if (!entry.getValue()) {
                filters.append(entry.getKey()).append(",");
            }
        }
        Assert.assertTrue(0 == filters.toString().trim().length(), filters + "displayed as not checked");
    }

    /**
     * Uncheck the selected filter condition values       
     * @throws DawgTestException     
     */
    @When("^I uncheck filter value/s$")
    public void unCheckFilterValues() throws DawgTestException {
        List<WebElement> conditionElements = DawgAdvancedFilterPageHelper.getInstance().getConditionDivElement();
        Assert.assertTrue(!conditionElements.isEmpty(), "Failed to find condition div elements");
        try {
            for (WebElement ele : conditionElements) {
                String filterText = ele.findElement(By.className(DawgHousePageElements.CONDITION_TEXT)).getText();
                if (!filterText.isEmpty()) {
                    WebElement chckBoxEle = ele.findElement(By.className(DawgHousePageElements.CONDITION_CHECK_BOX));
                    if (chckBoxEle.isSelected()) {
                        //De-select the selected check-box
                        chckBoxEle.click();
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw new DawgTestException("Failed to inspect the Webelement:" + e.getMessage());
        }
    }

    /**
     * Verify filter values removed from the filter overlay
     */
    @Then("^I should see filter value/s removed from filter overlay$")
    public void verifyFilterValRemoved() {
        StringBuilder filtersNotRemoved = new StringBuilder();
        List<String> addedFilters = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_CONDITION);
        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        Assert.assertTrue(filterConditions.isEmpty(), "Failed to find filter conditions");
        for (String filter : addedFilters) {
            if (filterConditions.contains(filter)) {
                filtersNotRemoved.append(filter).append(",");
            }
        }
        Assert.assertTrue(0 == filtersNotRemoved.toString().trim().length(),
            "Failed to remove filteres " + filtersNotRemoved + "from filter overlay");
    }    
}
