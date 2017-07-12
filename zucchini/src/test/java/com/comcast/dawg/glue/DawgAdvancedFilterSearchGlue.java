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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.helper.DawgAdvancedFilterPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains the UI behaviour of dawg house advanced filter search  
 * @author priyanka.sl
 */
public class DawgAdvancedFilterSearchGlue {

    /**
     * Select buttons(Add,Delete,Search,BREAK) from advanced filter overlay
     * @throws DawgTestException
     */
    @When("^I select '(.*)' button in advanced filter overlay$")
    public void selectBtnFmFilterOverlay(List<String> buttons) throws DawgTestException {
        //Select the condition button and verify the specified buttons are selected successfully
        String buttonsNotSelected = DawgAdvancedFilterPageHelper.getInstance().verifyBtnSelected(buttons);
        Assert.assertTrue(0 == buttonsNotSelected.trim().length(),
            "Failed to select condition button/s" + buttonsNotSelected);
        SeleniumWaiter.waitTill(5);
    }

    /**
     * Verify the search results displayed based on the search criteria applied
     * @param condition 
     * @throws DawgTestException
     */
    @Then("^I should see the search results displayed ?(for NOT condition)? in the filter table$")
    public void verifySearchResults(String condition) throws DawgTestException {
        String testStb = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_ID);
        //verify test STB listed in the search results displayed
        String filterCondtn = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION);
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().isResultsDisplayed(filterCondtn, testStb),
            "Failed to find search results in filter table");
    }

    /**
     * Verify the conditions(AND, OR, NOT )applied to filter values
     * @param conditionBtn - List of condition buttons       
     * @throws DawgTestException     
     */
    @Then("^I should see condition '(.*)' applied (?:for all|to) filter values$")
    public void verifyCondtitonApplied(List<String> conditionBtn) throws DawgTestException {
        List<String> addedFilters = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        String expectedFilterCondtn = DawgAdvancedFilterPageHelper.getInstance().getExpectedFilterCondtions(
            conditionBtn, addedFilters);
        Assert.assertTrue(addedFilters.contains(expectedFilterCondtn),
            "Failed to find filter condition" + expectedFilterCondtn + "in condition list");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION, expectedFilterCondtn);
    }

    /**
     * Enter filter values(fields, option,value) in advanced filter overlay  
     * @param filed
     * @param option
     * @param value      
     * @throws DawgTestException     
     */
    @Given("^there is an advanced filter with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void addFilterConditions(String field, String option, String value) throws DawgTestException {
        RemoteWebDriver driver = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_WEB_DRIVER);
        if ("testId".equals(value)) {
            value = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_ID);
        }
        DawgAdvancedFilterPageHelper.getInstance().selectFilterCondition(field, option, value);
        //Verify filter values are get selected in dropdownlist 
        Select fieldSelect = new Select(driver.findElementByClassName(DawgHousePageElements.FIELD_SELECT));
        WebElement fieldOptn = fieldSelect.getFirstSelectedOption();
        Assert.assertEquals(field, fieldOptn.getText(), "Field value is not get selected in dropdown list");
        Select opSelect = new Select(driver.findElementByClassName(DawgHousePageElements.OP_SELECT));
        WebElement optionElement = opSelect.getFirstSelectedOption();
        Assert.assertEquals(option, optionElement.getText(), "Option value is not get selected in dropdownlist");
    }

    /**
     * Verify filter values(fields, option,value) added in advanced filter overlay  
     * @param filed
     * @param option
     * @param value      
     * @throws DawgTestException     
     */
    @Then("^I should see filter \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" values added in filter overlay$")
    public void verifyFilterValuesAdded(String field, String option, String value) throws DawgTestException {
        if ("testId".equals(value)) {
            value = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_TEST_STB_ID);
        }
        StringBuilder condition = new StringBuilder();
        String expectedFilter = null;
        condition.append(field).append(" ").append(option).append(" ").append(value);

        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        //if NOT condition is applied to single filter condition then append "!" to expected condition
        if (filterConditions.size() == 1 && filterConditions.get(0).contains("!")) {
            expectedFilter = condition.insert(0, "!").toString();
        }
        expectedFilter = condition.toString();
        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions in advanced filter overlay");
        // Verify the conditions added in filter overlay
        Assert.assertTrue(filterConditions.contains(expectedFilter), "Filter condition not added in the filter overlay");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION, expectedFilter);
    }

    /**
     * Apply conditions AND/OR/NOT to first/last filter values
     * @param condition - AND/OR/NOT 
     * @param position - Last or first position
     * @param count - Filter count to apply condition       
     * @throws DawgTestException     
     */
    @When("^I apply '(.*)' condition to (.*) (\\d+) filter values$")
    public void applyConditionInFilters(List<String> condition, String position, int count) throws DawgTestException {

        List<String> filtersToSelect = null;
        List<String> filtersToDeSelect = null;
        boolean isApplied = false;
        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        int filterCount = filterConditions.size();
        Assert.assertTrue(
            filterCount > count,
            "Atleast 3 filter conditions should be available to apply " + condition + "to" + position + "two filter values");

        filtersToDeSelect = filterConditions.subList(count, filterCount);
        if ("first".equals(position)) {
            //Get first two filter values to select
            filtersToSelect = filterConditions.subList(0, count);
        } else if ("last".equals(position)) {
            //Get last two filter values to select
            filtersToSelect = filterConditions.subList(filterCount - count, filterCount);
            //first filter values will be move to last position if any conditions(AND, or OR)applied
            //And last filter values will be move to first position
            //So check any condition applied  before selecting the last values
            for (String condtn : filtersToSelect) {
                if (condtn.contains("and") || condtn.contains("or")) {
                    isApplied = true;
                }
            }
            if (isApplied) {
                filtersToSelect = filterConditions.subList(0, count);
            } else {
                filtersToDeSelect = filterConditions.subList(0, count);
            }
        } else {
            throw new DawgTestException("Invalid position" + position);
        }
        //Tick the filter values to select
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(filtersToSelect,
            DawgHouseConstants.CHECK_FILTERS);
        //Uncheck  filter values if they are checked
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(filtersToDeSelect,
            DawgHouseConstants.UNCHECK_FILTERS);
        //Click search buton
        selectBtnFmFilterOverlay(condition);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
        String expectedFilterCondtn = DawgAdvancedFilterPageHelper.getInstance().getExpectedFilterCondtions(condition,
            filtersToSelect);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION, expectedFilterCondtn);

    }

    /**
     * Verify conditions AND/OR/NOT applied to first/last filter values
     * @param condition - AND/OR/NOT 
     * @param position - Last or first position  
     * @throws DawgTestException     
     */

    @Then("^I should see '(.*)' condition applied for (.*) two values$")
    public void verifyCondtnApplied(List<String> condition, String pos) throws DawgTestException {
        // Get the expected filter condition
        String expectedFilter = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION);
        Map<String, Boolean> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterCondnWithCheckedStatus();
        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions");
        boolean isFound = false;
        for (Map.Entry<String, Boolean> entry : filterConditions.entrySet()) {
            //Verify the expected filter condition matches with the filter value found 
            if (expectedFilter.equals(entry.getKey()) && entry.getValue()) {
                isFound = true;
            }
        }
        Assert.assertTrue(isFound, condition + "condition is not applied to two filter values");
    }

    /**
     * Add filter values specified in the data table
     * @param filterValues to add    
     * @throws DawgTestException     
     */
    @Given("^I added following filter values to advanced filter overlay$")
    public void addFilterValues(DataTable filterValues) throws DawgTestException {
        List<List<String>> data = filterValues.raw();
        List<String> addedFilters = new ArrayList<String>();
        for (List<String> filtersToAdd : data) {
            if (!filtersToAdd.contains("field")) {
                DawgAdvancedFilterPageHelper.getInstance().addFilterCondition(filtersToAdd.get(0), filtersToAdd.get(1),
                    filtersToAdd.get(2));
                String filters = filtersToAdd.get(0) + " " + filtersToAdd.get(1) + " " + filtersToAdd.get(2);
                addedFilters.add(filters);
            }
        }
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, addedFilters);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_COUNT, data.size() - 1);
    }

    /**
     * Verify condition button applied for the filter values
     * @param buttons    
     * @throws DawgTestException     
     */
    @Then("^I should see '(.*)' condition applied for the filter values$")
    public void verifyGroupCondtn(List<String> buttons) throws DawgTestException {
        Map<String, Boolean> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterCondnWithCheckedStatus();
        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions");
        List<String> filterValues = new ArrayList<String>(filterConditions.keySet());
        StringBuilder message = new StringBuilder();
        for (String btn : buttons) {
            //Verify filter values applied with specified conditions.
            if (!filterValues.toString().contains(btn.toLowerCase())) {
                message.append(btn).append(",");
            }
        }
        Assert.assertTrue(message.toString().length() == 0, message + "condition is not applied to filter values");
    }

    /**
     * Select the filter values 
     * @param buttons    
     * @throws DawgTestException     
     */
    @When("^I select both filter values with group condition applied$")
    public void selectFilterValues() throws DawgTestException {
        Map<String, Boolean> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterCondnWithCheckedStatus();
        //Tick the filter values to select
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(
            new ArrayList<String>(filterConditions.keySet()), DawgHouseConstants.CHECK_FILTERS);
        filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterCondnWithCheckedStatus();
        //Verify filter values are selected or not       
        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions");
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : filterConditions.entrySet()) {
            if (!entry.getValue()) {
                message.append(entry).append(",");
            }
        }
        Assert.assertTrue(message.toString().length() == 0, message + "is not displayed as checked");
    }

    /**
     * Select first filter value to delete  
     * @throws DawgTestException     
     */
    @When("^I select first filter value to delete$")
    public void selectFilterForDelete() throws DawgTestException {
        //Get the filter values added in filter overlay
        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();

        Assert.assertTrue(!filterConditions.isEmpty(), "Failed to find filter conditions");
        //Select the first filter value to delete and keep in context
        String filterToDelete = filterConditions.get(0);
        List<String> filtersToNotDelete = filterConditions.subList(1, filterConditions.size());
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_TO_DELETE, filterToDelete);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_TO_NOT_DELETE, filtersToNotDelete);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
        //Tick the first filter value to delete
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(filterConditions.subList(0, 1),
            DawgHouseConstants.CHECK_FILTERS);
        //Uncheck  remaining filter values if they are checked
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(filtersToNotDelete,
            DawgHouseConstants.UNCHECK_FILTERS);
    }

    /**
     * Verify filter values removed from filter overlay.
     * @param type - All filter values or selected filter value
     * @throws DawgTestException     
     */
    @Then("^I should see (selected|all) filter (?:value|values|value/s) removed from filter overlay$")
    public void verifyFilterRemoved(String type) throws DawgTestException {
        //Get the available filter conditions
        List<String> filterConditions = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        if ("all".equals(type)) {
            //Delete all filter values
            Assert.assertTrue(filterConditions.isEmpty(), "All filters not removed from filter overlay");
        } else if ("selected".equals(type)) {
            //Delete selected filter values
            String filterDeleted = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_TO_DELETE);
            List<String> filtersBeforeDelete = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_CONDITION);
            // Verify filter value removed from filter overlay
            Assert.assertTrue(
                !filterConditions.contains(filterDeleted) && filterConditions.size() == filtersBeforeDelete.size() - 1,
                filterDeleted + "is not removed from filter overlay");
            TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_CONDITION, filterConditions);
        } else {
            throw new DawgTestException("Invalid type" + type);
        }
    }

    /**
     * Verify second filter value remains in filter overlay after the deletion of first filter value    
     * @throws DawgTestException     
     */
    @Then("^second filter value remains in filter overlay$")
    public void verifyFilterNotRemoved() {
        // Verify second filter value remains
        List<String> filtersNotDeleted = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_TO_NOT_DELETE);
        List<String> filterList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_CONDITION);
        //If one filter value removed from the list of 2 filters, 
        //then the filter list contains the filter which is not deleted
        Assert.assertTrue(filtersNotDeleted.equals(filterList), filtersNotDeleted + "is removed from filter list");
    }

    /**
     * Apply condition to specified no.of filter values
     * @param buttons
     * @param filterCount to add   
     * @throws DawgTestException     
     */
    @When("^I apply '(.*)' condition to (\\d+) filter values$")
    public void applyCondition(List<String> buttons, int filterCount) throws DawgTestException {
        selectBtnFmFilterOverlay(buttons);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_CONDTN_BTNS, buttons);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_COUNT, filterCount);
    }

    /**
     * Verify filter conditions splitted
     * @param filterCountSplitted  
     * @throws DawgTestException     
     */
    @Then("^each filter conditions splitted as (\\d+)$")
    public void verifyFiltersSplitted(int filterCountSplitted) throws DawgTestException {
        //Get the filters before applying BREAK condition
        List<String> buttonApplied = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_CONDTN_BTNS);
        List<String> currentFilters = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        //Verify the splitted filters does not contain applied condition
        StringBuilder message = new StringBuilder();
        for (String btn : buttonApplied) {
            //Verify filter values applied with specified conditions.
            if (currentFilters.toString().contains(btn.toLowerCase())) {
                message.append(btn).append(",");
            }
        }
        Assert.assertTrue(currentFilters.size() == filterCountSplitted && message.toString().length() == 0,
            message + "condition is still present in filter values");
    }
}
