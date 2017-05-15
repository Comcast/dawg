Feature: Covers various test cases related to STB device (addition/updation/removal of STB device from Dawg House)

Background: Add an STB device to dawg house
    Given I added an STB device to dawg house with following details
	|id|mac|
    |aabbccddeeff|AA:BB:CC:DD:EE:FF|
    Then I should receive status code 200
    
Scenario: Add an STB device to dawg house and verify that STB device list contains added devices
	Given I added an STB device to dawg house with following details
    |id|mac|
    |0000000000aa|00:00:00:00:00:AA| 
    Then I should receive status code 200
	When I GET STB device list from dawg house
	Then I should receive status code 200
	And I verify that the response contains expected STB id and MAC address
	
Scenario: Access particular STB with invalid device id from dawg house
	Given I GET an STB device from dawg house with invalid device id 
	Then I should receive the response "Not found Error"
	
Scenario: Access particular STB with valid device id from dawg house
	Given I GET an STB device from dawg house with valid device id
	Then I should receive status code 200
	And I verify that the response contains expected MAC address 
	
Scenario: Add an STB to dawg house with mandatory details given as request body
	Given I added an STB device to dawg house with following details
    |id|mac|
    |ffeeddccbbaa|FF:EE:DD:CC:BB:AA|
	Then I should receive status code 200
	Given I GET an STB device from dawg house with same device id
	And I verify that the response contains expected MAC address 
	
Scenario Outline: Add an STB to dawg house with invalid model params given as request body
	Given I added an STB device to dawg house with mandatory details, expected capabilities <caps> and Family name <family>
	|id|mac|model|
	|modeltest|00:00:00:00:00:BB|InvalidModel|
	Then I should receive status code 200
	And I verify that added STB device contains expected model name, capabilities <caps> and Family name <family> 
	Examples:
	|caps|family|
	|[]||
    |[]|TEST FAMILY|
    |[\"CAPS\"]||
    |[\"CAPS\"]|TEST FAMILY|
	
Scenario: Add multiple STBs to dawg house
	Given I add 2 STBs with mandatory details to dawg house
	Then I should receive status code 200
	And The STB device list should get populated with added STBs details

Scenario: Remove STB from dawg house with invalid device id
	Given I DELETE an STB device from dawg house with invalid device id
	Then I should receive the response "Not found Error"
	
Scenario: Remove STB from dawg house with valid device id
	Given I added an STB device to dawg house with following details
    |id|mac|
    |resttest|AA:BB:CC:DD:EE:FF|
    Then I should receive status code 200
	When I DELETE an STB device from dawg house with same device id
	Then I should receive status code 200
	And The deleted STB device should get removed from STB device list
	
Scenario: Remove STB from dawg house with valid device id specified as query param
 	Given I added an STB device to dawg house with following details
    |id|mac|
    |resttest|00:00:00:00:00:11|
    Then I should receive status code 200
	When I GET delete STB service with valid device id 
	Then I should receive status code 200
	And The deleted STB device should get removed from STB device list
	
Scenario: Remove STB from dawg house with invalid device id specified as query param	
    Given I GET delete STB service with invalid device id 
	Then I should receive the response "Not found Error"
	
Scenario: Access particular STB from dawg house with valid device id specified in request body
	When I POST access STB service with valid device id
	|id|
	|aabbccddeeff|
	Then I should receive status code 200
	And I verify that the response contains expected STB id and MAC address 

Scenario: Access particular STB from dawg house with invalid device id specified in request body
	When I POST access STB service with invalid device id
	Then I should receive the response "Not found Error"

Scenario: Access particular STB from dawg house with model name specified as query param
	Given I added an STB device to dawg house with following details
	|      id      |   model  |
	| 000000000000 | newmodel | 
	Then I should receive status code 200
	When I GET an STB device from dawg house with same model name specified as query param
	Then I should receive status code 200
	And I verify that the response contains expected STB model name
	
Scenario Outline: Access particular STB from dawg house with valid model name specified as query param
	When I GET an STB device from dawg house with valid model name <model>
	Then I should receive status code 200
	Examples:
	|model|
	|normal|
	|012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789|
	||
	|&refresh=true|
	|%26refresh%3dtrue|
	|%3BDROP%20TABLE%20table_name|
	|#AnchorTag|
	|%23AnchorTag|
	|%20%20%20%20%20%20%20%20%20%20|
	|%20%20%20%20%20%20%20%20this_string_begins_with_spaces|
	|\u0048\u0065\u006C\u006C\u006FWorld|
	|this_string_has_trailing_spaces%20%20%20%20%20|
	|%3cscript%20src=%22http%3a%2f%2fwww.jsfromanothersite.com%2fnasty.js%22%3e%3c%2fscript%3e|
	|%5Cr%5CnFLUSHDB|
	|%5C%22%20FLUSHDB|
	|null|
	
Scenario Outline: Access particular STB from dawg house with invalid model name specified as query param
	When I GET an STB device from dawg house with invalid model name <model>
	Then I should receive status code 500
	Examples:
	|model|
	|?refresh=true|
	|%3Frefresh%3Dtrue|

Scenario Outline: Access particular STB from dawg house with illegal model name specified as query param
	When I GET an STB device from dawg house with illegal model name <model>
	Then I should receive the response "Not found Error"
	Examples:
	|model|
	|{ 0x00, 0x00, 0x27, 0x13 }|
	|%0|
	|          |
	|\r\nFLUSHDB|
	|        this_string_begins_with_spaces|
	|this_string_has_trailing_spaces     |
	|spaces_in_the_       _of_this|
	|\" FLUSHDB|
	|\"\"\"\"\"|
	|\"escapedQuotes\"|
	|\\ FLUSHDB \u0022 + "\"|
	|\uD800|

Scenario: Populate from dawg house with valid client token specified as path param
	When I POST populate from dawg house with valid client token
	Then I should receive response which is not empty

Scenario: Populate from dawg house with invalid client token specified as path param
	When I POST populate from dawg house with invalid client token
	Then I should receive response which is zero
	
Scenario: Update tags for an STB by specifying device id and tag as request body
	Given I added an STB device to dawg house with following details
    |id|mac|
    |resttest|AA:BB:CC:DD:EE:FF|
	When I add tags to STB device with following details
	|id|tag|
	|resttest|testtag|
	Then I should receive status code 200
	And The tags should get updated for the same STB
	
Scenario: Remove tags for an STB by specifying device id and tag as request body
	When I remove tags from an STB device with following details
	|id|tag|
	|aabbccddeeff|testtag|
	Then I should receive status code 200
	And The tags should get removed for the same STB
	
Scenario: Update STB details on Dawg House
	Given I added an STB device to dawg house with following details
	|id|token|
	|resttest|testuser|
	Then I have reserved the same STB using Dawg Pound
	And I update the make for the same STB
	Then I verify that the response contains expected STB device make and token 


	
	
	