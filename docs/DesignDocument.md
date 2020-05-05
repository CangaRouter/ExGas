# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document (see EZGas Official Requirements.md ). <br>
The design must comply with interfaces defined in package it.polito.ezgas.service (see folder ServicePackage ) <br>
UML diagrams **MUST** be written using plantuml notation.

# High level design 

The style selected is client - server. Clients can be smartphones, tablets, PCs.
The choice is to avoid any development client side. The clients will access the server using only a browser. 

The server has two components: the frontend, which is developed with web technologies (JavaScript, HTML, Css) and is in charge of collecting user inputs to send requests to the backend; the backend, which is developed using the Spring Framework and exposes API to the front-end.
Together, they implement a layered style: Presentation layer (front end), Application logic and data layer (back end). 
Together, they implement also an MVC pattern, with the V on the front end and the MC on the back end.



```plantuml
@startuml
package "Backend" {

}

package "Frontend" {

}


Frontend -> Backend
@enduml


```


## Front End

The Frontend component is made of: 

Views: the package contains the .html pages that are rendered on the browser and that provide the GUI to the user. 

Styles: the package contains .css style sheets that are used to render the GUI.

Controller: the package contains the JavaScript files that catch the user's inputs. Based on the user's inputs and on the status of the GUI widgets, the JavaScript controller creates REST API calls that are sent to the Java Controller implemented in the back-end.


```plantuml
@startuml
package "Frontend" {

    package "it.polito.ezgas.resources.views" {

    }


package "it.polito.ezgas.resources.controller" {

    }


package "it.polito.ezgas.resources.styles" {

    }



it.polito.ezgas.resources.styles -down-> it.polito.ezgas.resources.views

it.polito.ezgas.resources.views -right-> it.polito.ezgas.resources.controller


}
@enduml

```

## Back End

The backend  uses a MC style, combined with a layered style (application logic, data). 
The back end is implemented using the Spring framework for developing Java Entrerprise applications.

Spring was selected for its popularity and relative simplicity: persistency (M and data layer) and interactions are pre-implemented, the programmer needs only to add the specific parts.

See in the package diagram below the project structure of Spring.

For more information about the Spring design guidelines and naming conventions:  https://medium.com/the-resonant-web/spring-boot-2-0-project-structure-and-best-practices-part-2-7137bdcba7d3



```plantuml
@startuml
package "Backend" {

package "it.polito.ezgas.service"  as ps {
   interface "GasStationService"
   interface "UserService"
} 


package "it.polito.ezgas.controller" {

}

package "it.polito.ezgas.converter" {

}

package "it.polito.ezgas.dto" {

}

package "it.polito.ezgas.entity" {

}

package "it.polito.ezgas.repository" {

}

    
}
note "see folder ServicePackage" as n
n -- ps
```



The Spring framework implements the MC of the MVC pattern. The M is implemented in the packages Entity and Repository. The C is implemented in the packages Service, ServiceImpl and Controller. The packages Dto and Converter contain classes for translation services.



**Entity Package**

Each Model class should have a corresponding class in this package. Model classes contain the data that the application must handle.
The various models of the application are organised under the model package, their Dtos(data transfer objects) are present under the dto package.

In the Entity package all the Entities of the system are provided. Entities classes provide the model of the application, and represent all the data that the application must handle.




**Repository Package**

This package implements persistency for each Model class using an internal database. 

For each Entity class, a Repository class is created (in a 1:1 mapping) to allow the management of the database where the objects are stored. For Spring to be able to map the association at runtime, the Repository class associated to class "XClass" has to be exactly named "XClassRepository".

Extending class JpaRepository provides a lot of CRUD operations by inheritance. The programmer can also overload or modify them. 



**Dto package**

The Dto package contains all the Dto classes. Dto classes are used to transfer only the data that we need to share with the user interface and not the entire model object that we may have aggregated using several sub-objects and persisted in the database.

For each Entity class, a Dto class is created (in a 1:1 mapping).  For Spring the Dto class associated to class "XClass" must be called "XClassDto".  This allows Spring to find automatically the Dto class having the corresponding Entity class, and viceversa. 




**Converter Package**

The Converter Package contains all the Converter classes of the project.

For each Entity class, a Converter class is created (in a 1:1 mapping) to allow conversion from Entity class to Dto class and viceversa.

For Spring to be able to map the association at runtime, the Converter class associated to class "XClass" has to be exactly named "XClassConverter".




**Controller Package**

The controller package is in charge of handling the calls to the REST API that are generated by the user's interaction with the GUI. The Controller package contains methods in 1:1 correspondance to the REST API calls. Each Controller can be wired to a Service (related to a specific entity) and call its methods.
Services are in packages Service (interfaces of services) and ServiceImpl (classes that implement the interfaces)

The controller layer interacts with the service layer (packages Service and ServieImpl) 
 to get a job done whenever it receives a request from the view or api layer, when it does it should not have access to the model objects and should always exchange neutral Dtos.

The service layer never accepts a model as input and never ever returns one either. This is another best practice that Spring enforces to implement  a layered architecture.



**Service Package**


The service package provides interfaces, that collect the calls related to the management of a specific entity in the project.
The Java interfaces are already defined (see file ServicePackage.zip) and the low level design must comply with these interfaces.


**ServiceImpl Package**

Contains Service classes that implement the Service Interfaces in the Service package.










# Low level design

```plantuml
@startuml
left to right direction
skinparam linetype ortho
skinparam nodesep 5
skinparam ranksep 10
Package it.polito.ezgas.controller{
Class UserController{
    boolean createUser()
    boolean authorizeUser()
    UserDto getUser()
    boolean modifyUser()
    boolean deleteUser()
}
Class GasStationController{
    boolean createGasStation()
    boolean modifyGasStation()
    boolean deleteGasStation()
    GasStationDto getGasStation()
    boolean insertReport()
    List<GasStationDto> listGasStations()
    boolean evaluatePrice()
}
}

package it.polito.ezgas.entity{


class User {
 String name
 String surname
 String email
 int trust_level
 boolean isAdmin
 String id
 String password
 
 void setName()
 void setSurname()
 void setEmail()
 void seTrustLevel()
 void setCredentials()
 void setIsAdmin()
 void setId()
 void setPassword()
 Login getCredentials()
 String getName()
 String getEmail()
 String getSurname()
 Int getTrustLevel()
 boolean getIsAdmin()
 void incremenTrustLevel()
 void decremenTrusLevel()
 String getId()
 String getPassword()
}

class Login{
   String email
   String name
   boolean IsAdmin
    
    void setEmail()
    void setIsAdmin()
    void setName()
    String getName()
    String getEmail()
    String getIsAdmin()
}

class GasStation {
 String ID
 String name
 String address
 String brand
 List<String> carSharingCompany
 boolean hasCarSharingCompany
 boolean hasDiesel
 boolean hasGasoline
 boolean hasPremiumDiesel
 boolean hasPremiumGasoline
 boolean hasLPG
 boolean hasMethane
 double dieselPrice
 double gasolinePrice
 double premiumdieselPrice
 double premiumgasolinePrice
 double LPGPrice
 double methanePrice
 TimeStamp time_tag
 int trust_level
 String userId

 
 void setName()
 void setAddress()
 void setBrand()
 void addCarSharingCompany()
 void setHascarSharingCompany()
 void setHasGasoline()
 void setHasDiesel()
 void setHasPremiumDiesel()
 void setHasLPG()
 void setHasMethane()
 void setTimeTag()
 void setDieselPrice()
 void setGasolinePrice()
 void setPremiumDieselPrice()
 void setPremiumGasolinePrice()
 void setLPGPrice()
 void setMethanPrice()
 void setTrust_level()
 void setUserId()
 String getID()
 String getName()
 String getAddress()
 String getBrand()
 List<String> getCarSharingCompany()
 boolean getHascarSharingCompany()
 boolean getHasGasoline()
 boolean getHasDiesel()
 boolean getHasPremiumDiesel()
 boolean getHasLPG()
 boolean getHasMethane()
 TimeStamp getTime_tag()
 double getDieselPrice()
 double getGasolinePrice()
 double getPremiumDieselPrice()
 double getPremiumGasolinePrice()
 double getLPGPrice()
 double getMethanPrice()
 int getTrust_level()
 String getUserId()
}

User"0..1"--GasStation

}

Package it.polito.ezgas.repository{
class UserRepository{ 
User find(userId)
List<User> findAll(List<userId>)
void save(user)
void update(user)
void delete(userId)
void increaseReputationLevel(userId)
void decreaseReputationLevel(userId)
}
class GasStationRepository{
GasStation find(gasStationId)
List<GasStation> findAll(List<gasStationId>)
void save(gasStation)
void update(gasStation)
void delete(gasStationId)
}

class LoginRepository{
Login find(loginId)
List<Login> findAll(List<loginId>)
void save(login)
void update(login)
void delete(loginId)
}

}

Package it.polito.ezgas.converter{
class UserConverter{
    UserDto toUserDto(user)
    User toUser(userdto)
    List<User> toUserList(List<UserDto>)
    List<UserDto> toUserDtoList(List<User>)
}
class GasStationConverter{
   GasStationDto toGasStationDto(gasStation)
   GasStation toGasStation(gasStationdto)
   List<GasStation> toGasStationDtoList(List<GasStationDto>)
   List<GasStationDto> toGasStationList(List<GasStation>)
}
class LoginConverter {
    LoginDto toLoginDto(Login)
    Login toLogin(LoginDto)
}

}

Package it.polito.ezgas.dto{
    class IdPwDto{
    String id
    String password
    boolean setId()
    boolean setPassword()
    String getId()
    String getPassword()
}
Class LoginDto{
    String name
    String email
    boolean isAdmin
    boolean setName()
    boolean setEmail()
    boolean setIsAdmin()
    String getEmail()
    String getName()
    boolean getIsAdmin()
}
Class UserDto{
String name
String surname
String email
int trust_level
String name
boolean isAdmin

void setName()
 boolean setSurname()
 boolean setEmail()
 boolean seTrustLevel()
 boolean setIsAdmin()
 String getName()
 String getEmail()
 String getSurname()
 Int getTrustLevel()
 boolean getIsAdmin()
}
Class GasStationDto{
 String ID
 String name
 String address
 doube latitude
 double longitude
 String brand
 List<String> carSharingCompany
 TimeStamp time_tag
 double dieselPrice
 double gasolinePrice
 double premiumDieselPrice
 double premiumGasolinePrice
 double LPGPrice
 double methanePrice
 double dieselPrice
 double gasolinePrice
 double premiumdieselPrice
 double premiumgasolinePrice
 double LPGPrice
 double methanePrice

 boolean setName()
 boolean setAddress()
 boolean setBrand()
 boolean addCarSharingCompany()
 boolean setIsAdmin()
 void setDieselPrice()
 void setGasolinePrice()
 void setPremiumDieselPrice()
 void setPremiumGasolinePrice()
 void setLPGPrice()
 void setMethanPrice()
 String getID()
 String getName()
 String getAddress()
 String getBrand()
 List<String> getCarSharingCompany()
 double getDieselPrice()
 double getGasolinePrice()
 double getPremiumDieselPrice()
 double getPremiumGasolinePrice()
 double getLPGPrice()
 double getMethanPrice()
 boolean hasCarSharingCompany()
 boolean hasDiesel()
 boolean hasGasoline()
 boolean hasPremiumDiesel()
 boolean hasPremiumGasoline()
 boolean hasLPG()
 boolean hasMethane()

}
}

Package it.polito.ezgas.service{
Interface GasStationService{
}
Interface UserService{
}
}

Package it.polito.ezgas.serviceimpl {
 Class GasStationServiceImp{}
 Class UserServiceImp{}
}


UserService <|-- UserServiceImp
GasStationService <|-- GasStationServiceImp

UserServiceImp -- User
UserServiceImp -- Login
UserServiceImp -- UserConverter
UserServiceImp -- LoginConverter
UserServiceImp -- Login


LoginConverter -- Login
UserConverter -- User
GasStationConverter-- GasStation


LoginConverter -- LoginDto
UserConverter -- UserDto
GasStationConverter-- GasStationDto

LoginRepository --"*" Login
GasStationRepository --"*" GasStation
UserRepository --"*" User

UserDto "*"-- UserServiceImp

GasStationServiceImp --"*" GasStation
GasStationServiceImp -- GasStationConverter

GasStationDto "*"- GasStationServiceImp


UserController -- UserService
GasStationController -- GasStationService


@enduml

```



# Verification traceability matrix


|   | UserController | GasStationController | PriceList | IdPw | User | Login | GasStation | UserRepository | GasStationRepository | PriceListRepository | LoginRepository | IdPwRepository | UserConverter | GasStationConverter | LoginConverter | IdPwConverter | PriceListConverter | IdPwDto | LoginDto | UserDto | GasStationDto | PriceListDto | GasStationServiceImp | UserServiceImp |
|------|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| FR1.1  | X |   |   | X | X | X |   | X |   |   | X | X | X |   | X | X |   | X | X | X |   |   |   | X |  
| FR1.2  | X |   |   | X | X | X |   | X |   |   | X | X | X |   | X | X |   | X | X | X |   |   |   | X |
| FR1.3  | X |   |   | X | X | X |   | X |   |   | X | X | X |   | X | X |   | X | X | X |   |   |   | X |
| FR1.4  | X |   |   | X | X | X |   | X |   |   | X | X | X |   | X | X |   | X | X | X |   |   |   | X |
| FR2    | X |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | X |
| FR3.1  |   | X |   |   |   |   | X |   | X |   |   |   |   | X |   |   |   |   |   |   | X |   | X |   |
| FR3.2  |   | X |   |   |   |   | X |   | X |   |   |   |   | X |   |   |   |   |   |   | X |   | X |   |
| FR3.3  |   | X |   |   |   |   | X |   | X |   |   |   |   | X |   |   |   |   |   |   | X |   | X |   |
| FR4.1  |   | X | X |   |   |   | X |   | X | X |   |   |   | X |   |   | X |   |   |   | X | X | X |   |
| FR4.2  |   | X | X |   |   |   | X |   | X | X |   |   |   | X |   |   | X |   |   |   | X | X | X |   |
| FR4.3  |   | X | X |   |   |   | X |   | X | X |   |   |   | X |   |   | X |   |   |   | X | X | X |   |
| FR4.4  |   | X | X |   |   |   | X |   | X | X |   |   |   | X |   |   | X |   |   |   | X | X | X |   |
| FR4.5  |   | X | X |   |   |   | X |   | X | X |   |   |   | X |   |   | X |   |   |   | X | X | X |   |
| FR5.1  | X |   |   |   | X |   |   | X |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | X |
| FR5.2  | X |   |   |   | X |   |   | X |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | X |
| FR5.3  | X |   |   |   | X |   |   | X |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | X |

# Verification sequence diagrams 

## UC1 - Create User Account
```plantuml
@startuml
        FrontEnd --> UserController : createUser(userDto)
        UserController -> UserService : saveUser(userDto)
        UserService -> UserConverter : toUser(userDto)
        UserConverter -> User: new(...)
        User -> UserConverter: user
        UserConverter -> UserService : user
        UserService -> UserRepository: save(user)
        UserService -> UserController: userDto
        UserController -> FrontEnd: userDto
@enduml
```
## UC2 - Modify user account
```plantuml
@startuml
    FrontEnd --> UserController : modifyUser(userDto)
    UserController -> UserService: saveUser(userDto)
    UserService -> UserConverter: toUser(userDto)
    UserConverter -> User: new(...)
    User -> UserConverter: user
    UserConverter -> UserService : user
    UserService -> UserRepository: update(user)
    UserService -> UserConverter: toDto(user)
    UserConverter -> UserDto: new(...)
    UserDto -> UserConverter: userDto
    UserService -> UserController: userDto
    UserController -> FrontEnd: userDto
@enduml
```
## UC3 - Delete user account
```plantuml
@startuml
    @startuml
    FrontEnd -> UserController: deleteUser(userId)
    UserController -> UserService: deleteUser(userId)
    UserService -> UserRepository: delete(userId)
    UserController -> FrontEnd: booleanResult
    @enduml
@enduml
```
## UC4 - Create Gas Station 
```plantuml
@startuml
     @startuml
        FrontEnd --> GasStationController : createGasStation(gasStationDto)
        GasStationController -> GasStationService : saveGasStation(gasStationDto)
        GasStationService -> GasStationConverter : toGasStation(gasStationDto)
        GasStationConverter -> GasStation: new(...)
        GasStation -> GasStationConverter: gasStation
        GasStationConverter -> GasStationService: gasStation
        GasStationService -> GasStationRepository: save(gasStation)
        GasStationService -> GasStationController: gasStationDto
        GasStationController -> FrontEnd: booleanResult
@enduml
```
## UC5 - Modify Gas Station information
```plantuml
@startuml
     @startuml
    FrontEnd -> GasStationController : modifyGasStation(gasStationDto)
    GasStationController ->GasStationService: saveGasStation(gasStationDto)
    GasStationService -> GasStationConverter: toGasStation(gasStationDto)
    GasStationConverter -> GasStation: new(...)
    GasStation -> GasStationConverter: gasStation
    GasStationConverter -> GasStationService: gasStation
    GasStationService -> GasStationRepository: update(gasStation)
    GasStationService -> GasStationConverter: toDto(gasStation)
    GasStationConverter -> GasStationDto: new(...)
    GasStationDto -> GasStationConverter: gasStationDto
    GasStationService -> GasStation: gasStationDto
    GasStationController -> FrontEnd: booleanResult
@enduml
```
## UC6 - Delete Gas Station
```plantuml
 @startuml
    FrontEnd -> GasStationController: deleteGasStation(gasStationId) 
    GasStationController -> GasStationService: deleteGasStation(gasStationId)
    GasStationService -> GasStationRepository: delete(gasStationId)
    GasStationController -> FrontEnd: booleanResult
@enduml
```
## UC7 - Report fuel price for a gas station
```plantuml
@startuml
    FrontEnd -> GasStationController : insertReport(gasStationId,dieselPrice,superPrice,superPlusPrice,gasPrice,methanePrice,userId)
    GasStationController -> GasStationService: setReport(gasStationId,dieselPrice,superPrice,superPlusPrice,gasPrice,methanePrice,userId)
    GasStationService -> GasStationRepository: find(gasStationId)
    GasStationRepository -> GasStationService: gasStation
    GasStationService -> GasStation: gasStation.setPriceList(gasStationId,dieselPrice,superPrice,superPlusPrice,gasPrice,methanePrice,userId)
    GasStationService -> GasStationRepository: update(GasStation)
    GasStationController -> FrontEnd: booleanResult
@enduml
```
## UC8 - Obtain price of fuel for gas stations in a certain geographic area
```plantuml
@startuml
    FrontEnd -> GasStationController : listGasStations(lat,lon,gasolinetype,carsharing)
    GasStationController -> GasStationService: getGasStationsWithCoordinates(lat,lon,gasolinetype,carsharing)
    GasStationService -> GasStationRepository : findAll(lat,lon,gasolinetype,carsharing)
    GasStationRepository -> GasStationService: List<GasStation>
    GasStationService -> GasStationConverter: toGasStationDtoList(List<GasStation>)
    GasStationConverter -> GasStationService: List<GasStatioDto>
    GasStationService -> GasStationController: List<GasStatioDto>
    GasStationController -> FrontEnd: List<GasStationDto>
@enduml

```
## UC10 - Evaluate price

### Scenario 10.1 - Increase Reputation

```plantuml
@startuml
    FrontEnd -> UserController : evaluatePrice()
    UserController -> UserService: IncreaseUserReputation(userId)
    UserService -> UserRepository: increaseReputation(userId)
    UserController -> FrontEnd: booleanResult
@enduml
```

### Scenario 10.2 - Decrease Reputation

```plantuml
@startuml
    FrontEnd -> UserController : evaluatePrice()
    UserController -> UserService: DecreaseUserReputation(userId)
    UserService -> UserRepository: DecreaseReputation(userId)
    UserController -> FrontEnd: booleanResult
@enduml
```


