@uitest 
Feature: Dawg house's bulk tag behaviours. 
	Verify behavior when logging into the dawg house portal

Background: 
	Given I am on the Log In page of dawg house portal 
	
Scenario: Tag a box has no tags 
	When I add a box having no tags to new tag 
	Then the box added to the newly added tag in the tag cloud 
	
Scenario: Tag a box has no tags to already existing tag [doubt]
	When I add a box having no tags to existing tag 
	Then verify the box added to existing tag 
	
Scenario: Tag a box that contains one tag 
	When I tag a box having one tag to new tag 
	Then verify the box added to the new tag 
	
Scenario: Tag a box with multiple tags 
	When I tag a box with multiple tags 
	Then the box added to the multiple tags in tag cloud 
	
Scenario: Tag a box with multiple tags and the box already tagged to one of those tags 
	When I Tag a box with multiple tags that already tagged to one of those tags 
   Then the box tagged to new tag and the box remains same in the alreday existing tag  
   	
Scenario: Tag a box with an existing tag 
	When I tag a box with an existing tag in tag cloud
	Then the box added to the existing tag 

Scenario: Tag multiple boxes to new tag
	When I tag multiple boxes to new tag
	Then the selected boxes added to the new tag in tag cloud
	
Scenario: Verify the alert message when add a tag without selecting any box
	When I add a tag without selecting the box
	Then an alert message will be displayed
	

	