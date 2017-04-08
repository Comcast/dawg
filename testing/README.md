  This is where the dawg integration tests will go. Not yet decided on the framework to use

Instructions to run selenium dawg tests in Sauce Labs
==================================

**Description**

Automating dawg portal selenium tests inside sauce Labs. Automation can run in different Platforms (Windows,Linux,Mac)with various chrome browser versions inside Labs.

**Tests will run in sauce labs with following Supported Platform versions**

|Platform  | Versions |
| ------ | ------ |
| Windows | Windows 10, Windows 8.1, Windows 8, Windows 7 |
| MAC |OS X 10.12, OS X 10.11,OS X 10.10,OS X 10.9, OS X 10.9 |
| Linux | Linux |

**Supported chrome versions in Sauce Labs**

|Platform  | Chrome Versions |
| ------ | ------ |
| Windows | 46, 52, 51, 53, 54,55, 56, 57 |
| MAC |57, 56, 53, 51,50,49,47,46 |
| Linux | 47, 46, 45, 44 |

**For executing tests in SauceLabs**
>*Note : sauce credentials are mandatory(-Dsauce.username -Dsauce.key -Dsauce.port)

```sh
mvn clean test -Dtest=LoginPageIT -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX
```
**For executing tests in different Platform inside SauceLabs**
>*Note :If platform type , platform version or chrome version is not specified, then the test will runs with default windows platform with platform version Windows 8.1 with chrome version 46*

**For executing test in MAC platform with a particular platform verison**
``` sh
mvn clean test -Dtest=LoginPageIT -Dtest.platform=mac -Dmac.version=OS X 10.12
```
**For executing tests in MAC platform with particular chrome version**

```sh
mvn clean test -Dtest=LoginPageIT -Dtest.platform=mac -Dmac.veriosn=OS X 10.12 -Dchrome.version=57
```

**For executing test in Windows platform with a particular platform verison and chrome version**

``` sh
mvn clean test -Dtest=LoginPageIT -Dwin.version=Windows 8.1
```

``` sh
mvn clean test -Dtest=LoginPageIT -Dwin.version=Windows 8.1 -Dchrome.version=56
```
**For executing test in Linux platform with a particular platform verison and chrome version**
*LINUX(Platform version-Linux)*

```sh
mvn clean test -Dtest=LoginPageIT -Dtest.platform=linux -Dmac.version=Linux
```
```sh
mvn clean test -Dtest=LoginPageIT -Dtest.platform=linux -Dmac.veriosn=Linux -Dchrome.version=44
``` 


