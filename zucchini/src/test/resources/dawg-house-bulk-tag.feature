@uitest 
Feature: Verify Dawg house's bulk tag behaviours. 

Background: 
	Given I am on the home page of dawg house portal 
	
Scenario: Tag a box has no tags to new tag 
    Given I have a box that has no tags
	When I add the box to a new tag
	Then I should see the box is added to new tag in tag cloud 
	
Scenario: Tag a box has no tags to an existing tag 
    Given I have a box that has no tags
	When I add the box to an existing tag 
	Then I should see the box is added to existing tag in tag cloud 
	
Scenario: Tag a box that has one tag to new tag 
    Given I have a box that has one tag
	When I add the box to a new tag 
	Then I should see the box is added to new tag in tag cloud 
	And the box is tagged with two tags
	
Scenario: Tag a box has no tags to multiple tags  
    Given I have a box that has no tags  
	When I tag the box with three different tags 
	Then I should see the box added to three tags in tag cloud 
	And the box is tagged with three tags
	
Scenario: Tag a box with multiple tags if the box already tagged to one of those tags 
    Given I have a box that has one tag
	When I add the box to the same tag  
	Then I should see the box added remains same in tag cloud
	And I add another existing tag to that box 
	Then I should see the box added to existing tag 
	
Scenario: Tag multiple boxes to a new tag 
	When I tag multiple boxes to new tag 
	Then I should see all the boxes added to the new tag 
	
Scenario: Verify the alert message when add a tag without selecting any box 
	When I add a tag without selecting the box 
	Then I should see the alert message "No devices selected" on screen 
		
#new test case 		
Scenario: Tag multiple boxes to an existing tag 
	When I tag multiple boxes to an existing tag 
	Then I should see all the boxes selected is added to the existing tag 
	
#new test case 
Scenario: Tag a box that has one tag to another existing tag 
    Given I have a box that has one tag
	When I add that box to an existing tag 
	Then I should see the box is added to existing tag in the tag cloud 
	And the box is present in both tags in tag cloud 