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
Scenario: Verify STB's list in filter table based on tag selection 
	When I select any tag element 
	Then I should see the STB's listed for the selected tag 
	And I select another tag element 
	Then I should see the STB's listed for both the tags selected 
	
	# new scenario 
Scenario: Verify STB's list in filter table based on tag de-selection 
	Given I selected any two tag elements 
	Then the STB's' list for both tags should be displayed 
	When I de-select first tag element 
	Then I should see the STB's listed for first tag element is removed 
	And the STB's listed for second tag element remains same 
	When I de-select second tag element 
	Then I should see the STB's listed for all tags in tag cloud 
	
	# new scenario 
Scenario: Selection of tags having common STB's list 
	When I select any tag element 
	Then I should see the STB's listed for the selected tag element 
	When I select another tag element which has same list of STBs in first tag 
	Then I should see the STB's list displayed remains same 
	
Scenario: De-selection of tags having common STB's list 
	Given I selected any two tags elements having same STB's list 
	Then I should see the STBs listed for both tag elements 
	And I de-select the first tag element 
	Then I should see the STB's list displayed remains same 
	
Scenario: 
	Verify tag highlight remains on first tag when adding a new tag to tag cloud 
	Given I have a tag element having two tagged STBs 
	And the STB's list of selected tag should be displayed 
	When I tag one STB to a new tag 
	Then I should see the STB added to new tag in tag cloud 
	And the highlight remains on the first tag 
	
	#new feature added     
Scenario: Verify the STB's list in filter table while tagging one STB to new tag 
	Given I have a tag element having two tagged STBs 
	Then the STB's list of selected tags should be displayed 
	And I select one STB to add to a new tag 
	Then I should see the same STB's list displayed 
	
Scenario: Deletion of STB from a tag 
	Given I have a common stb in any two tags in tag cloud 
	And I should see delete option appears in both tags 
	When I delete the selected STB from any of the above tag 
	Then I should see STB removed from the selected tag 
	
Scenario: Deletion of tag from tag cloud 
	Given I have a common stb in any two tags in tag cloud 
	And I should see delete option appears in both tags 
	When I delete any tag having single STB 
	Then I should see the tag removed from tag cloud 
	
Scenario: Verify the tag highlight while deleting STB from tag 
	Given I have a tag element having two tagged STBs 
	And I should see the tag element as highlighted 
	When I delete the selected STB content from the tag element 
	Then the I should see STB removed from the tag 
	And the highlight remains on the tag element 
	
Scenario: Verify Delete option in tags while selecting a common STB in two tags 
	Given I selected any two tags elements having same STB's list 
	When I select one STB from the stb list displayed 
	Then I should see the delete option displayed for both tags 
	When I remove the selection from first tag element 
	Then I should see the highlight remains on first tag element 
	And the checked status of selected STBs remains same 
	
Scenario: Delete option display in tags while selecting an STB 
	Given I selected any two tags elements having more than one STBs 
	Then  I should see the STB's list displayed 
	When I select checkbox of one STB 
	Then the delete option appears in both the tags 
	And I remove the selection from first tag element 
	Then the delete option disappears from the tags 
	
Scenario: Verify Delete option in tags while selecting any STB from the STB's list 
	Given I selected any two tags elements having more than one STBs 
	When I select one STB from the stb list displayed 
	Then I should see the delete option appears in tags which contains the selected STB 
	When I remove the selection from first tag element 
	Then the delete option disappears from the tags 		