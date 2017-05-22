@uitest 
Feature: STB device Edit Device overlay verifications 

Scenario: Verify STB Device properties available in edit device overlay 
	Given I selected one STB device which has properties(Name, Model, Capabilities, Controller Ip Address, Family, Model, Hardware Revision, Ir Blaster Type, Make,Power Type, Program, Rack Name, Remote type, Slot Name, Tags) 
	When I select edit option on right click 
	Then edit device overlay is displayed 
	And I should see following properties in edit device overlay 
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

Scenario: Verify STB device edit overlay is dismissed 
	Given I selected one STB device from dawg house 
	When I select edit option on right click 
	Then the Edit Device overlay is displayed 
	And I select "Close" button in edit overlay 
	Then edit device overlay is dismissed 