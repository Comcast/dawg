Feature: Covers various test cases related to STB device (addition/updation/removal of STB device from Dawg House)

Background: 
    Given I added an STB device to dawg house with following details
	| id           | mac               |
    | aabbccddeeff | AA:BB:CC:DD:EE:FF |
    Then I should receive status code 200
    
Scenario: Verify STB device added is available in the dawg house
	Given I added an STB device to dawg house with following details
    | id           | mac               |
	| 0000000000aa | 00:00:00:00:00:AA | 
    Then I should receive status code 200
	When I GET STB device list from dawg house
	Then I should receive status code 200
	And I should verify that the response contains expected STB device id and MAC address
	
Scenario: Get an STB using invalid device id from dawg house
	Given I GET an STB device using invalid device id from dawg house
	Then I should receive the response "Not found Error"
	
Scenario: Get an STB using valid device id from dawg house
	Given I GET an STB device using valid device id from dawg house
	Then I should receive status code 200
	And I should verify that the response contains expected MAC address 
	
Scenario: Add an STB to dawg house with specified properties
	Given I added an STB device to dawg house with following details
    | id           | mac               |
    | ffeeddccbbaa | FF:EE:DD:CC:BB:AA |
	Then I should receive status code 200
	When I GET an STB device from dawg house with same device id
	Then I should verify that the response contains expected MAC address 
	
Scenario Outline: Add an STB to dawg house with invalid model params
	Given I added an STB device to dawg house with device id <id> , mac address <mac>, model <model>, capabilities  <caps> and Family name <family>
	Then I should receive status code 200
	And I should verify that added STB device contains expected model name <model>, capabilities <caps> and Family name <family> 
	Examples:
	| id         | caps       | mac               | family      | model        |
	| modeltest1 | []         | 00:00:00:00:00:AA ||              InvalidModel |
    | modeltest2 | []         | 00:00:00:00:00:BB | TEST FAMILY | InvalidModel |
    | modeltest3 | [\"CAPS\"] | 00:00:00:00:00:CC ||              InvalidModel |
    | modeltest4 | [\"CAPS\"] | 00:00:00:00:00:DD | TEST FAMILY | InvalidModel |
	
Scenario Outline: Add multiple STBs to dawg house
	Given I added 2 STB devices to dawg house with device id <id> and mac address <mac>
	Then I should receive status code 200
	And I should verify that STB devices added are available in the dawg house
	Examples:
	| id           | mac               |
	| 112233445566 | 11:22:33:44:55:66 |
	| 665544332211 | 66:55:44:33:22:11 |

Scenario: Remove STB from dawg house with invalid device id
	Given I DELETE an STB device from dawg house with invalid device id
	Then I should receive the response "Not found Error"
	
Scenario: Remove STB from dawg house with valid device id
	Given I added an STB device to dawg house with following details
    | id       | mac               |
    | resttest | AA:BB:CC:DD:EE:FF |
    Then I should receive status code 200
	When I DELETE an STB device from dawg house with same device id
	Then I should receive status code 200
	And I should verify that STB device is removed from dawg house
	
Scenario: Remove STB from dawg house with valid device id specified as query param
 	Given I added an STB device to dawg house with following details
    | id       | mac               |
    | resttest | 00:00:00:00:00:11 |
    Then I should receive status code 200
	When I delete an STB device from dawg house with same device id specified as query param
	Then I should receive status code 200
	And  I should verify that STB device is removed from dawg hosue
	
Scenario: Remove STB from dawg house with invalid device id specified as query param	
    Given I delete an STB device from dawg house with invalid device id specified as query param
	Then I should receive the response "Not found Error"
	
Scenario: Get an STB from dawg house with valid device id specified in request body
	When I POST access STB service with valid device id
	| id           |
	| aabbccddeeff |
	Then I should receive status code 200
	And I verify that the response contains expected STB id and MAC address 

Scenario: Get an STB from dawg house with invalid device id specified in request body
	When I POST access STB service with invalid device id
	Then I should receive the response "Not found Error"

Scenario: Get an STB from dawg house with model name specified as query param
	Given I added an STB device to dawg house with following details
	| id           | model    |
	| 000000000000 | newmodel | 
	Then I should receive status code 200
	When I GET an STB device from dawg house with same model name specified as query param
	Then I should receive status code 200
	And I verify that the response contains expected STB model name
	
Scenario Outline: Get an STB from dawg house with valid model name specified as query param
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
	
Scenario Outline: Get an STB from dawg house with invalid model name specified as query param
	When I GET an STB device from dawg house with invalid model name <model>
	Then I should receive status code 500
	Examples:
	|model|
	|?refresh=true|
	|%3Frefresh%3Dtrue|

Scenario Outline: Get an STB from dawg house with illegal model name specified as query param
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

Scenario: Populate device lists from dawg house with valid client token specified as path param
	When I POST populate from dawg house with valid client token
	Then I should receive response which is not empty

Scenario: Populate device lists from dawg house with invalid client token specified as path param
	When I POST populate from dawg house with invalid client token
	Then I should receive response which is zero
	
Scenario: Add tags for an STB in dawg house by specifying device id and tag as request body
	Given I added an STB device to dawg house with following details
    | id       | mac               |
    | resttest | AA:BB:CC:DD:EE:FF |
	When I add tags to STB device with following details
	| id       | tag           |
	| resttest | [\"testtag\"] |
	Then I should receive status code 200
	And I should verify that tags are added for the same STB

Scenario: Update tags for an existing STB in dawg house by specifying device id and tag as request body
	Given I update tags for an existing STB device with following details
	| id       | tag                       |
	| resttest | [\"newtag1\",\"newtag2\"] |
	Then I should receive status code 200
	And I should verify that tags are updated for the same STB
		
Scenario: Remove tags from an STB by specifying device id and tag as request body
	When I remove tags from an STB device with following details
	| id           | tag           |
	| aabbccddeeff | [\"testtag\"] |
	Then I should receive status code 200
	And I should verify that tags are removed from the STB
	
Scenario: Update STB details on Dawg House
	Given I added an STB device to dawg house with following details
	| id       | token    |
	| resttest | testuser |
	When I reserve the same STB in Dawg Pound
	Then I should verify that the response contains reservation token and STB device make
	When I update make for the same STB
	Then I should verify that the response contains reservation token and updated STB device make  


	
	
	