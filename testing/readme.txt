This is where the dawg integration tests will go. Not yet decided on the framework to use

Instructions to run selenium dawg tests in Sauce Labs
==================================

**Description**

Automating dawg portal selenium tests inside sauce Labs.Automation can run in different Platforms (Windows,Linux,Mac)with various chrome browser versions inside Labs.

**Execution command**

For executing tests in SauceLabs

*Note : If sauce credentials(-Dsauce.username -Dsauce.key -Dsauce.port) are not specified it will take the default values from config file*

``` mvn clean test -Dtest=LoginPageIT -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4446```

For executing tests in different Platform inside SauceLabs
*Note :If platform type , platform version or chrome version is not specified, then the test will runs with default windows platform with platform version Windows 8.1 with chrome version 46*

*(Supported Platforms in Sauce Labs MAC, Windows, Linux)*

*MAC(Supported MAC Platform version in Sauce Labs-OS X 10.12, OS X 10.11,OS X 10.10,OS X 10.9, OS X 10.9)*

``` mvn clean test -Dtest=LoginPageIT -Dtest.platform=mac -Dmac.veriosn=OS X 10.12```

For executing tests in MAC platform with particular chrome version

*MAC(Supported chrome versions in Sauce labs -57, 56, 53, 51,50,49,47,46)* 

``` mvn clean test -Dtest=LoginPageIT -Dtest.platform=mac -Dmac.veriosn=OS X 10.12 -Dchrome.veriosn=57```

*WINDOWS(Supported Windows Platform version in Sauce Labs-"Windows 10", "Windows 8.1", "Windows 8", "Windows 7")*

```mvn clean test -Dtest=LoginPageIT -Dwin.version=Windows 8.1```

*WINDOWS(Supported chrome versions in Sauce labs -"46", "52", "51", "53", "54","55", "56", "57")* 

```mvn clean test -Dtest=LoginPageIT -Dwin.version=Windows 8.1 -Dchrome.veriosn=56```

*LINUX(Platform version-Linux)*

``` mvn clean test -Dtest=LoginPageIT -Dtest.platform=linux -Dmac.veriosn=Linux```

*LINUX(Supported chrome versions in Sauce labs -"47", "46", "45", "44")* 

``` mvn clean test -Dtest=LoginPageIT -Dtest.platform=linux -Dmac.veriosn=Linux -Dchrome.veriosn=44``` 

