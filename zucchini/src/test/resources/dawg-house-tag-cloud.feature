@uitest 
Feature: Dawg House's Tag Cloud behaviours 

Background: 
	Given I am on the dawg house home page 
	
Scenario: Verify the selection and deselection of tags 
	When I select first tag element 
	Then the tagged stb details of  first tag  will be listed 
	And I select second tag element 
	Then the tagged stb details of highlighted first tag and second tag will be listed 
	Then deselect the tags to remove the selection and verify the highlight and tagged stbs 	
	
Scenario: Selection and deselection of tags having common STB list 
	When I select first tag element 
	Then I choose second tag having same list of STBs in first tag selected 
	Then the common stbs are not repeated in the filter table. 
	Then deselect the first tag to remove the selection 
	And the common STBs remains in the filter table 	
	
Scenario: Deletion of STB from a tag and delete the tag from tag cloud 
	When I select one stb that is common in any two of the tags 
	Then verify delete option appears in both the tags 
	And delete the selected STB content from the first tag 
	Then delete the second tag having single STB 
	
Scenario: Verify the tag highlight while deleting STB from tag 
	When I select one tag element and verify the highlight 
	And delete the selected STB content from the tag element 
	Then the highlight remains on the tag element 	
	
Scenario: Verify the delete option of selected tags and checked status of selected STBs. 
	When I select first tag element having atleast one tagged STB 
	Then select check box of one stb common among two tags 
	And the delete option appears for both the tags 
	Then I select second tag element 
	And the checked status of STB and delete option of previous selected tags remains on the page 	
	
Scenario: Verify the functionality by deselecting the tags. 
	When I select first tag element having atleast one tagged STB 
	Then select check box of one stb common among two tags 
	Then select second tag element and deselect the first tag 
	And all the STBs of first tags will be disappers and the delete option gets removed from tags 
	Then choose the first tag again to see the STB is not in selected mode 
	
	
	