Feature: Covers various test cases related to STB device (addition/updation/removal of STB device from Dawg House)

Scenario: Add a new STB device to dawg house via PUT request with specified properties
	Given I send PUT request to 'add an STB' service with following details
    | id           | mac               |
    | ffeeddccbbaa | FF:EE:DD:CC:BB:AA |
	Then I should receive status code 200
	When I send GET request to 'get STB by id' service with same device id
	Then I should verify that the response contains expected MAC address 
	
Scenario Outline: Add an STB to dawg house via PUT request with invalid model params
	Given I send PUT request to 'add an STB' service with device id <id> , mac address <mac>, model <model>, capabilities  <caps> and Family name <family>
	Then I should receive status code 200
	And I should verify that added STB device contains device id <id> , mac address <mac>, expected model name <model>, capabilities <caps> and Family name <family> 
	Examples:
	| id         | caps       | mac               | family      | model        |
	| modeltest1 | []         | 00:00:00:00:00:AA ||              InvalidModel |
    | modeltest2 | []         | 00:00:00:00:00:BB | TEST FAMILY | InvalidModel |
    | modeltest3 | [\"CAPS\"] | 00:00:00:00:00:CC ||              InvalidModel |
    | modeltest4 | [\"CAPS\"] | 00:00:00:00:00:DD | TEST FAMILY | InvalidModel |
	
Scenario Outline: Add multiple STBs to dawg house via PUT request
	Given I send PUT request to 'add an STB' service with device id <id> and mac address <mac>
	Then I should receive status code 200
	And I should verify that STB devices added are available in the dawg house
	Examples:
	| id           | mac               |
	| 112233445566 | 11:22:33:44:55:66 |
	| 665544332211 | 66:55:44:33:22:11 |
	
Scenario Outline: <case> STB details from dawg house via <rest_method> request with invalid device id
	When I send <rest_method> request to '<endpoint>' service with invalid device id
	Then I should receive the response "Not found Error"
	Examples:
	| case    | rest_method | endpoint      |
	| Retrive | GET         | get STB by id |
	| Remove  | DELETE      | delete STB    |
	
Scenario: Remove STB details from dawg house via GET request with invalid device id specified as query param	
    When I send GET request to 'delete STB by query' with invalid device id 
	Then I should receive the response "Not found Error"

Scenario: Retrieve STB details from dawg house via POST request with invalid device id specified in request body
	When I send POST request to 'retrieve STB by id' service with invalid device id
	Then I should receive the response "Not found Error"

Scenario: Retrieve STB details from dawg house via GET request with model name specified as query param
	Given I send PUT request to 'add an STB' service with following details
	| id           | model    |
	| 000000000000 | newmodel | 
	Then I should receive status code 200
	When I send GET request to 'get STB by query' service with same model name
	Then I should receive status code 200
	And I verify that the response contains expected STB model name
	
Scenario Outline: Retrieve STB details from dawg house via GET request with valid model name specified as query param
	When I send GET request to 'get STB by query' service with valid model name <model>
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
	
Scenario Outline: Retrieve STB details from dawg house via GET request with invalid model name specified as query param
	When I send GET request to 'get STB by query' service with invalid model name <model>
	Then I should receive status code 500
	Examples:
	|model|
	|?refresh=true|
	|%3Frefresh%3Dtrue|

Scenario Outline: Retrieve STB details from dawg house via GET request with illegal model name specified as query param
	When I send GET request to 'get STB by query' service with illegal model name <model>
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

Scenario: Populate device lists from dawg house via POST request with valid client token specified as path param
	When I send POST request to 'populate' service with valid client token
	Then I should receive response which is not empty

Scenario: Populate device lists from dawg house via POST request with invalid client token specified as path param
	When I send POST request to 'populate' service with invalid client token
	Then I should receive response as '0'

Scenario: Update STB details on Dawg House via POST request with device details passed as request body
	Given I send PUT request to 'add an STB' service with following details
	| id       | token    |
	| resttest | testuser |
	When I send POST request to 'reserve an STB' service in Dawg Pound
	Then I should verify that the response contains reservation token and STB device make
	When I send POST request to 'update an STB' service with same STB device id and new STB device make
	Then I should verify that the response contains reservation token and updated STB device make   





	
	
	