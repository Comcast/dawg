@uitest @editDevice 
Feature: Edit STB device 

Background: 
    Given I am on the dawg house home page
	Then I selected one STB device from dawg house which has properties 'Name, Model, Capabilities, Controller Ip Address, Family, Hardware Revision, Ir Blaster Type, Make, Power Type, Program, Rack Name, Remote Type, Slot Name, Tags' 
	And I opened edit device overlay 
	Then I should see the STB properties
@smoke		
Scenario: Verify device name modified
	When I edit the STB device name value
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see device name is modified in edit device overlay 
@smoke
Scenario: Verify device name not modified  
	When I edit the STB device name value
	And I select 'Close' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see device name is not modified in edit device overlay

Scenario Outline: Verify Capabilities and Family name change by modifying the Model name 
	When I edit the model name to <ModelName> 
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see Capabilities <Capabilities>, Family name <Family> and Model name <ModelName> are displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities               |Family|
		|null            | null                      |null  |		
		|InvalidModelName| null                      |null  |
		# disabled as legacy device configurations are not available in xdawg
		#|SA3000          | VOD                       |MAC_S |
		#|SA4250HDC       | HD, DVR, NOT68K, VOD, ASTB|MAC_S |
		#|SA3000          | VOD                       |MAC_S |
		
Scenario Outline: Edit multiple STB device properties (Capabilities, Family and Model name) 
	When I edit STB device properties model <ModelName>, family <Family> and capabilities <Capabilities> 
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see Capabilities <Capabilities>, Family name <Family> and Model name <ModelName> are displayed in model overlay 
	Examples: 
		|ModelName       |Capabilities       |Family    |
	#	|SA4250HDC       |TestCap1,TestCap2 |TestFamily1| disabled as legacy device configurations are not available in xdawg 
		|InvalidModelName|TestCap1,TestCap2 |TestFamily|
			
Scenario Outline: Add a new STB device property by clicking Add button in edit device overlay 
	Given I entered a new device property in the text field 
	When I add the property name by clicking the Add button
	Then I should see the property name is displayed in edit device overlay 
	When I add value <Value> for the property name 
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see property name and corresponding value <Value> displayed in edit device overlay 
	Examples: 
		| Value   |
		| test val|
		| ""      |
		| "\"?<>!@#$%^&*(){}+_:.,;'[]=-`~'|\\" |
				
Scenario Outline:Add a new STB device property by pressing the ENTER key in edit device overlay 
	Given I entered a new device property in the text field 
	And I select Set checkbox 
	When I add the property name by pressing the ENTER key 
	Then I should see the property name is displayed in edit device overlay 
	When I add value <Value> for the property name 
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see property name and corresponding value <Expected_Value> displayed in edit device overlay 
	Examples: 
		| Value              |Expected_Value     |
		| ""                 |""                 |
		| testval1           |testval1           |
		| testval1,testval2  |testval1, testval2 |
	    | testval1, testval2 |testval1, testval2 |
		| testval1,testval2, |testval1, testval2 | 
	
Scenario: Add an existing STB device property in edit device overlay
	When I enter an existing device property in the text field 
	Then an alert pop up with message "already exists as property" is displayed 
	When I select 'OK' button on alert message 
	Then I should see the pop up message is dismissed 

Scenario: Add an empty STB device property in edit device overlay
	Given I entered an empty device property in the text field
	When I add the property name by pressing the ENTER key
	Then I should see the property name is not added in edit device overlay 
@smoke			
Scenario: Delete an existing STB device property from edit device overlay 
	When I select delete button on any STB device property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select 'OK' button on alert message 
	Then I should see the property name is removed from edit device overlay 	
@smoke	
Scenario: Verify the STB device property is not removed while selecting Cancel button 
	When I select delete button on any STB device property 
	Then an alert pop up with message "Are you sure you want to remove property" is displayed 
	When I select 'Cancel' button on alert message 
	Then I should see the property name is not removed from edit device overlay 

Scenario: Verify the modified icon highlight while marking as modified	
	Given I selected modified icon of one device property
	Then I should see modified icon displayed as highlighted
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	And the highlight remains on the modified icon

Scenario: Verify the modified icon highlight while marking as unmodified	
	Given I selected modified icon of one device property
	Then I should see modified icon displayed as highlighted
	When I de-select modified icon of same device property
	Then the highlight removed from the modified icon	
	When I select 'Save' button in edit device overlay 
	Then  edit device overlay is dismissed 
	When I reopen edit device overlay 
	And I should see modified icon is not highlighted	

Scenario: Set any STB device properties as modified 
	Given I modified any 2 device properties 
	And I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see edited device properties displayed as modified 
	
Scenario: Verify the STB device properties are displayed as Unmodified 
	Given I modified any 2 device properties 
	And I de-select the modified icon of selected device properties
	When I select 'Save' button in edit device overlay 
	Then edit device overlay is dismissed 
	When I reopen edit device overlay 
	Then I should see selected device properties displayed as unmodified