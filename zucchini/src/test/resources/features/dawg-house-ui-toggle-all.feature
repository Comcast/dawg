@uitest @uitest2 @toggleall
Feature: Verify Dawg House's toggle all UIbehaviours 

Background: 
	Given I am on the dawg house home page 

@uitest21
Scenario: Verify the toggle all behaviour when no tags selected 
	When I select toggle all checkbox 
	Then I should see all STB checkboxes as checked 
	And I uncheck toggle all checkbox 
	Then I should see all STB checkboxes as unchecked 	

@uitest22
Scenario: Verify the toggle all behaviour when single tag selected 
	Given I selected any tag element in tag cloud	
	When I select toggle all checkbox 
	Then I should see all STB checkboxes as checked
	And the delete option of tag should be displayed	

@uitest23
Scenario: Verify the toggle all behaviour when multiple tag selected 
	Given I selected any tag element in tag cloud	
	When I select toggle all checkbox 
	Then I should see all STB checkboxes as checked
	When I select another tag element in tag cloud
	Then I should see the toggle all checkbox as unchecked
	And the STB checkboxes of first tag remains as checked