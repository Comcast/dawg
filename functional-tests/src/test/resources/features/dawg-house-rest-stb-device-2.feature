Feature: Covers various test cases related to STB device (addition/updation/removal of STB device from Dawg House)

Background: 
    Given I send PUT request to 'add an STB' with following details
	| id           | mac               |
    | aabbccddeeff | AA:BB:CC:DD:EE:FF |
    Then I should receive status code 200
    
Scenario: Retrieve STB device list from dawg house via GET request 
	When I send GET request to 'STB device list'
	Then I should receive status code 200
	And I should verify that the response contains expected STB device id and MAC address
	
Scenario: Retreive STB details from dawg house via GET request with valid device id 
	When I send GET request to 'get STB' with valid device id as path param
	Then I should receive status code 200
	And I should verify that the response contains expected MAC address 
	
Scenario: Remove STB from dawg house via DELETE request with valid device id
	When I send DELETE request to 'delete STB' with valid device id as path param
	Then I should receive status code 200
	And I should verify that STB device is removed from dawg house
	
Scenario: Remove STB from dawg house via GET request with valid device id specified as query param
	When I send GET request to 'delete STB' with valid device id as query param
	Then I should receive status code 200
	And  I should verify that STB device is removed from dawg hosue
	
Scenario: Retrieve STB details from dawg house via POST request with valid device id specified in request body
	When I send POST request to 'retrieve STB' with valid device id
	Then I should receive status code 200
	And I verify that the response contains expected STB device id and MAC address 

Scenario: Add tags for an STB in dawg house via POST request by specifying device id and tag as request body
	When I send POST request to 'add tag to STB' with following details
	| id           | tag           |
	| aabbccddeeff | [\"testtag\"] |
	Then I should receive status code 200
	And I should verify that tags are added for the same STB
	
Scenario: Update tags for an existing STB in dawg house via POST request by specifying device id and tag as request body
	Given I send POST request to 'add tag to STB' with following details
	| id           | tag           |
	| aabbccddeeff | [\"testtag\"] |
	Then I should receive status code 200
	When I send POST request to 'add tag to STB' for same STB device with following details
	| tag                       |
	| [\"newtag1\",\"newtag2\"] |
	Then I should receive status code 200
	And I should verify that tags are updated for the same STB
		
Scenario: Remove tags from an existing STB in dawg house via POST request by specifying device id and tag as request body
	Given I send POST request to 'add tag to STB' with following details
	| id           | tag           |
	| aabbccddeeff | [\"testtag\"] |
	Then I should receive status code 200
	When I send POST request to 'remove tag from STB' with same STB device id and tag name
	Then I should receive status code 200
	And I should verify that tags are removed from the STB