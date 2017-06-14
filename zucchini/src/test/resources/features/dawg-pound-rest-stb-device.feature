Feature: Covers various test cases related to STB device reservation from Dawg Pound

Background: 
    Given I send PUT request to 'add an STB' with following details
	| id        | mac               |
    | poundrest | 00:00:00:00:00:AA |
    Then I should receive status code 200
    
Scenario: Verify that dawg pound STB reservation list contains STBs added to dawg house 
	When I send GET request to get 'STB reservation list' 
	Then I should receive status code 200
	And I verify that the response contains expected STB device id and MAC address

Scenario: Reserve an STB device on dawg pound via POST request
    When I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200
    When I send GET request to get 'STB reservation token' with same reservation token
    Then I verify that the response contains expected STB device id and reservation token
    
Scenario: Get STB reservation token from dawg pound via GET request with invalid reservation token
	Given I send GET request to get 'STB reservation token' with invalid token
    Then I verify that the response doesn't contain reservation token
 
Scenario: Check if an STB device is reserved on dawg pound via GET request with valid device id
    Given I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200   
    And I should verify that the STB is reserved with expected reservation token

Scenario: Check if an STB device is reserved on dawg pound via GET request with invalid device id
    When I send GET request to 'check if STB is reserved' with invalid device id
    Then I should receive the response which is empty

Scenario: Verify the response of STB reservation on dawg pound with valid device id
    When I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200   
    And I verify that the response contains true
    
Scenario Outline: Verify the response of STB reservation on dawg pound with invalid device id
    When I send POST request to 'reserve STB' with invalid <param>
    Then I should receive status code 200   
    And I verify that the response contains false
    Examples:
    | param             |
    | device id         |
    | expiration time   |
    | reservation token |
    
Scenario: Reserve an STB device which is already reserved with different reservation token on dawg pound 
    Given I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200   
    When I send POST request to 'reserve STB' for same STB device with different reservation token
    And I verify that the response contains first reservation token
    
Scenario: Reserve an STB device on dawg pound which is non reserved via POST request
    Given I send POST request to 'unreserve STB' with following details
    | id        |
    | poundrest |
    Then I should receive status code 200   
    When I send POST request to reserve same STB with different reservation token
    And I verify that the response contains reserved token
    
Scenario: Unreserve an STB device which is already reserved via POST request with valid device id
    Given I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200 
    When I send POST request to unreserve same STB
    Then I should receive status code 200   
    And I verify that the response contains unreserved token
    
Scenario: Unreserve an STB device which is already reserved via POST request with invalid device id
    Given I send POST request to 'reserve STB' with following details
    | id        | token       |
    | poundrest | reservetest |
    Then I should receive status code 200  
    When I send POST request to unreserve same STB with invalid device id
    Then I verify that the response as empty