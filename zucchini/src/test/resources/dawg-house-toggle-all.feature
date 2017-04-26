@uitest 
Feature: Dawg House's toggle all check box behaviours 

Background: 
	Given I am on the dawg house home page 
	
Scenario: Verify the toggle all behaviour in DAWG House when no tags selected 
	When I choose toggle all checkbox 
	Then all the STB check boxes will be displayed as selected 
	And uncheck the toggle all checkbox 
	Then validate all STB checkboxes are unchecked 	
	
Scenario: Verify the toggle all behaviour in DAWG House when single tag selected 
	When I select first tag element 
	Then the tagged stb details of first tag will be listed 
	And I choose toggle all checkbox 
	Then all STB checkboxes are checked and delete option of tag will be displayed 	
	
Scenario: Verify the toggle all behaviour in DAWG House when multiple tag selected 		
	When I select toggle all checbox after selecting the first tag element
	Then all STB checkboxes are checked
	And I select second tag element 
	Then toggle all checkbox is unchecked and the STB checkboxes of first tag remains checked 
	And select the toggle all checkbox again to validate all STBs are checked
	Then remove the first tag and verify toggle all checkbox is uncheckd
	
	
	
	
	