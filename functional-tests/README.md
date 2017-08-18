xDawg test cases
==================================
In xDawg project test cases automate using following frameworks 

1. Selenium Zucchini API for automating xDawg UI
2. REST assured API for automating REST related test cases

1. Selenium Zucchini API for automating xDawg UI
=====================================================

**Description**

Automates dawg portal selenium tests inside sauce Labs or local. Automation can run in different Platforms (Windows,Linux,Mac)with various chrome browser versions inside Labs.Supported browser: chrome

**Tests will run in sauce labs with following Supported Platform versions**

|Platform  | Versions |
| ------ | ------ |
| Windows | Windows 10, Windows 8.1, Windows 8, Windows 7 |
| MAC |OS X 10.12, OS X 10.11, OS X 10.10, OS X 10.9 |
| Linux | No specific version |

**Supported chrome versions in Sauce Labs**

|Platform  | Chrome Versions |
| ------ | ------ |
| Windows | 46, 52, 51, 53, 54,55, 56, 57 |
| MAC |46, 47, 49, 50, 51, 53, 56, 57 |
| Linux | 47, 46, 45, 44 |

**For executing all smoke tests in SauceLabs**
>*Note : sauce credentials are mandatory(-Dsauce.username -Dsauce.key -Dsauce.port -Ddawg.house.url -Ddawg.username -Ddawg.passwrod)

```sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/  -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @smoke -D

**For executing Integration tests in SauceLabs**
```sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @uitest
```
**For executing tests in different Platform inside SauceLabs**
>*Note :If platform type , platform version or chrome version is not specified, then the test will runs with default windows platform with platform version 8.1 with chrome version 46*

**For executing test in MAC platform with a particular platform verison**
``` sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @uitest -Dtest.platform=mac -Dmac.version=OS X 10.12
```
**For executing tests in MAC platform with particular chrome version**

```sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @uitest -Dtest.platform=mac -Dmac.veriosn=OS X 10.12 -Dchrome.version=57
```

**For executing test in Windows platform with a particular platform verison and chrome version**

``` sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @uitest -Dwin.version=8.1 -Dchrome.version=56
```
**For executing test in Linux platform with particular chrome version**

```sh
mvn clean test -Dtest=DawgHouseUITest -Dtest.mode=sauce -Dsauce.username=XXXX -Dsauce.key=XXXXXXXX -Dsauce.port=4445 -Dsauce.url=http://XXX -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @uitest -Dtest.platform=linux -Dchrome.version=44
```
2. REST assured API for automating REST related test cases 
=====================================================
Automates various xDawg rest test cases.

Execution command

``` sh
mvn clean test -Dtest=DawgHouseRestTest -Ddawg.house.url=https://localhost:/dawg-house/ -Ddawg.pound.url =https://localhost:/dawg-house/ -Ddawg.username=XXX -Ddawg.password=XXX -Dcucumber.options=--tags @dawg_rest 
```