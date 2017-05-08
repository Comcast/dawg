@uitest 
Feature: Verify Dawg-house Advanced Filter UI behaviours. 

Background: 
	Given I am on advanced filter overlay 
	
Scenario: Verify button display using single filter values 
	Given I added one filter value/s to advanced filter overlay 
	When I click Add button 
	Then I should see filter value/s added in filter overlay 
	And I should verify button display as follows 
		|Button  |Enabled | 
		|AND     |false    |
		|OR      |false    |
		|NOT     |true     | 
		|DEL     |true     | 		
		|BREAK   |false    |
		
Scenario: Verify button display using multiple filter values 
	Given I added two filter value/s to advanced filter overlay 
	When I click Add button 
	Then I should see filter value/s added in filter overlay 
	And I should verify button display as follows 
		|Button  |Enabled  | 
		|AND     |true     |
		|OR      |true     |
		|NOT     |true     | 
		|DEL     |true     | 		
		|BREAK   |false    |
		
Scenario: Verify the search results using multiple filter values 
	Given I added two filter value/s to advanced filter overlay 
	When I click Add button 
	Then I should see filter value/s added in filter overlay 
	When I click on the Search button 
	Then I should see the search results displayed in the filter table 
	
Scenario Outline: 
	Verify the search results while selecting condition buttons (AND,OR,NOT,) 
	Given I added two filter value/s to advanced filter overlay 
	And I should see filter value/s added in filter overlay 
	When I select  condition button/s "<button>" 
	Then I should see condition applied to filter values
	When I click on the Search button 
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 		
		|OR      | 
		|AND     |
		|NOT     |
		|AND, NOT|		
		
Scenario Outline: 
	Verify button display while selecting condition buttons(AND,OR,NOT) 
	Given I added two filter value/s to advanced filter overlay 
	When I select condition button/s "<button>" 
	Then I should verify the button display as follows 
		|Button  |Enabled | 
		|AND     |false   |
		|OR      |false   |
		|NOT     |true    | 
		|DEL     |true    | 		
		|BREAK   |true    |		
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |
		|NOT     |
		|AND, NOT| 
		
Scenario Outline: Verify the search results using single filter values 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I click Add button 
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 
	When I click on the Search button 
	Then I should see the search results displayed in the filter table 
	Examples: 
		| field                 | option  | value               |  		
		| Id                    | equals  | testadvf116118527982|
		| Model                 | matches | Test.*              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 
		| Hardware Revision     | contains| TestHardwareRevision|  
		| Capabilities          | contains| TestCap             |
		| Capabilities          | contains| SomeOtherCapability |	
		
Scenario Outline: 
	Verify the search results using single filter values with NOT condition 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I click Add button to add filters in filter overlay 
	And I select "NOT" button in the filter overlay 
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 
	When I click on the Search' button 
	Then I should see the search results displayed for NOT condition 
	Examples: 
		| field                 | option  | value               |  		
		| Id                    | equals  | testadvf116118527982|
		| Model                 | matches | Test.*              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 
		| Hardware Revision     | contains| TestHardwareRevision|  
		| Capabilities          | contains| TestCap             |
		| Capabilities          | contains| SomeOtherCapability |
		
		
Scenario Outline: 
	Verify checkbox selection while adding filter values to filter overlay 
	Given I added <count> filter value/s to advanced filter overlay 
	Then I should see filter value/s added in filter overlay 
	And I should verify filter value checkboxes as selected 
	Examples: 
		|count  | 	
		|one    | 
		|two    |
		|four   |
		
Scenario: 
	Verify search results by applying filter conditions together (AND and OR) 
	Given I added four filter value/s to advanced filter overlay 
	When I apply 'AND' condition to first two filter values 
	Then I should see condition applied for first two values 
	And I apply 'OR' condition to last two filter values 
	Then I should see condition applied for last two values 
	When I click on the Search button 
	Then I should see the search results for group condition filter 
	
Scenario Outline: 
	Verify search results by applying group conditions (AND and OR) and applying the conditions separatly 
	Given I applied group conditions AND and OR to four filter values 
	And I select condition button/s "<button>" 
	Then I should see condition applied for filter values
	When I click on the Search button 
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |
		|NOT     |
		|AND, NOT| 
		
Scenario: Verify the deletion of single filter value 
	Given I added two filter values to advanced filter overlay 
	Then I should see filter value/s added in filter overlay 
	When I select one filter value/s to delete 
	Then I should see the selected filter value/s removed from the filter overlay 
	And the second filter value remains in filter overlay 
	
Scenario: Verify the deletion of multiple filter values 
	Given I added two filter values to advanced filter overlay 
	Then I should see filter value/s added in filter overlay 
	When I select all filter value/s to delete 
	Then I should see the selected filter value/s removed from the filter overlay 
	
Scenario: Break the group conditions with two filter values 
	Given I added two filter values to advanced filter overlay 
	When I apply 'AND' condition to two filter values 
	Then I should see filter condition 'AND' applied in filter values 
	When I select BREAK button 
	Then I should see two individual filter conditions 
	
Scenario: Break the group conditions with four filter values 
	Given I applied group conditions 'AND and OR' to four filter values 
	Then I should see filter condition 'AND and OR' applied in filter values 
	When I select BREAK button 
	Then I should see four individual filter conditions
	 
#new scenario	
Scenario Outline: Verify search history display in advanced filter overlay 
	Given I perfomed a search using <count> filter values 
	And I should see the search results displayed in filter table 
	When select Advanced button in the dawg home page 
	Then I should see search history in advanced filter overlay 
	Examples: 
		|count | 	
		|single| 
		|two   |
		|four  |
		
#new scenario	
Scenario: Verify search history deletion 
	Given I perfomed a search using four filter values 
	And I should see the search results displayed in filter table 
	When select Advanced button in the dawg home page 
	Then I should see search history in advanced filter overlay
	When I delete one entry from the search history
	Then I should see the entry deleted removed from the search history	