Feature: Dawg House's STB Model behaviour. 

Background: 
	Given I am on the model configuration page. 
	
Scenario: Add a new capability name on model configuration page 
	When I click on the Add model button I can see model overlay 
	Then a lists of capabilities are available on the model overlay 
	Then add a new capability to the model overlay 
	And capability added listed on the model overlay 
	
Scenario: Add a new family name on model overlay page 
	When I click on the Add model button I can see model overlay 
	Then add a new family name 
	And the newly added family name persist on the page 
	
Scenario Outline: Verify the alert message while trying to add a duplicate enty 
	When I click on the Add model button I can see model overlay 
	Then enter an already existing <property> name 
	And verify the alert message during the addition of duplicate <property> name 
	
	Examples: 
		|property|  
		|capability|		
		|family|
		|model|
		
Scenario: Add a new model on model configuration page 
	When I click on the Add model button I can see model overlay 
	Then add a new model name with capability and family name 
	And verify the model page is loaded 
	Then verify capabilities and famility name added with model get reflected in the page 
	
Scenario: Edit a new model on model configuration page 
	When I click on model row to view the edit dialog box 
	Then Model name text field is disabled state 
	And Fill all the editable text fields in the edit dialog box 
	Then capabilities and famility name added with model get reflected in the page 
	
Scenario: Delete a model from the model configuration page 
	When I click on the Delete model button 
	Then the model will be removed from the page 
	
	
