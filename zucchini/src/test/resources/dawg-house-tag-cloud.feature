@uitest 
Feature: UI verification of Dawg House's Tag Cloud behaviours 

Background: 
	Given I am on the dawg house home page 
	
Scenario: Verify the tag highlight during tag selection 
	When I select any tag element 
	Then I should see the tag element as highlighted
	And I select another tag element 
	Then I should see both tag elements as highlighted 	
	
# new scenario
Scenario: Verify the tag highlight during tag de-selection 
	Given I selected any two tags elements 
	When I de-select first tag element 
	Then I should see highlight removed from first tag element 
	And highlight remains on second tag element 
	When I de-select the second tag element 
	Then I should see highlight removed from both tags elements 
		
# new scenario 
Scenario: Verify STB listings in filter table based on tag selection 
	When I select any tag element 
	Then I should see the STB's listed for the selected tag 
	And I select another tag element 
	Then the STB listings for both the tags should be displayed 
		
# new scenario 
Scenario: Verify STB listings in filter table based on tag de-selection 
	Given I selected any two tags elements 
	Then the STB listings for both tags should be displayed 
	When I de-select first tag element 
	Then I should see the STBs listed  for first tag element is removed 
	And the STB's listed for second tag element remains same 
	When I de-select second tag element 
	Then I should see the STB's listed for all tags in tag cloud 
		
# new scenario 
Scenario: Selection of tags having common STB list 
	When I select any tag element 
	Then the STB lists of selected tag should be displayed 
	And I choose another tag having same list of STBs in first tag 
	Then the same STB list should be displayed 
	
Scenario: De-selection of tags having common STB list 
	Given I selected any two tags elements having same STB lists 
	Then I should see the STB lists displayed 
	And I de-select the first tag element 
	Then same STB list should be displayed 
	
Scenario: Tag highlight remains on first tag when adding a new tag to tag cloud 
	Given I have a tag element having two tagged STBs 
	Then the STB lists of selected tag should be displayed 
	When I tag one STB to a new tag 
	Then I should see the STB added to new tag in tag cloud 
	And the highlight remains on the first tag 
	
	#new feature added     
Scenario: 
	Verify the STB lists in filter table while tagging one STB to new tag 
	Given I have a tag element having two tagged STBs 
	Then the STB lists of selected tag should be displayed 
	And I choose one STB to add to a new tag 
	Then same STB list should be displayed 
	
Scenario: Deletion of STB from a tag 
	Given I have an stb common in any two tags in tag cloud 
	Then I should see delete option appears in both the tags 
	And I delete the selected STB content from the tag 
	Then I should see STB removed from the tag 
	
Scenario: Deletion of tag from tag cloud 
	Given I have an stb common in any two tags in tag cloud 
	Then I should see delete option appears in both the tags 
	And I delete the tag having single STB 
	Then I should see the tag removed from tag cloud 
	
Scenario: Verify the tag highlight while deleting STB from tag 
	When I select any tag element 
	Then I should see the tag element as highlighted
	And I delete the selected STB content from the tag element 
	Then the I should see STB removed from the tag 
	And the highlight remains on the tag element 
	
Scenario: Delete option display in tags while selecting an STB common among two tags 
	Given I selected any two tags elements having same STB lists 
	Then I should see the STB lists displayed 
	When I select checkbox of one STB 
	Then the delete option appears in both the tags 
	And I remove the selection from first tag element 
	Then the checked status of STB and delete option remains in the tag 
	
Scenario: Delete option display in tags while selecting an STB 
	Given I selected any two tags elements having more than one STB lists 
	Then  I should see the STB lists displayed 
	When I select checkbox of one STB 
	Then the delete option appears in both the tags 
	And I remove the selection from first tag element 
	Then the delete option disappears from the tags 
		