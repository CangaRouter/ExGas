# GUI  Testing Documentation 

Authors:

Date:

Version:

# GUI testing

This part of the document reports about testing at the GUI level. Tests are end to end, so they should cover the Use Cases, and corresponding scenarios.

## Coverage of Scenarios and FR

```
<Complete this table (from IntegrationApiTestReport.md) with the column on the right. In the GUI Test column, report the name of the .py  file with the test case you created.>
```

### 

| Scenario ID | Functional Requirements covered | GUI Test(s) |
| ----------- | ------------------------------- | ----------- | 
| 1           | FRx                             |             |             
| 2           | FRy                             |             |             
| ...         |                                 |             |         
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             


# REST  API  Testing

This part of the document reports about testing the REST APIs of the back end. The REST APIs are implemented by classes in the Controller package of the back end. 
Tests should cover each function of classes in the Controller package

## Coverage of Controller methods


<Report in this table the test cases defined to cover all methods in Controller classes >

| class.method name | Functional Requirements covered |REST  API Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  GasStationController.saveGasStation                   | FR3.1  |  TC01_TestsaveGasStation                   |     
|  GasStationController.getAllGasStations                | FR3.3  |  TC02_TestgetAllGasStations                |
|  GasStationController.getGasStationById                | FR4    |  TC03_TestgetGasStationById                |
|  GasStationController.getGasStationByProximity         | FR4.1  |  TC04_TestgetGasStationByProximity         |
|  GasStationController.searchGasStationByNeighborhood   | FR4.2  |  TC05_TestsearchGasStationsByNeighborhood  |
|  GasStationController.searchGasStationsByGasolineType  | FR4.3  |  TC06_TestsearchGasStationsByGasolineType  |
|  GasStationController.getGasStationsWithCoordinates    | FR4.5  |  TC07_TestgetGasStationsWithCoordinates    |
|  GasStationController.setReport                        | FR5.1  |  TC09_TestsetReport                        |
|  GasStationController.deleteGasStation                 | FR3.2  |  TC10_TestdeleteGasStation                 |
|  UserController.saveUser                               | FR1.1  |  TC11_TestsaveUser                         |
|  UserController.getUserById                            | FR1.4  |  TC12_TestgetUserById                      |
|  UserController.getAllUsers                            | FR1.3  |  TC13_TestgetAllUsers                      |
|  UserController.increaseUserReputation                 | FR5.3  |  TC14_TestincreaseUserReputation           |
|  UserController.decreaseUserReputation                 | FR5.3  |  TC15_TestdecreaseUserReputation           |
|  UserController.login                                  | FR2    |  TC16_Testlogin                            |
|  UserController.deleteUser                             | FR1.2  |  TC17_TestdeleteUser                       |
<!--
|  GasStationController.getGasStationWithoutCoordinates  | FR  |  TC08_TestgetGasStationsWithoutCoordinates | 

check FR for TC04 -->