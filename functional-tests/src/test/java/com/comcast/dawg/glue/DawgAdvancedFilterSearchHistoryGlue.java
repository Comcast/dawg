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

import com.comcast.dawg.DawgTestException;
import com.comcast.dawg.constants.DawgHouseConstants;
import com.comcast.dawg.constants.DawgHousePageElements;
import com.comcast.dawg.constants.TestConstants;
import com.comcast.dawg.helper.DawgAdvancedFilterPageHelper;
import com.comcast.dawg.selenium.SeleniumImgGrabber;
import com.comcast.dawg.selenium.SeleniumWaiter;
import com.comcast.zucchini.TestContext;

import org.testng.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * This class contains the UI behavior of dawg house advanced filter search
 * history
 * 
 * @author priyanka.sl
 */
public class DawgAdvancedFilterSearchHistoryGlue {

    /**
     * Choose one filter value to perform search 
     * @throws DawgTestException
     */
    @Given("^I choose filter value to perform search$")
    public void selectFilterValue() throws DawgTestException {

        List<String> availableFilters = DawgAdvancedFilterPageHelper.getInstance().getFilterConditionList();
        //At a time search can be perform using single filter value.So select one filter value among the list. 
        if (1 < availableFilters.size()) {
            //un check all the selected filter values
            DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(availableFilters,
                DawgHouseConstants.UNCHECK);
            //Get the first entry
            availableFilters = availableFilters.subList(1, availableFilters.size());
        }
        //Check the filter to perform search operation
        DawgAdvancedFilterPageHelper.getInstance().checkOrUncheckFilterValues(availableFilters,
            DawgHouseConstants.CHECK);
        // Perform search using the selected filter value 
        Assert.assertTrue(
            DawgAdvancedFilterPageHelper.getInstance().selectConditionBtn(DawgHouseConstants.BTN_NAME_SEARCH),
            "Failed to select the search button");
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
        // Verifying whether search overlay is displayed or not
        Assert.assertFalse(DawgAdvancedFilterPageHelper.getInstance().isSearchFilterOverlayDisplayed(),
            "Failed to display search results");
    }

    /**
     * Select advanced button in dawg house page       
     */
    @When("^I select Advanced button in the dawg home page$")
    public void selectAdvButton() {
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().selectAdvancedBtn(),
            "Failed to select advanced button from dawg home page");
        SeleniumWaiter.waitTill(DawgHousePageElements.DEFAULT_WAIT);
    }

    /**
     * Verify search history details displayed in advanced filter overlay
     */
    @Then("^search history details displayed in advanced filter overlay$")
    public void verifySearchHistoryList() {
        //Verify search history displayed
        StringBuilder msg = new StringBuilder();
        List<String> searchHistoryList = DawgAdvancedFilterPageHelper.getInstance().getSearchHistoryList();
        Assert.assertTrue(!searchHistoryList.isEmpty(),
            "Failed to find search history details in advanced filter overlay");
        List<String> searchesFilter = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_CONDITION);
        SeleniumImgGrabber.addImage();
        // Verify the searched filter is present in the search history list
        for (String filter : searchesFilter) {
            if (!searchHistoryList.contains(filter)) {
                msg.append(filter).append(",");
            }
        }
        Assert.assertTrue(msg.toString().length() == 0,
            "Search history detail/s" + msg + "not found for the searched filter");
    }

    /**
     * Verify search history list contains the specified count  
     * @param count - Search history count to add  
     * @throws DawgTestException
     */
    @Given("^I have (\\d+) search history details in advanced filter overlay$")
    public void VerifySearchHistory(int count) throws DawgTestException {
        List<String> oldSearchHistoryList = DawgAdvancedFilterPageHelper.getInstance().getSearchHistoryList();
        int existingFilterCount = oldSearchHistoryList.size();
        List<String> searchHistoryList = new ArrayList<String>();
        if (count < existingFilterCount) {
            // Deleting additional search histories if count < search history list size
            for (String filter : oldSearchHistoryList.subList(0, existingFilterCount - count)) {
                DawgAdvancedFilterPageHelper.getInstance().deleteSearchHistory(filter);
            }
        } else if (count > existingFilterCount) {
            // Adding search history details equal to count in advanced filter overlay.
            int maxRetry = 0;
            do {
                DawgAdvancedFilterPageHelper.getInstance().addFilterValues(1);
                //Select filter value to perform search
                this.selectFilterValue();
                //Select advanced button to load the advanced filter overlay
                selectAdvButton();
                searchHistoryList = DawgAdvancedFilterPageHelper.getInstance().getSearchHistoryList();
                maxRetry++;
                if ((searchHistoryList.size() == count || maxRetry >= TestConstants.VALID_FILTER_CONDITIONS.length * 2)) {
                    break;
                }
            } while (true);
        }
        SeleniumImgGrabber.addImage();
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_SEARCH_HISTORY, searchHistoryList);
    }

    /**
     * Delete search history filter based on the count specified  
     * @param count - Search history count to delete 
     */
    @When("^I delete (\\d+) (?:entry|entries) from the search history list$")
    public void deleteSearchHistory(int count) {
        StringBuilder filtersNotDeleted = new StringBuilder();
        List<String> searchHistoryList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_SEARCH_HISTORY);
        Assert.assertTrue(count <= searchHistoryList.size(),
            "Delete count " + count + "exceeds the history list count " + searchHistoryList.size());
        //Select the filters to be deleted based on the delete count
        List<String> listToDelete = (count == searchHistoryList.size()) ? searchHistoryList : searchHistoryList.subList(
            count, searchHistoryList.size());
        for (String filter : listToDelete) {
            if (!DawgAdvancedFilterPageHelper.getInstance().deleteSearchHistory(filter)) {
                filtersNotDeleted.append(filter).append(",");
            }
        }
        SeleniumImgGrabber.addImage();
        Assert.assertTrue(filtersNotDeleted.toString().length() == 0,
            "Failed to click delete button on filter/s" + filtersNotDeleted);
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_FILTER_TO_DELETE, listToDelete);
    }

    /**
     * Verify the selected entry removed from search history list   
     * @throws DawgTestException
     */
    @Then("^I should see the selected entry removed from the search history list$")
    public void verifyHistoryRemoved() throws DawgTestException {
        SeleniumImgGrabber.addImage();
        StringBuilder filtersNotDeleted = new StringBuilder();
        List<String> filtersDeleted = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_FILTER_TO_DELETE);
        // Get the available history list from filter overlay.
        List<String> currentHistoryList = DawgAdvancedFilterPageHelper.getInstance().getSearchHistoryList();
        if (!currentHistoryList.isEmpty()) {
            for (String filter : filtersDeleted) {
                if (currentHistoryList.contains(filter)) {
                    filtersNotDeleted.append(filter).append(",");
                }
            }
        }
        Assert.assertTrue(filtersNotDeleted.toString().length() == 0,
            "Failed to delete filter/s" + filtersNotDeleted + "from search history list");
    }

    /**
     * Verify all search history entries removed      
     * @throws DawgTestException
     */
    @Then("^I should see all entries removed from search history$")
    public void verifyAllEntriesRemoved() throws DawgTestException {
        SeleniumImgGrabber.addImage();
        // Get the available history list from filter overlay.
        List<String> currentHistoryList = DawgAdvancedFilterPageHelper.getInstance().getSearchHistoryList();
        Assert.assertTrue(currentHistoryList.isEmpty(), "Failed to delete all entries from search history list");
    }

    /**
     * Select "first/second/one" entry from search history list 
     * @param type - first/second
     * @throws DawgTestException
     */
    @When("^I select (.*) entry from search history list$")
    public void selectEntryFmHistory(String type) throws DawgTestException {
        List<String> currentHistoryList = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_SEARCH_HISTORY);
        //Select one entry from search history list  
        String selectEntry = null;
        if ("first".equals(type) || "one".equals(type)) {
            selectEntry = currentHistoryList.get(0);
        } else if ("second".equals(type)) {
            selectEntry = currentHistoryList.get(1);
        } else {
            throw new DawgTestException("Invalid entry" + type);
        }
        SeleniumImgGrabber.addImage();
        selectEntry = selectEntry.replaceAll(DawgHouseConstants.REPLACE_REGEX, "");
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().selectEntryFmSearchHistory(selectEntry),
            "Failed to select entry " + selectEntry + "from search history list");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_SEARCH_HISTORY_ENTRY, selectEntry);
    }

    /**
     * Select "first/second" entry from search history list 
     * @param type - first/second    
     */
    @Then("^I should see (.*) filter entry selected in filter overlay$")
    public void verifyHistoryEntrySelected(String type) {
        SeleniumImgGrabber.addImage();
        String filterEntry = TestContext.getCurrent().get(DawgHouseConstants.CONTEXT_SEARCH_HISTORY_ENTRY);
        //Verify filter entry row is highlighted in history list
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().isHistoryFilterhighlighted(filterEntry),
            type + "filter entry'" + filterEntry + "' is not get highlighted in history list");
        //Verify filter entry selected from history list also get selected/displayed in filter list        
        Assert.assertTrue(DawgAdvancedFilterPageHelper.getInstance().isFilterDisplayed(filterEntry),
            type + "filter entry'" + filterEntry + "' is not selected among filter list");
        TestContext.getCurrent().set(DawgHouseConstants.CONTEXT_EXPECTED_CONDITION, filterEntry);
    }

}
