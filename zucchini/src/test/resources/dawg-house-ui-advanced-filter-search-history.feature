@uitest 
Feature: Search history verification in dawg house advanced filter UI

Background: 
	Given I am on advanced filter overlay 
		 
#new scenario	
Scenario Outline: Verify search history display in advanced filter overlay 
	Given I perfomed a valid search using <count> filter values 
	And the search results displayed in filter table  
	When select Advanced button in the dawg home page 
	Then I should see search history in advanced filter overlay 
	Examples: 
		|count | 	
		|one   | 
		|two   |
		|four  |
		
#new scenario	
Scenario: Verify search history deletion 
	Given I have two search history details in advanced filter overlay
	When I delete one entry from the search history list
	Then I should see the selected entry removed from the search history list	
		
#new scenario
Scenario: Perform search operation from search history
	Given I have one search history details in advanced filter overlay
	When I select that entry from search history list	
	Then I should see filter entry selected in filter overlay
	When I select "Search" button
	Then I should see the search results displayed in filter table 
	
#new scenario
Scenario: Verify the filter value display while selecting an entry from search history list.
	Given I have two search history details in advanced filter overlay
	When I select first entry from search history list
	Then the first filter entry row is highlighted
	And I should see first filter entry selected in filter overlay
	When I select second entry from search history list
	Then the second filter entry row is highlighted
	And I should see second filter entry selected in filter overlay