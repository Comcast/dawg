Feature: Covers various test cases related to STB Model (addition/updation/removal of STB Model from Dawg House)

Scenario: verify accessing particular STB with valid model id specified as query Params
	When I GET an STB device with valid model id specified as query Params
	Then I should receive status code 200
	And I should receive response with STB model Id and family name 
	
Scenario: verify adding particular STB with mandatory model details specified in request body
	When I add an STB model with mandatory model details specified in request body
	Then I should receive status code 200
	And The added STB model should get appended to the list
	
Scenario: verify adding new capability to an STB Model already present in the list
	When I add an STB model with new capability and mandatory model details specified in request body
	Then I should receive status code 200
	And The added capability for the STB model should be present in the list

Scenario: verify removing an STB Model with valid model id specified as path Params
	Given I add an STB model with mandatory model details specified in request body
	Then The added STB model should get appended to the list
	When I DELETE an STB model with valid model id specified as path Params
	Then The deleted STB model should get removed from the list
	
Scenario: verify that added STB Model get reflected in STb device during assign model
	Given I add an STB model with mandatory model details specified in request body
	Then I PUT an STB device with mandatory details and no model details
	When I assign the same STB model
	Then The added STB model details should get reflected for STB device
	
