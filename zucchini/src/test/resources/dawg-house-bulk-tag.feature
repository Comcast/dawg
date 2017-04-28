@uitest 
Feature: Dawg house's bulk tag behaviours. 

Background: 
	Given I am on the home page of dawg house portal 
	
Scenario: Tag a box has no tags to new tag 
	When I add a box having no tags to new tag 
	Then the box added to the newly added tag in the tag cloud 
	
Scenario: Tag a box has no tags to an existing tag 
	When I add a box having no tags to an existing tag 
	Then the box should be added to existing tag 
	
Scenario: Tag a box that contains one tag to new tag 
	When I tag a box having one tag to new tag 
	Then the box should be added to new tag 
	
	#new feature added	
Scenario: Tag a box that contains one tag to existing tag 
	When I tag a box having one tag to existing tag 
	Then the box should be added to existing tag 
	
Scenario: Tag a box with multiple tags 
	When I tag a box with multiple tags 
	Then the box added to the multiple tags in tag cloud 
	
Scenario: Tag a box with multiple tags and the box already tagged to one of those tags 
	When I add a box having tag to that tag 
	Then the box added remains same in the tag 
	And I add another tag to that box 
	Then I should see the box added to that tag 
	
Scenario: Tag multiple boxes to new tag 
	When I tag multiple boxes to new tag 
	Then I should see the boxes added to the new tag 
	
	#new feature added		
Scenario: Tag multiple boxes to existing tag 
	When I tag multiple boxes to an existing tag 
	Then I should see the boxes added to the existing tag 
	
Scenario: Verify the alert message when add a tag without selecting any box 
	When I add a tag without selecting the box 
	Then an alert message should be displayed 
	
	
	