@uitest @tagcloud 
Feature: Tag Cloud behaviours 

Background: 
	Given I am on the dawg house home page 
		
Scenario: Verify the tag highlight during tag selection 
	When I select any tag element in tag cloud 
	Then I should see the tag element highlighted 
	And I select another tag element in tag cloud 
	Then I should see both tag elements highlighted 
	
Scenario: Verify the tag highlight during tag de-selection 
	Given I selected two tag elements in tag cloud 
	When I de-select first tag element 
	Then I should see highlight removed from first tag element 
	And highlight remains on second tag element 
	When I de-select second tag element 
	Then I should see highlight removed from both tag elements 
		
Scenario: Verify STB list displayed in filter table on tag selection 
	When I select any tag element in tag cloud 
	Then I should see STB list for the tag selected 
	And I select another tag element in tag cloud 
	Then I should see STB list for both the tags selected 
	
Scenario: Verify STB list displayed in filter table based on tag de-selection 
	Given I selected two tag elements in tag cloud 
	And I should see STB list for both the tags selected 
	When I de-select first tag element 
	Then I should see the STB list for first tag element is removed 
	And the STB list for second tag element remains same 
	When I de-select second tag element 
	Then I should see all available STBs in Dawg House 
@commonStbs
Scenario: De-selection of tags having common STB list 
	Given I selected two tag elements having same STB list 
	And the STB list of both tags are same 
	When I de-select the first tag element 
	Then I should see STB list displayed remains same 	
@commonStbs 
Scenario: Verify Delete option in tags while selecting common STB in two tags 
	Given I selected two tag elements having same STB list 
	And the STB list of both tags are same 
	When I select one STB from the STB list displayed 
	Then delete option appears in both tags 
	When I de-select the first tag element 
	Then the checked status of selected STB remains same 	
@commonStbs 
Scenario: Verify Delete option in tags while selecting any STB from the STB list 
	Given I selected tag1 and tag2 with different STB list 
	When I select tag1 and tag3 which has same STB list 
	Then I should see the STB list displayed for all selected tags 
	When I select one STB which belongs to tag1 
	Then delete option appears for tag1 and tag3 
	When I remove the selection from selected STB 
	Then delete option disappears from tag1 and tag3 	
@commonStbs  
Scenario: Selection of tags having common STB list 
	When I select any tag element in tag cloud 
	Then I should see STB list for the tag selected 
	When I select another tag element which has same STB list in first tag 
	Then I should see STB list displayed remains same 	
@commonStbs
Scenario: Deletion of tag from tag cloud 
	Given I selected two tag elements having same STB list 
	And the STB list of both tags are same 
	When I select all the STBs displayed 
	Then delete option appears in both tags 
	When I delete any one of the selected tag 
	Then I should see the tag removed from tag cloud 
		
Scenario Outline: Verify tag highlight remains on first tag, while adding a new tag/s to tag cloud 
	Given I select any tag element in tag cloud 
	And I should see STB list for the tag selected 
	When I tag <count> STB to a new tag 
	Then I should see the STB added to new tag in tag cloud 
	And highlight remains on first tag element 
	Examples: 
		|count|
		|1|
		|2| 
		#new feature added 
Scenario Outline: Verify STB list remains same, on adding an STB to a new tag/s in tag cloud 
	Given I select any tag element in tag cloud 
	And I should see STB list for the tag selected 
	When I tag <count> STB to a new tag 
	Then I should see STB list displayed remains same 
	Examples: 
		|count|
		|1|
		|2| 		
Scenario: Verify the tag highlight while deleting STB from tag 
	When I select any tag element in tag cloud 
	Then I should see the tag element highlighted 
	When I delete an STB from the tag element 
	Then I should see the STB removed from the tag in tag cloud 
	And highlight remains on the selected tag element 	
@deleteTag
Scenario: Deletion of STB content from a tag 
	Given I selected one tag having multiple tagged STBs 
	Then I select another tag having one STB which is tagged in the first tag 
	When I select common STB from the STB list displayed 
	Then I should see delete option appears in both tags 
	And I delete the selected STB content from first tag 
	Then I should see the STB removed from the tag in tag cloud 