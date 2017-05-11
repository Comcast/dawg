@uitest 
Feature: Verify Dawg-house Advanced Filter UI behaviours. 

Background: 
	Given I am on advanced filter overlay 
	
Scenario Outline: Verify the search results using multiple filter values 
	Given I added <Count> filter value/s to advanced filter overlay 	 
	Then I should see filter value/s added in filter overlay 
	When I select "Search" button 
	Then I should see the search results displayed in the filter table 
		Examples: 
		|Count  | 		
		|2      | 		
		|4      |
	
Scenario Outline: 
	Verify the search results while selecting condition buttons (AND,OR,NOT) 
	Given I added two filter value/s to advanced filter overlay 
	And I should see filter value/s added in filter overlay 
	When I select condition button/s "<button>" 
	Then I should see condition applied to filter values
	When I select "Search" button 
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 		
		|OR      | 
		|AND     |
		|NOT     |
		|OR, NOT |	
		|AND, NOT|
			
Scenario Outline: Verify the search results using single filter values 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I select "Add" button 
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 
	When I select "Search" button 
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
		
Scenario Outline: Verify the search results using single filter values with NOT condition 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I click Add button to add filters in filter overlay 
	And I select "NOT" button in the filter overlay 
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 
	When I select "Search" button 
	Then I should see the search results displayed for NOT condition of filter query
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
		
Scenario Outline: Verify checkbox selection while adding filter values to filter overlay 
	Given I added <count> filter value/s to advanced filter overlay 
	Then I should see filter value/s added in filter overlay 
	And I should verify all filter value checkboxes as selected 
	Examples: 
		|count  | 	
		|one    | 
		|two    |
		|four   |
		
Scenario: Verify search results by applying filter conditions together (AND and OR) 
	Given I added four filter value/s to advanced filter overlay 
	When I apply 'AND' condition to first two filter values 
	Then I should see condition applied for first two values 
	And I apply 'OR' condition to last two filter values 
	Then I should see condition applied for last two values 
	When I select "Search" button 
	Then I should see the search results for group condition filter 
	
#new scenario	
Scenario Outline: Verify the search results using multiple filter values with conditions(AND, OR, OR NOT, AND NOT)
	Given I added following filter values to advanced filter overlay 
		| field                 | option  | value               |
		| Model                 | matches | Test.*              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 	
	Then I should see filter values added in filter overlay 
	When I select condition button/s "<button>" 
	Then I should see "<button>" condition applied for all filter values 
	When I select "Search" button 
	Then I should see the search results displayed for "<button>" condition 
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |		
		|OR, NOT | 
		|AND, NOT| 	
		
Scenario Outline: Verify search results by applying group conditions (AND and OR) and applying single conditions  
	Given I added four filter value/s to advanced filter overlay 
	When I apply 'AND' condition to first two filter values 
	Then I should see condition applied for first two values 
	And I apply 'OR' condition to last two filter values 
	Then I should see condition applied for last two values 
	When I select both filter values with group condition applied
	And I select condition button/s "<button>" 
	Then I should see condition<button> applied for filter values
	When I select "Search" button 
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |
		|NOT     |
		|OR, NOT | 
		|AND, NOT| 
		
Scenario: Verify the deletion of single filter value 
	Given I added two filter values to advanced filter overlay 
	Then I should see filter value/s added in filter overlay 
	When I select first filter value/s to delete 
	Then I should see the selected filter value/s removed from the filter overlay 
	And the second filter value remains in filter overlay 
	
#new scenario	
Scenario Outline: Verify the deletion of filter values 
	Given I added <count> filter value/s to advanced filter overlay 
	And I should see filter value/s added in filter overlay 
	When I select "Delete" button 
	Then I should see all filter value/s removed from filter overlay 
	Examples: 
		|count  | 	
		|one    | 
		|two    |
		|four   |	
	
Scenario Outline: Break the group conditions with two filter values 
	Given I added <count> filter values to advanced filter overlay 
	When I apply '<condition>' condition to <count> filter values 
	Then I should see filter condition '<condition>' applied in filter values 
	When I select "BREAK" button 
	Then each filter conditions splitted as separate
	Examples: 
		|condition|count | 	
		|OR       |two   |
		|AND      |two   |
		|OR       |three |
		|AND      |three |
		
Scenario: Break the group conditions with four filter values 
    Given I added four filter value/s to advanced filter overlay 
	When I apply 'AND' condition to first two filter values 
	Then I should see condition applied for first two values 
	And I apply 'OR' condition to last two filter values 
	Then I should see condition applied for last two values 
	When I select both filter values with group condition applied 
	And I select "BREAK" button 
	Then each filter conditions splitted as separate	