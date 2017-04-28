@uitest 
Feature: Dawg House's Tag Cloud behaviours 

Background: 
	Given I am on the dawg house home page 
	
Scenario: Verify the tag highlight during tag selection 
	When I select first tag element 
	Then I highlight should be on the first tag element 
	And I select second tag element 
	Then I should see both tag elements should be highlighted 
	# new scenario added
Scenario: Verify the tag highlight during tag De-selection 
	When I select any two tags elements 
	Then I should see both tag elements should be highlighted 
	And I de-select first tag element 
	Then the highlight removed from the first tag and remains on the second tag 
	And I de-select the second tag element 
	Then the highlight should be removed from both tags 
	
	# new scenario added	
Scenario: Verify STB listings in filter table based on tag selection 
	When I select first tag element 
	Then I should see the STB listings of first tag 
	And I select second tag element 
	Then the STB listings of both tags should be displayed 
	# new scenario added	
Scenario: Verify STB listings in filter table based on tag de-selection 
	When I select any two tags elements 
	Then the STB listings of both tags should be displayed 
	And I de-select first tag element 
	Then the STB listings of first tag should be removed and second tag remains 
	And I de-select second tag element 
	Then I should see the STB listings of all tags in tag cloud 
	
	# new scenario added
Scenario: Selection of tags having common STB list 
	When I select first tag element 
	Then the STB lists of first tag should be displayed 
	And I choose second tag having same list of STBs in first tag 
	Then the same STB list should be displayed 
	
Scenario: De-selection of tags having common STB list 
	When I select any two tags having same STB lists 
	Then I should see the STB lists displayed 
	And I de-select the first tag element 
	Then same STB list should be displayed 	
		
Scenario: Tag highlight remains on first tag when adding a new tag to tag cloud 
	When I select a tag element having two tagged STBs 
	Then the STB lists of selected tag should be displayed 
	And I tag one STB to a new tag 
	Then I should see the STB added to new tag in tag cloud 
	And the highlight remains on the first tag 
	
	#new feature added     
Scenario: 
	Verify the STB lists in filter table while tagging one STB among the lists to new tag 
	When I select a tag element having two tagged STBs 
	Then the STB lists of selected tag should be displayed 
	And I choose one STB to add to a new tag 
	Then same STB list should be displayed 
	
Scenario: Deletion of STB from a tag 
	When I select one stb that is common in any two of the tags 
	Then verify delete option appears in both the tags 
	And I delete the selected STB content from the tag 
	Then I should see STB removed from the tag 
	
Scenario: Deletion of tag from tag cloud 
	When I select one stb that is common in any two of the tags 
	Then verify delete option appears in both the tags 
	And I delete the tag having single STB 
	Then I should see the tag removed from tag cloud 
	
Scenario: Verify the tag highlight while deleting STB from tag 
	When I select one tag element 
	Then the highlight should be on the tag element 
	And I delete the selected STB content from the tag element 
	Then the I should see STB removed from the tag 
	And the highlight remains on the tag element 
	
Scenario: Delete option display in tags while selecting an STB common among two tags 
	When I select any two tags having same STB lists 
	Then I should see the STB lists displayed 
	And I select checkbox of one STB 
	Then the delete option appears in both the tags 
	And I remove the selection from first tag element 
	Then the checked status of STB and delete option remains in the tag 	
	
Scenario: Delete option display in tags while selecting an STB 
	When I select any two tags having more than one STB lists 
	Then I should see the STB lists displayed 
	And I select checkbox of one STB 
	Then the delete option appears in both the tags 
	And I remove the selection from first tag element 
	Then the delete option disappears from the tags 
		