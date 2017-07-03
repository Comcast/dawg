@uitest @stbModel
Feature: Verify UI behaviours for Dawg house's STB model 

Background: 
	Given I am on the model configuration page 

Scenario Outline: Add a new model property on model configuration page 
	Given I am on model overlay 
	Then I should see <property> list displayed in model overlay 
	When I add a new <property> name to the model overlay 
	Then the <property> name added displayed in model overlay 
	When I reopen the model overlay page 
	Then I should see the <property> name persists on model overlay page 
	Examples: 
		|property  |  
		|capability|		
		|family    |
		
Scenario Outline: Verify the alert message while trying to add a duplicate enty 
	Given I am on model overlay 
	When I add an already existing <property> name to model overlay page 
	Then I should see the alert message <property> name added 'already exists' 
	Examples: 
		|property  |  
		|capability|		
		|family    |
		|model     |			

Scenario: Add a new model on model configuration page 
	Given I am on model overlay 
	When I add a new model with capability and family name 
	Then I should see model page is loaded 
	And I should verify model properties added to the model page 
	
Scenario: Edit a model from model configuration page 
	When I click on model row to view the edit dialog box 
	Then I should see the model name field is disabled 
	And I modify all the editable text fields in the dialog box 
	Then I should see the modified fields get reflected in the model page 

Scenario: Delete a model from the model configuration page 
	When I select 'delete model' button 
	Then an alert message 'Are you sure you want to delete' should be displayed 
	And I select ok on the alert box 
	Then the model will be removed from the configuration page 
	#new scenario
	
Scenario: Verify the select status of Model properties(Capabilities and family) in model overlay 
	Given I choose a model from the model configuration page 
	Then model overlay page is displayed 
	And I should see model properties are displayed as selected 
