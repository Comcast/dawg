@uitest @advSearchHistory
Feature: Advanced filter search history 

Background: 
	Given I am on advanced filter overlay 	
#new scenario	
@advanceSearch @smoke
Scenario: Verify search history display in advanced filter overlay 
	Given I added 1 filter value to advanced filter overlay 
	When I choose filter value to perform search 
	Then I should see the search results displayed in the filter table  
	When I select Advanced button in the dawg home page 
	Then search history details displayed in advanced filter overlay 	
		
#new scenario
Scenario Outline: Delete single history
	Given I have <count> search history details in advanced filter overlay 
	When I delete 1 entry from the search history list 
	Then I should see the selected entry removed from the search history list 
	Examples:
	       |count|
	       |2    |
	       |3    |	
Scenario Outline: Delete multiple histories
	Given I have <count> search history details in advanced filter overlay 
	When I delete <count> entries from the search history list 
	Then I should see all entries removed from search history
	Examples:
	       |count|
	       |2    |
	       |3    |
#new scenario
@advanceSearch 
Scenario: Perform search operation from search history 
	Given I have 1 search history details in advanced filter overlay 
	When I select one entry from search history list 
	Then I should see the filter entry selected in filter overlay 
	When I select 'Search' button in advanced filter overlay 
	Then I should see the search results displayed in the filter table 
	
#new scenario
Scenario: Verify the filter value display while selecting an entry from search history list. 
	Given I have 2 search history details in advanced filter overlay 
	When I select first entry from search history list 	
	And I should see first filter entry selected in filter overlay 
	When I select second entry from search history list	
	And I should see second filter entry selected in filter overlay 