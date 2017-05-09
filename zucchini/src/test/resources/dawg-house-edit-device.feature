@uitest 
Feature: Verify Dawg-house's' Edit STB device UI behaviours. 

Background: 
	Given I choose one STB device to perform edit operation 
	When I right click on the STB to select the Edit button
	Then I should see STB device edit overlay 
	
Scenario: Verify STB Device properties available in edit device overlay 
	And I should see following  properties in edit device overlay 
		|Properties           |
		|Name                 |
		|Model                |
		|Capabilities         |
		|Controller Ip Address|	
		|Family               |
		|Model                |
		|Hardware Revision    |
		|Ir Blaster Type      |	
		|Make                 |
		|Power Type           |
		|Program              |
		|Rack Name            |
		|Remote Type          |
		|Slot Name            |
		|Tags                 |
		
Scenario Outline: Editing the STB device name in edit device overlay 
	When I edit the STB device "Name" value to <Value> 
	And I click <Button> button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see device "Name" <value> appears in device overlay 
	Examples: 
		|Value       | Button|
		|testName    | Save  |
		|DeviceName  | Close |
		
Scenario Outline: Verify family name and capabilities name based on modified model name 
	When I edit the model name to <ModelName> 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see the expected model name <ModelName>, capabilities <Capabilities> and family name <Family> are displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities               |Family|
		|null            | null                      |null  |
		|InvalidModelName| null                      |null  |
		|SA3000          | VOD                       |MAC_S |
		|SA4250HDC       | HD, DVR, NOT68K, VOD, ASTB|MAC_S |
		|SA3000          | VOD                       |MAC_S |
		
Scenario Outline: Edit multiple STB device properties (Model, Family and Capabilities)
	When I edit the properties Model <ModelName>,Family <Family> and Capabilities <Capabilities> 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see model name "<ModelName>", capabilities "<Capabilities>" and family name "<Family>" displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities       |Family    |
		|SA4250HDC       |TestCap1, TestCap2 |TestFamily|
		|InvalidModelName|TestCap1, TestCap2 |TestFamily|
		
Scenario Outline: Add a new property by clicking Add button in edit device overlay 
	Given I enter a new property name in the text field of edit device overlay 
	And I add the property name by clicking the Add button 
	Then I should see property name added displayed in edit device overlay 
	When I add value<value> for the property name 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see property name and corresponding value <Value> displayed in edit device overlay 
	Examples: 
		| Value   |
		| test val|
		| ""      |
		| "\"?<>!@#$%^&*(){}+_:.,;'[]=-`~'|\\" |		
		
Scenario Outline: Add a new property by pressing the ENTER key in edit device overlay 
	Given I enter a new property name in the text field of edit device overlay 
	And I select the Set checkbox 
	And I add the property name by pressing the ENTER key 
	Then I should see property name added displayed in edit device overlay 
	When I add value<value> for the property name 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see property name and corresponding value <Value> displayed in edit device overlay 
	Examples: 
		| Value              |
		| ""                 |
		| testval1           |
		| testval1,testval2  |
		| testval1, testval2 |
		| testval1, testval2,|
		
Scenario: Add an existing property in edit device overlay 
	Given I add an existing property name in edit device overlay 
	Then an alert pop up with message "alreday exists as property" is displayed 
	When I select OK button on alert message 
	Then I should see alert pop is dismissed 
	
Scenario: Add an empty property in edit device overlay 
	Given I add an empty property name in edit device overlay 
	Then I should see property name is not added in edit device overlay 
	
Scenario: Delete an existing property from edit device overlay 
	When I click delete button on any property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select OK button on alert message 
	Then I should see selected property removed from edit device overlay 
		
# new scenario	
Scenario: Verify property is not removed while selecting cancel button 
	When I click delete button on any exisiting property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select Cancel button on alert message 
	Then I should see selected property is not removed from edit device overlay 	
	
Scenario: Mark property as Modified 
	Given I marked any two property as "Modified" 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see properties are marked as "Modified" 
	
Scenario: Verify property marked as Unmodified 
	Given I marked any two property as "Modified" 
	And I marked these properties as "Unmodified" 
	And I click Save button in edit device overlay 
	Then I should see edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see properties are marked as "Unmodified" 
	
	