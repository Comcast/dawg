@uitest 
Feature: UI verification of Dawg House's Tag Cloud behaviours 

Background: 
	Given I am on the dawg house home page 
	
Scenario: Verify the tag highlight during tag selection 
	When I select any tag element in tag cloud 
	Then I should see the tag element highlighted 
	And I select another tag element 
	Then I should see both tag elements highlighted 
	
	# new scenario
Scenario: Verify the tag highlight during tag de-selection 
	Given I selected two tags elements in tag cloud 
	When I de-select first tag element 
	Then I should see highlight removed from first tag element 
	And highlight remains on second tag element 
	When I de-select the second tag element 
	Then I should see highlight removed from both tags elements 
	
	# new scenario 
Scenario: Verify STB list displayed in filter table on tag selection 
	When I select any tag element in tag cloud 
	Then I should see the STB list for the selected tag 
	And I select another tag element 
	Then I should see the STB list for both the tags selected 
	
	# new scenario 
Scenario: Verify STB list displayed in filter table based on tag de-selection 
	Given I selected two tag elements in tag cloud 
	And the STB list for both tags is displayed 
	When I de-select first tag element 
	Then I should see the STB list for first tag element is removed 
	And the STB list for second tag element remains same 
	When I de-select second tag element 
	Then I should see all available STBs in Dawg House 
	
	# new scenario 
Scenario: Selection of tags having common STB list 
	When I select any tag element in tag cloud 
	Then I should see the STB list for the selected tag element 
	When I select another tag element which has same STB list in first tag 
	Then I should see the STB list displayed remains same 
	
Scenario: De-selection of tags having common STB list 
	Given I selected two tags elements having same STB list 
	And the STB list for both tags is displayed 
	When I de-select the first tag element 
	Then I should see the STB list displayed remains same 
	
Scenario: Verify tag highlight remains on first tag, while adding a new tag to tag cloud 
	Given I have a tag element having multiple tagged STBs 
	And the STB list of selected tag is displayed 
	When I tag one STB to a new tag 
	Then I should see the STB added to new tag in tag cloud 
	And the highlight remains on the first tag 
	
	#new feature added     
Scenario: Verify STB list remains same, on adding an STB to a new tag in tag cloud 
	Given I have a tag element having multiple tagged STBs 
	And the STB list of selected tag is displayed 
	When I tag one STB to a new tag 
	Then I should see the STB list displayed remains same 
	
Scenario: Deletion of an STB from a tag 
	Given I selected 2 tags in tag cloud which has same STB list 
	And the STB list of selected tags is displayed 
	When I select one STB from the STB list displayed 
	Then I should see delete option appears in both tags 
	When I delete the selected STB from any of the above tag 
	Then I should see STB removed from the selected tag in tag cloud	
	
Scenario Outline: Deletion of tag from tag cloud 
	Given I selected 2 tags with same <count> STBs
	And the STB list of selected tags is displayed	
	When I select all the STBs displayed
	Then I should see delete option appears in both tags 
	When I delete any one of the selected tag 
	Then I should see the tag removed from tag cloud
	Examples: 
	| count |
	|   1   |
	|   3   |
	
Scenario: Verify the tag highlight while deleting STB from tag 
	Given I selected a tag element having multiple tagged STBs 
	And the tag element is highlighted 
	When I delete an STB from the tag element 
	Then I should see the STB removed from the tag 
	And the highlight remains on the tag element 
	
Scenario: Verify Delete option in tags while selecting a common STB in two tags 
	Given I selected 2 tags in tag cloud which has same STB list 
	And the STB list of selected tags is displayed 
	When I select one STB from the STB list displayed 
	Then I should see the delete option displayed for both tags 
	When I remove the selection from any selected tag element 
	Then the checked status of selected STB remains same 
	
Scenario: Verify Delete option in tags while selecting any STB from the STB list 
	Given I have two tags tag1 and tag2 with same STB list
	When I select tag1 and tag3 which has different STB list 
	Then I should see the STB list displayed for all tags   
	When I select one STB which belongs to tag1
	Then I should see the delete option displayed for tag1 and tag2
	When I remove the selection from tag1
	Then I should see the delete option disappears from tag1 and tag2