Feature: Covers various test cases related to STB Model (addition/updation/removal of STB Model from Dawg House)

Background: 
	Given I added an STB device to dawg house with following details
	| model_name | capabilities       | family |
    | DTA30      | [\"SINGLE_TUNER\"] | DTA    |
    Then I should receive status code 200

Scenario: Get an STB model from dawg house
	Given I GET an STB device model from dawg house
	Then I should receive status code 200
	And I should verify that the response contains expected model id and family name
	
Scenario: Add an STB model to dawg house with specified properties
	Given I added an STB model to dawg house with following properties
	| id     | name  | Capabilites | family |
	| SA3000 | SA300 | VOD         | MAC_S  |
	Then I should receive status code 200
	And I should verify that added STB model is available in the dawg house
	
Scenario: Add a new capability to an STB Model already present in the dawg house
	Given I added an STB model to dawg house with following properties
	| name  | Capabilites | family   |
	| DTA30 | VOD         | newfamily|
	Then I should receive status code 200
	Then I should verify that the capability added is available in the dawg house

Scenario: Remove an existing STB model from dawg house
	Given I added an STB model to dawg house with following properties 
	| name   | Capabilites | family |
	| SA3200 | CAP         | FAMILY |
	Then I should verify that added STB model is available in the dawg house
	When I delete the STB model from dawg house 
	Then I should receive status code 200
	And I should verify that the STB is removed from dawg house
	
Scenario: Verify properties of STB model from dawg house
     When I GET an STB device model from dawg house
     Then I should receive the status code 200
     And I should verify that the response contains following properties
     | model   | cap | family |
     | SA3200  | CAP | FAMILY |
         	
Scenario Outline: verify that added STB Model gets reflected in STB device during assign model
	Given I added an STB model to dawg house with following properties 
	| name   | cabability | family |
	| SA3200 | CAP        | FAMILY |
	Then I should receive the status code 200 
	When I add STB device to dawg house with Family <Family> and Capabilities <Capabilities> 
	Then I should receive the status code 200 
	When I assign the same STB model
	And I should verify that STB device contains the expected capabilities <caps> and Family name <family>
	Examples: 
	| Capabilities | Family  | caps       | family  |
	| null         | null    | [\"CAP\"]  | FAMILY  |
	| null         | FAMILY1 | [\"CAP\"]  | FAMILY1 |
	| [\"CAP2\"]   | null    | [\"CAP2\"] | FAMILY  |
	
