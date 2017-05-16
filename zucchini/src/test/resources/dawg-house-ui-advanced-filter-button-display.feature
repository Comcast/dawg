@uitest 
Feature: Various button(Add, Search, AND, OR, NOT, DEL, BREAK) display verification in STB device Advanced Filter Overlay.

Background: 
	Given I am on advanced filter overlay 
	
Scenario: Verify button display in advanced filter overlay using single filter values 
	Given I added one filter value/s to advanced filter overlay 	
	Then I should see filter value/s added in filter overlay 
	And I should verify following buttons displayed as below 
		|Button  |Enabled  | 
		|AND     |false    |
		|OR      |false    |
		|NOT     |true     | 
		|DEL     |true     | 		
		|BREAK   |false    |
		|Add     |true     | 		
		|Search  |true     |
		
Scenario: Verify button display in advanced filter overlay using multiple filter values 
	Given I added two filter value/s to advanced filter overlay 	
	Then I should see both filter value/s added in filter overlay 
	And I should verify following buttons displayed as below
		|Button  |Enabled  | 
		|AND     |true     |
		|OR      |true     |
		|NOT     |true     | 
		|DEL     |true     | 		
		|BREAK   |false    |
		|Add     |true     | 		
		|Search  |false    |
		
# new scenario
Scenario Outline:
Verify button display in advanced filter overlay while de-selecting all filter value/s 
	Given I added <Count> filter value/s to advanced filter overlay	
	Then I should see filter value/s added in filter overlay 
	And I should verify all filter value checkboxes as selected 
	When I uncheck filter value/s 
	And I should verify following buttons displayed as below 
		|Button  |Enabled| 
		|AND     |false  | 	
		|OR      |false  | 	
		|NOT     |false  | 	 
		|DEL     |false  | 		
		|BREAK   |false  |
		|Add     |true   | 		
		|Search  |false  |	
	Examples: 
		|Count  | 		
		|1      | 		
		|2      |	
Scenario Outline: Verify button display while selecting condition buttons(AND, OR, AND NOT, OR NOT) 
	Given I added two filter value/s to advanced filter overlay 
	When I select condition button/s "<button>" 
	Then I should verify following buttons displayed as below
		|Button  |Enabled | 
		|AND     |false   |
		|OR      |false   |
		|NOT     |true    | 
		|DEL     |true    | 		
		|BREAK   |true    |		
		|Add     |true    | 		
		|Search  |true    |
	Examples: 
		|button  | 	
		|OR      | 
		|AND     |		
		|OR, NOT | 
		|AND, NOT| 		
#new scenario
Scenario: Verify button display while selecting condition button NOT 
	Given I added two filter value/s to advanced filter overlay 
	When I select condition button/s "NOT" 
	Then I should verify following buttons displayed as below
		|Button  |Enabled| 
		|AND     |true   |
		|OR      |true   |
		|NOT     |true   | 
		|DEL     |true   | 		
		|BREAK   |false  |
		|Add     |true   | 		
		|Search  |false  |	
#new scenario		
Scenario Outline: Verify the button display while deleting filter value/s from filter table 
	Given I added <count> filter value/s to advanced filter overlay  
	Then I should see filter value/s added in filter overlay 
	When I select "Delete" button 
	Then I should see filter value/s removed from filter overlay 
	And I should verify following buttons displayed as below
	    |Button  |Enabled|
	    |Add     |true   |
		|AND     |false  |
		|OR      |false  |
		|NOT     |false  | 
		|DEL     |false  | 		
		|BREAK   |false  | 
		|Search  |false  |
	Examples: 
		|count  | 	
		|one    | 
		|two    |