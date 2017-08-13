@uitest @advanceSearch  
Feature: Advanced Filter Search behaviours. 

Background: 
	Given I am on advanced filter overlay 

Scenario: Verify the search results using multiple filter values 
	Given I added 1 filter value to advanced filter overlay 	 
	Then I should see filter value added in filter overlay 
	When I select 'Search' button in advanced filter overlay	
	Then I should see the search results displayed in the filter table 	

Scenario Outline: Verify the search results while selecting condition buttons (AND,OR,NOT) 
	Given I added 2 filter values to advanced filter overlay 
	And I should see filter values added in filter overlay 
	When I select condition button/s '<button>' 
	Then I should see condition '<button>' applied to filter values
	When I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 		
		|OR      | 
		|AND     |		
		|OR, NOT |	
		|AND, NOT|

Scenario: Verify the search results with singel filter value using NOT condition
	Given I added 1 filter values to advanced filter overlay 
	And I should see filter values added in filter overlay 
	When I select condition button/s 'NOT' 
	Then I should see condition 'NOT' applied to filter values 
	When I select 'Search' button in advanced filter overlay 
	Then I should see the search results displayed in the filter table

Scenario Outline: Verify the search results using single filter values 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I select 'Add' button in advanced filter overlay 
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 
	When I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed in the filter table 
	Examples: 
		| field                 | option  | value               |  		
		| Id                    | equals  | testId              |
		| Model                 | matches | Test.*              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 
		| Hardware Revision     | contains| TestHardwareRevision|  
		| Capabilities          | contains| TestCap             |
	
Scenario Outline: Verify the search results using single filter values with NOT condition 
	Given there is an advanced filter with "<field>", "<option>", "<value>" 
	When I select 'Add' button in advanced filter overlay
	And I select condition button/s 'NOT'	
	Then I should see filter "<field>" "<option>" "<value>" values added in filter overlay 	
	When I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed for NOT condition in the filter table 
	Examples: 
		| field                 | option  | value               |  		
		| Id                    | equals  | testId              |
		| Model                 | matches | Test.*              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 
		| Hardware Revision     | contains| TestHardwareRevision|  
		| Capabilities          | contains| TestCap             |
					

Scenario Outline: Verify checkbox selection while adding filter values to filter overlay 
	Given I added <count> filter value/s to advanced filter overlay 	
	Then I should see filter value/s added in filter overlay 
	And I should verify all filter value checkboxes as selected 
	Examples: 
		|count | 	
		|1     | 
		|2     |
		|3     |	
Scenario: Verify search results by applying filter conditions together (AND and OR) 
	Given I added 4 filter values to advanced filter overlay 
	When I apply 'AND' condition to last 2 filter values 
	Then I should see 'AND' condition applied for first two values 
	And I apply 'OR' condition to first 2 filter values 
	Then I should see 'OR' condition applied for last two values 	
	And I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed in the filter table
	
Scenario Outline: Verify the search results using multiple filter values with conditions(AND, OR, OR NOT, AND NOT)
	Given I added following filter values to advanced filter overlay 
		| field                 | option  | value               |
		| Model                 | matches | Test              |  
		| Make                  | contains| Test                | 
		| Rack Name             | contains| Test                | 
		| Controller Ip Address | contains| TestIp              | 
		| Slot Name             | contains| TestSlotName        | 
		| IR Blaster Type       | contains| TestIrBlasterType   | 	
	Then I should see filter values added in filter overlay 
	When I select condition button/s '<button>' 
	Then I should see condition '<button>' applied for all filter values 
	When I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed in the filter table
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |		
		|OR, NOT | 
		|AND, NOT| 	
		
Scenario Outline: Verify search results by applying group conditions (AND and OR) and applying single conditions  
	Given I added 4 filter values to advanced filter overlay 
	When I apply 'AND' condition to first 2 filter values 	
	And I apply 'OR' condition to last 2 filter values 	
	Then I should see 'AND, OR' condition applied for the filter values
	When I select both filter values with group condition applied
	And I select condition button/s '<button>' 
	Then I should see condition '<button>' applied for all filter values
	When I select 'Search' button in advanced filter overlay
	Then I should see the search results displayed in the filter table 
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |		
		|OR, NOT | 
		|AND, NOT| 

Scenario: Verify the deletion of single filter value 
	Given I added 2 filter values to advanced filter overlay 
	Then I should see filter values added in filter overlay 
	When I select first filter value to delete 
	When I select 'DEL' button in advanced filter overlay 
	Then I should see selected filter value removed from filter overlay 
	And second filter value remains in filter overlay 
	
Scenario Outline: Verify the deletion of filter values 
	Given I added <count> filter value/s to advanced filter overlay 
	And I should see filter value/s added in filter overlay 
	When I select 'DEL' button in advanced filter overlay 
	Then I should see all filter value/s removed from filter overlay 
	Examples: 
		|count| 	
		|1    | 
		|2    |
		|3    |
Scenario Outline: Break the group conditions with two filter values 
	Given I added <count> filter values to advanced filter overlay 
	When I apply '<condition>' condition to <count> filter values 	
	Then I should see '<condition>' condition applied for the filter values		
	When I select 'BREAK' button in advanced filter overlay
	Then each filter conditions splitted as <count>
	Examples: 
		|condition|count| 	
		|OR       |2    |
		|AND      |2    |
		|OR       |3    |
		|AND      |3    |	
Scenario: Break the group conditions with four filter values 
    Given I added 4 filter values to advanced filter overlay 
	When I apply 'AND' condition to first 2 filter values 
	Then I should see 'AND' condition applied for first two values 
	And I apply 'OR' condition to last 2 filter values 
	Then I should see 'OR' condition applied for last two values 
	When I select both filter values with group condition applied 
	And I select 'BREAK' button in advanced filter overlay
	Then each filter conditions splitted as 4	