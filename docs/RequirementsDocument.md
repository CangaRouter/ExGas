# Requirements Document 

Authors:

Date:

Version:

# Contents

- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)


# Stakeholders


| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|User               |Uses the application          |
|Unregistered user - User Profile 1	|He uses the application without an account. He can view the list of gas stations around him and give a feedback about prices veracity|
|Registered user - User Profile 2|He uses the application with an account. Because of it, he can also notify a different price for a certain gas station or an error. He can also get points for keeping prices updated|
|App Developer | manages the system and databases |
|GoogleMaps | The app is based on a map system (like Google Maps) and every change can possibly modify the behaviour of the application|


# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|       |  |  |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as use cases>


# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<will match to high level use cases>

| ID        | Description  |
| ------------- |:-------------:| 
|  FR_RU1     |Perform log in and log out operations  |
|  FR_RU2     |Registered user can insert the fuel type price for a gas station   |
|  FR_RU3	  |Registered user can signal a price error|
|  FR_RU4     |Registered user can signal a location error|
|  FR_RU5     |Registered user can add a new gas station|
|  FR_RU6     |Registered user can signal a gas station closure|
|  FR_RU7     |Registered user can see the points he collected and their equivalent money value in his wallet|
|  FR_RU8     |Registered user can print the coupon|
|  FR_RU9     |Registered user can delete his account|
|  FR_RU10    |Registered user can update its profile and settings|
|  FR_RU11    |Registered user can insert a gas station into a favourite list|
## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR_RU1     | Performance  |All operations should be completed in less than 1 sec  | ALL FR_RU |
|  NFR_RU2     | Localisation |Prices are expressed in EUR  |FR_RU2, FR_RU3, FR_RU7, FR_RU8 |
|  NFR_RU3     | Usability | Send a notification to registered user when one of his favourites gas stations' price is updated |FR_RU5, FR_RU11 |
|  NFR_RU4     | Usability | When a registered user insert a new gas station or notify a closure, it's necessary to attach a photo of the gas station and check if his location is around 500m|FR_RU5, FR_RU6|
|  NFR_RU5	   | Localisation | Points are integer values |FR_RU2, FR_RU7|
|  NFR_RU6     | Usability |Registered user receives points when he insert a new price. Points are computing according to this time parameter:  p= (timestamp_lastupdate-timestamp_now). The older the last update , the higher the number of points (p=24h -> 10 points; p=48h->15 points; ...) |FR_RU2, FR_RU7|
|  NFR_RU7     | Usability |Registered user receives 50 points when the insert or the signal of closure of a gas station is correctly verified. |FR_RU2, FR_RU7|
| NFR_RU8 | Usability | 100 points = 1 EUR | FR_RU7, FR_RU8|


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>


\<next describe here each use case in the UCD>
### Use case 1, UC1
Registration, login, logout
| Actors Involved        | Registered user |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |  
|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     |in case of registration or login errors issue a warning |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>
Registration
| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | Account does not exist |
|  Post condition     | Account exists |
| Step#        | Description  |
|  1     | The user clicks to the registration button  |  
|  2     | He fills a form with all required informations |
|  3     |  He has an account |

##### Scenario 1.2
login
| Scenario 1.2 | |
| ------------- |:-------------:| 
|  Precondition     | User is not logged in|
|  Post condition     | User is logged in |
| Step#        | Description  |
|  1     | The  user clicks to the login button  |  
|  2     | He inserts username and password |
|  3     |  He is logged |

##### Scenario 1.3
logout
| Scenario 1.3 | |
| ------------- |:-------------:| 
|  Precondition     | User logged in|
|  Post condition     | User is not logged in |
| Step#        | Description  |
|  1     | The user clicks to the logout button  |  
|  3     |  He is not logged anymore|

### Use case 2, UC2
Visualize the list of gas stations
| Actors Involved        | User, Google Maps |
| ------------- |:-------------:| 
|  Precondition     | Map M exists, Gas stations' positions <= User position + 10 km |  
|  Post condition     |  |
|  Nominal Scenario     |The user opens the app, the map is retrieved by the map system and the gas stations (with their prices) are list on the map. |
|  Variants     | |

##### Scenario 2.1
| Scenario 2.1 | |
| ------------- |:-------------:| 
|  Precondition     | User GPS should be activated|
|  Post condition     | All the gas stations are in a range of 10 km around the user position |
| Step#        | Description  |
|  1     | The  user opens the app  |  
|  2     | The GPS signal is captured |
|  3     | The map is retrieved by GoogleMaps |
|  4     | The gas stations are selected and listed with their prices on the map|
|  5     | The cheapest and the most expensive gas stations are highlighted|

### Use case 3, UC3
Get the route 
| Actors Involved        | User, Google Maps |
| ------------- |:-------------:| 
|  Precondition     | Map M exists, Gas station G exists |  
|  Post condition     | User's location <= G's location + 500mt |
|  Nominal Scenario     |The user choices a gas station on the list and the GoogleMaps system gets a route for it. |
|  Variants     | |

##### Scenario 3.1
| Scenario 3.1 | |
| ------------- |:-------------:| 
|  Precondition     | User GPS should be activated|
|  Post condition     | User's position <= G's position + 500mt |
| Step#        | Description  |
|  1     | The  user choices a gas station G  |  
|  2     | The user presses the route button |
|  3     | User's location and G's location are sent to GoogleMaps  |
|  4     | GoogleMaps provides a route|
|  5     | User follows this route and he reaches the gas station G|

### Use case 4, UC4
Manage Feedback
| Actors Involved        | User |
| ------------- |:-------------:| 
|  Precondition     | Map M exists, Gas station G exists, User's location < G's location + 3 km, User has not already expressed a feedback for G on that day |  
|  Post condition     |User can no longer express a feedback for G on that day |
|  Nominal Scenario     |The user choices a gas station in a renge of 3km around him and sent a feedback (positive/negative) about the correctness of prices. |
|  Variants     | |

##### Scenario 4.1
| Scenario 4.1 | |
| ------------- |:-------------:| 
|  Precondition     |Map M exists, Gas station G exists, User's location < G's location + 3 km, User has not already expressed a feedback for G on that day|
|  Post condition     | User can no longer express a feedback for G on that day |
| Step#        | Description  |
|  1     | The  user choices a gas station G  |  
|  2     | The user presses one of the feedback's buttons (up/down) |

### Use case
..



# Glossary

\<use UML class diagram to define important concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >
