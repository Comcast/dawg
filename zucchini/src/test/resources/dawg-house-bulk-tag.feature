@uitest 
Feature: Verify Dawg house's bulk tag behaviours. 

Background: 
	Given I am on the home page of dawg house portal 
	
Scenario: Tag a box has no tags to new tag 
    Given I have a box that has no tags
	When I add the box to a new tag
	Then I should see the box is added to new tag in the tag cloud 
	
Scenario: Tag a box has no tags to an existing tag 
    Given I have a box that has no tags
	When I add the box to an existing tag 
	Then I should see the box is added to existing tag in the tag cloud 
	
Scenario: Tag a box that has one tag to new tag 
    Given I have a box that has one tag
	When I add the box to a new tag 
	Then I should see the box is added to new tag in the tag cloud 
	And the box is present in both tags in the tag cloud 
	
Scenario: Tag a box with multiple tags    
	When I tag a box with multiple tags 
	Then the box added to the multiple tags in tag cloud 
	
Scenario: Tag a box with multiple tags and the box already tagged to one of those tags 
    Given I have a box that has one tag
	When I add the box to the same tag  
	Then I should see the box added remains same in the tag cloud
	And I add another tag to that box 
	Then I should see the box added to that tag 
	
Scenario: Tag multiple boxes to a new tag 
	When I tag multiple boxes to new tag 
	Then I should see all the boxes added to the new tag 
	
Scenario: Verify the alert message when add a tag without selecting any box 
	When I add a tag without selecting the box 
	Then I should see the alert message displayed on screen 
		
	#new test case 		
Scenario: Tag multiple boxes to an existing tag 
	When I tag multiple boxes to an existing tag 
	Then I should see all the boxes selected is added to the existing tag 
	
	#new test case 
Scenario: Tag a box that has one tag to another existing tag 
    Given I have a box that has one tag
	When I add that box to an existing tag 
	Then I should see the box is added to existing tag in the tag cloud 
	And the box is present in both tags in the tag cloud 