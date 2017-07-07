Feature: Covers various test cases related to STB Model (addition/updation/removal of STB Model from Dawg House)

Background: 
	Given I send POST request to 'add STB model' with following details
	| model id | model_name | capabilities       | family |
    | DTA30    | DTA30      | [\"SINGLE_TUNER\"] | DTA    |
    Then I should receive status code 200
	And I should verify that added STB model is available in the dawg house
	
Scenario: Retrieve STB model details from dawg house via GET request
	When I send GET request to 'get STB model' with valid model id
	Then I should receive status code 200
	And I should verify that the response contains expected model id, model name and family name
	
Scenario: Add a new STB model to dawg house via POST request with specified properties
	When I send POST request to 'add STB model' with following properties
	| id     | name  | Capabilites | family |
	| SA3000 | SA300 | VOD         | MAC_S  |
	Then I should receive status code 200
	And I should verify that added STB model is available in the dawg house
	
Scenario: Update fields for an STB model already present in the dawg house via POST request
	When I send POST request to update same STB model with following properties
	| name  | Capabilites | family   |
	| DTA30 | VOD         | newfamily|
	Then I should receive status code 200
	Then I should verify that the STB model fields are updated in the dawg house

Scenario: Remove an existing STB model from dawg house via DELETE request with valid model id
	When I send DELETE request to 'delete STB model' with valid STB device id as path param
	Then I should receive status code 200
	And I should verify that the STB is removed from dawg house
	
Scenario Outline: verify that STB Model fields gets reflected in STB device on sending GET request to 'assign models' service
	When I send PUT request to 'add an STB' with STB device id <id>, mac address <mac>, STB model name <model>, model family <Family> and model capabilities <Capabilities> 
	Then I should receive the status code 200 
	When I send GET request to 'assign models' service
	And I should verify that added STB device contains STB device id <id>, mac address <mac>, STB model name <model>, expected capabilities <caps> and Family name <family>
	Examples: 
	| id         | mac               | model | Capabilities | Family  | caps       | family  |
	| modeltest1 | 00:00:00:00:00:AA | mod1  | null         | null    | [\"CAP\"]  | FAMILY  |
	| modeltest2 | 00:00:00:00:00:BB | mod2  | null         | FAMILY1 | [\"CAP\"]  | FAMILY1 |
	| modeltest3 | 00:00:00:00:00:CC | mod3  | [\"CAP2\"]   | null    | [\"CAP2\"] | FAMILY  |