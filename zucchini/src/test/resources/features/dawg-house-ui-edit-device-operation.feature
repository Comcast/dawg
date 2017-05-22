@uitest 
Feature: Verify Dawg-house's Edit STB device UI behaviours. 

Background: 
	Given I selected one STB device which has properties(Name, Model, Capabilities, Controller Ip Address, Family, Model, Hardware Revision, Ir Blaster Type, Make,Power Type, Program, Rack Name, Remote type, Slot Name, Tags) 
	And I opened Edit Device overlay 
	
Scenario Outline: Editing STB device Name through edit device overlay 
	When I edit the STB device name to <value> 
	And I select <Button> button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see device name displayed as <value> in edit device overlay 
	Examples: 
		|Value       | Button|
		|testName    | Save  |
		|DeviceName  | Close |
		
Scenario Outline: Verify Capabilities and Family name change by modifying the Model name 
	When I edit the model name to <model_name> 
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see Capabilities <Capabilities>, Family name <Family> and Model name <ModelName> are displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities               |Family|
		|null            | null                      |null  |
		|InvalidModelName| null                      |null  |
		|SA3000          | VOD                       |MAC_S |
		|SA4250HDC       | HD, DVR, NOT68K, VOD, ASTB|MAC_S |
		|SA3000          | VOD                       |MAC_S |
		
Scenario Outline: Edit multiple STB device properties (Capabilities, Family and Model name) 
	When I edit STB device properties Model <ModelName>, Family <Family> and Capabilities <Capabilities> 
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see Capabilities <Capabilities>, Family name <Family> and Model name <ModelName> are displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities       |Family    |
		|SA4250HDC       |TestCap1, TestCap2 |TestFamily|
		|InvalidModelName|TestCap1, TestCap2 |TestFamily|
		
Scenario Outline: Add a new STB device property by clicking Add button in edit device overlay 
	Given I entered a new device property in the text field 
	And I click Add button to add the property 
	Then I should see the newly added property name displayed in the edit device overlay 
	When I add value <Value> for the property name 
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see property name and corresponding value <Value> displayed in edit device overlay 
	Examples: 
		| Value   |
		| test val|
		| ""      |
		| "\"?<>!@#$%^&*(){}+_:.,;'[]=-`~'|\\" |
		
Scenario Outline:
Add a new STB device property by pressing the ENTER key in edit device overlay 
	Given I entered a new device property in the text field 
	And I select Set checkbox 
	When I add the property name by pressing the ENTER key 
	Then I should see the newly added property name displayed in the edit device overlay 
	When I add value <Value> for the property name 
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see property name and corresponding value <Value> displayed in edit device overlay 
	Examples: 
		| Value              |
		| ""                 |
		| testval1           |
		| testval1,testval2  |
		| testval1, testval2 |
		| testval1, testval2,|
		
Scenario: Add an existing STB device property in edit device overlay
	When I enter an existing device property in the text field 
	Then an alert pop up with message "already exists as property" is displayed 
	When I select 'OK' button on alert message 
	Then I should see the pop up message is dismissed 
	
Scenario: Add an empty STB device property in edit device overlay
	When I add an empty device property in edit device overlay 
	Then I should see property name is not added in edit device overlay 
	
Scenario: Delete an existing STB device property from edit device overlay 
	When I select delete button on any STB device property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select 'OK' button on alert message 
	Then I should see the selected STB device property removed from edit device overlay 
	
	# new scenario	
Scenario: Verify the STB device property is not removed while selecting Cancel button 
	When I select delete button on any STB device property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select 'Cancel' button on alert message 
	Then I should see the selected STB device property is not removed from edit device overlay 
	
Scenario: Verify the modified icon highlight while marking as modified	
	Given I selected the modified icon of one device property
	Then I should see modified icon as highlighted
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	And the highlight remains on the modified icon
	
Scenario: Verify the modified icon highlight while marking as unmodified	
	Given I selected the modified icon of one device property
	Then I should see modified icon as highlighted
	When I de-select the modified icon 
	Then the highlight removed from the modified icon	
	When I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	And I should see modified icon is not highlighted
	
#new scenario
Scenario: Set any STB device properties as modified 
	Given I modified any two device properties 
	And I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see the edited device properties displayed as modified 
#new scenario
Scenario: Verify the STB device properties are displayed as Unmodified 
	Given I modified any two device properties 
	And I de-select the modified icon
	When I select 'Save' button in edit device overlay 
	Then the edit device overlay is dismissed 
	When I reopen the edit device overlay 
	Then I should see selected STB device properties displayed as unmodified