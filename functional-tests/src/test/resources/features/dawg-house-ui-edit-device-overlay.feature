@uitest @editDevice
Feature: STB device properties in edit Device overlay 

Background: 
	Given I am on the dawg house home page 

Scenario: Verify STB Device properties available in edit device overlay 
	Given I selected one STB device which has properties(Name, Model, Capabilities, Controller Ip Address, Family, Hardware Revision, Ir Blaster Type, Make,Power Type, Program, Rack Name, Remote type, Slot Name, Tags) 
	When I select edit option on right click 
	Then edit device overlay is displayed 
	And I should see following properties in edit device overlay 
		|Properties           |
		|Name                 |
		|Model                |
		|Capabilities         |
		|Controller Ip Address|	
		|Family               |
		|Hardware Revision    |
		|Ir Blaster Type      |	
		|Make                 |
		|Power Type           |
		|Program              |
		|Rack Name            |
		|Remote Type          |
		|Slot Name            |
		|Tags                 |   	

Scenario: Verify STB device edit overlay is dismissed 
	Given I selected one STB device from dawg house 
	When I select edit option on right click 
	Then edit device overlay is displayed 
	And I select 'Close' button in edit device overlay 
	Then edit device overlay is dismissed 