@dawg_rest
Feature: REST verification STB device operations-1(addition/updation/removal of STB device from Dawg House)

@rest_stb_add @dawg_rest
Scenario Outline: Add an STB to dawg house via PUT request with invalid model params
	When I send PUT request to 'add an STB' with STB device id <id>, mac address <mac>, STB model name <model>, model capabilities <caps> and model family <family>
	Then I should receive status code 200
	And I should verify that added STB device contains STB device id <id>, mac address <mac>, STB model name <model>, expected model capabilities <caps> and model family <family>
	Examples:
	| id       | mac               | model        | caps     | family      |
	| teststb1 | 00:00:00:00:00:AA | InvalidModel | []       ||             
	| teststb2 | 00:00:00:00:00:BB | InvalidModel | []       | TEST FAMILY |
	| teststb3 | 00:00:00:00:00:CC | InvalidModel | ["CAPS"] ||             
	| teststb4 | 00:00:00:00:00:DD | InvalidModel | ["CAPS"] | TEST FAMILY |

@rest_stb_add
Scenario Outline: Add multiple STBs to dawg house via PUT request
	When I send PUT request to 'add an STB' with device id <id> and mac address <mac>
	Then I should receive status code 200
	And I should verify all the STB devices added are available in the dawg house
	Examples:
	| id       | mac               |
	| teststb1 | 11:22:33:44:55:66 |
	| teststb2 | 66:55:44:33:22:11 |

Scenario Outline: <endpoint> STB details from dawg house via <rest_method> request with invalid device id
	When I send <rest_method> request to '<endpoint> STB' with invalid device id
	Then I should receive status code 404
	Examples:
	| rest_method | endpoint |
	| GET         | get      |
	#| DELETE      | delete   |
	| POST        | retrieve |
	
Scenario: Remove STB details from dawg house via GET request with invalid device id specified as query param	
    When I send GET request to 'delete STB' with invalid device id as query param
	Then I should receive status code 404

@rest_stb_add
Scenario: Retrieve STB details from dawg house via GET request with model name specified as query param
	Given I send PUT request to 'add an STB' with following details
	| id       | model    |
	| teststb1 | newmodel | 
	Then I should receive status code 200
	When I send GET request to 'get STB' with same model name as query param
	Then I should receive status code 200
	And I verify that the response contains expected STB model name
	
Scenario Outline: Retrieve STB details from dawg house via GET request with valid model name specified as query param
	When I send GET request to 'get STB' with valid model name <model> as query param
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
	When I send GET request to 'get STB' with invalid model name <model> as query param
	Then I should receive status code 500
	Examples:
	|model|
	|?refresh=true|
	#|%3Frefresh%3Dtrue|
@disabled
Scenario Outline: Retrieve STB details from dawg house via GET request with illegal model name specified as query param
	When I send GET request to 'get STB' with illegal model name <model> as query param
	Then I should receive status code 404
	Examples:
	|model|
	|\\{ 0x00, 0x00, 0x27, 0x13 \\}|
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

@disabled
Scenario: Populate device lists from dawg house via POST request with valid client token specified as path param
	When I send POST request to 'populate' with valid client token
	Then I should receive status code 200

@disabled
Scenario: Populate device lists from dawg house via POST request with invalid client token specified as path param
	When I send POST request to 'populate' with invalid client token
	Then I should receive response as '0'

@rest_stb_add
Scenario: Update STB details on Dawg House via POST request with device details passed as request body
	Given I send PUT request to 'add an STB' with following details
	| id       | model    |
	| teststb1 | newmodel |
	When I send POST request to 'reserve an STB' in dawg pound
	Then I should verify that reserved STB contains expected reservation token and STB device make
	When I send POST request to 'update STB' with same STB device id and new STB device make
	Then I should verify that reserved STB contains expected reservation token and updated STB device make   