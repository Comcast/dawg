@uitest
Feature: Bulk tag behaviours. 

Background: 
	Given I am on the dawg house home page 	
@bulkTag  @smoke	
Scenario: Tag a box that has no tags to new tag 
	Given I have a box that has no tags 
	When I add the box to a new tag 
	Then I should see the box is added to new tag in tag cloud
	And the box is tagged with 1 tag in tag cloud  
@bulkTag @bulkTagwithSTB @smoke
Scenario: Tag a box that has no tags to an existing tag 
	Given I have a box that has no tags 
	When I add the box to an existing tag 
	Then I should see the box is added to existing tag in tag cloud 
	And the box is tagged with 1 tag in tag cloud 
@bulkTagwithSTB
Scenario: Tag a box that has one tag to new tag 
	Given I have a box that has one tag 
	When I add the box to a new tag 
	Then I should see the box is added to new tag in tag cloud 
	And the box is tagged with 2 tags in tag cloud 
@bulkTag 
Scenario Outline: Tag a box has no tags to multiple tags 
	Given I have a box that has no tags 
	When I tag the box with <count> different tags 
	Then the box is tagged with <count> tags in tag cloud	
	Examples: 
		| count |
		|   2   |
		|   3   |
@bulkTagwithSTB
Scenario: Tag a box with multiple tags if the box already tagged to one of those tags 
	Given I have a box that has one tag 
	When I add the box to the same tag 
	Then I should see the box added remains same in tag cloud 
	And I add the box to an another existing tag
	Then I should see the box is added to existing tag in tag cloud 
@bulkTagwithSTB 
Scenario: Tag multiple boxes to a new tag 
	When I tag multiple boxes to a new tag 
	Then I should see all the boxes added to the new tag 
@bulkTagwithSTB	 @smoke	
Scenario: Verify the alert message when add a tag without selecting any box 
	When I add a tag without selecting the box 
	Then I should see the alert message 'No devices selected' on screen 	
@bulkTagwithSTB 
Scenario: Tag multiple boxes to an existing tag 
	When I tag multiple boxes to an existing tag 
	Then I should see all the boxes added to the existing tag 	
@bulkTagwithSTB		
Scenario: Tag a box that has one tag to another existing tag 
	Given I have a box that has one tag 	
	When I add the box to an another existing tag
	Then I should see the box is added to existing tag in tag cloud 
	And the box is tagged with 2 tags in tag cloud 