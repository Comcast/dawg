Feature: Verify UI behaviours for Dawg house's STB model 

Background: 
	Given I am on the model configuration page 
	
Scenario: Add a new capability name on model configuration page 
	Given I am on model overlay page 
	And I should see the list of capabilities displayed in the model overlay 
	When I add a new capability to the model overlay 
	Then I should see the capability added is displayed on model overlay 
	
Scenario: Verify add family name on model overlay page 
	Given I am on model overlay page 
	When I add a new family name to the model overlay 
	Then I should see the family named added to model overlay 
	And I reopen the model overlay page 
	Then I should see the family name persists on model overlay page 
	
Scenario Outline: Verify the alert message while trying to add a duplicate enty 
	Given I am on model overlay page 	
	When I add an already existing <property> name to model overlay page 
	Then I should see the alert message '<property> already found' 	
	
	Examples: 
		|property|  
		|capability|		
		|family|
		|model|
		
Scenario: Add a new model on model configuration page 
	Given I am on model overlay page 
	When I add a new model with capability and family name 
	Then the new model page is loaded 
	And I should verify the capabilities and famility name is added to the model page 
	
Scenario: Edit a model from model configuration page 
	When I click on model row to view the edit dialog box 
	Then I should see the model name field is disabled 
	And I modify all the editable text fields in the dialog box 
	Then I should see the modified fields get reflected in the model page
	
Scenario: Delete a model from the model configuration page 
	When I click on the Delete model button 
	Then an alert message should be displayed 
	And I select ok on the alert box 
	Then the model will be removed from the configuration page 