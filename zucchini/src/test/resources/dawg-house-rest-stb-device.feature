Feature: Covers various test cases related to STB device (addition/updation/removal of STB device from Dawg House)

Scenario: verify that accessed device list contains previously added STBs
	Given I PUT an STB device with mandatory details
	When I GET STB device list
	Then I should receive status code 200
	And I should receive response with mandatory details of previously added STBs
	
Scenario: verify accessing particular STB with invalid device id specified in Path Params
	When I GET an STB device with invalid device id
	Then I should receive response with Not Found error
	
Scenario: verify accessing particular STB with valid device id specified in Path Params
	When I GET an STB device with valid device id
	Then I should receive status code 200
	And I should receive response with mandatory STB details 
	
Scenario: verify that an STB can be added with mandatory details given as request body
	Given I PUT an STB device with mandatory details
	Then I should receive status code 200
	And The MAC address and Id should get populated for added STB
	
Scenario: verify that an STB can be added with invalid model params given as request body
	Given I PUT an STB device with mandatory details and Invalid Model Name
	Then I should receive status code 200
	And The family and capabilities should get populated for added STB 
	
Scenario: verify that multiple STBs can be added 
	Given I PUT 2 STBs with mandatory details
	Then I should receive status code 200
	And The STB device list should get populated with added STBs details

Scenario: verify STB removal with invalid device id specified in Path Params
	Given I DELETE an STB device with invalid device id
	Then I should receive status code 200
	
Scenario: verify STB removal with valid device id specified in Path Params
	Given I DELETE an STB device with valid device id
	Then I should receive status code 200
	And The deleted STB device should get removed from STB device list
	
Scenario: verify STB removal with valid device id specified as query param
	Given I GET delete STB service with valid device id 
	Then I should receive status code 200
	And The deleted STB device should get removed from STB device list
	
Scenario: verify STB removal with invalid device id specified as query param	
    Given I GET delete STB service with invalid device id 
	Then I should receive status code 200
	
Scenario: verify accessing particular STB with valid device id specified in request body
	When I POST to "devices/id" service with valid device id specified in request body
	Then I should receive status code 200
	And I should receive response with mandatory STB details 

Scenario: verify accessing particular STB with invalid device id specified in request body
	When I POST to "devices/id" service with invalid device id specified in request body
	Then I should receive response with Not Found error

Scenario: verify accessing particular STB with model name specified as query param
	Given I PUT an STB device with valid Model Name 
	When I GET an STB with same model name specified as query param
	Then I should receive status code 200
	And I should receive response with same STB model name
	
Scenario: verify accessing particular STB with invalid model name specified as query param
	When I GET an STB with invalid model name specified as query param
	Then I should receive status code 500

Scenario: verify accessing particular STB with illegal model name specified as query param
	When I GET an STB with illegal model name specified as query param
	Then I should receive response with Not Found error
	
Scenario: verify populate with valid client token specified as path param
	When I POST populate with valid client token
	Then I should receive response which is not empty

Scenario: verify populate with invalid client token specified as path param
	When I POST populate with invalid client token
	Then I should receive response which is zero
	
Scenario: verify updation of tags for an STB by specifying device id and tag as request body
	Given I PUT an STB device with mandatory details
	When I POST add tags service with STB device id and tag given as request body
	Then I should receive status code 200
	And The tags should get updated for the same STB
	
Scenario: verify removal of tags for an STB by specifying device id and tag as request body
	When I POST remove tags service with STB device id and tag given as request body
	Then I should receive status code 200
	And The tags should get removed for the same STB
	
Scenario: verify updating STB details on Dawg House
	Given I have added an STB to Dawg house
	Then I have reserved the same STB using Dawg Pound
	And I update the Make for the same STB
	Then The Make details should get updated for the same STB


	
	
	