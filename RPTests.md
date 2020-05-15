# Unit Testing Documentation

Authors: Barco Luca, Petruzzi Rocco Luigi

Date: 15/05/2020

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)

- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezgas   You find here, and you can use,  class EZGasApplicationTests.java that is executed before 
    the set up of all Spring components
    >

 ### **Class *User* - method *setUserId***


**Criteria for method *setUserId*:**
	
 - Is an integer number
 - Range

**Predicates for method *setUserId*:**

| Criteria | Predicate |
| -------- | --------- |
|  Is an integer number        |     Yes      |
|          |      No     |
|  Range        |   >= -2147483648   and <= 2147483647   |
|          |     < -2147483648      |
|          |     > 2147483647      |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|     Range     |     -2147483648,  2147483647          |
|          |                 |

**Combination of predicates**:

| Is an integer number | Boundary values | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|Yes    |>= -2147483648   and <= 2147483647| V |  setUserId(8888);||
||||getUserId()-> 8888 ||
||< -2147483648 | I | setUserId(-2147483649) -> error||
|| > 2147483647| I | setUserId(2147483648) -> error||
|No    |>= -2147483648   and <= 2147483647| I | Not feasible||
||< -2147483648 | I | Not feasible||
|| > 2147483647| I | Not feasible||


 ### **Class *User* - method *setUserName***


**Criteria for method *setUserName*:**
	
 - String validity

**Predicates for method *setUserName*:**

| Criteria | Predicate |
| -------- | --------- |
|  String validity        |     Yes      |
|          |      No     |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

| String validity  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes    | V |  setUserName("Enrico Mattei");||
||| getUserName() -> "Enrico Mattei" ||
|No    | I | setUserName(null) -> error||


 ### **Class *User* - method *setPassword***


**Criteria for method *setPassword*:**

 - String validity

**Predicates for method *setPassword*:**

| Criteria | Predicate |
| -------- | --------- |
|  String validity        |     Yes      |
|          |      No     |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

| String validity  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes    | V |  setPassword("Th1s@ppISc00l"); ||
|||getPassword -> "Th1s@ppISc00l" ||
|No    | I | setPassword(null) -> error||


### **Class *User* - method *setEmail***


**Criteria for method *setEmail*:**
	
 - String validity
 - Number of "@"

**Predicates for method *setPassword*:**

| Criteria | Predicate |
| -------- | --------- |
|   String validity     |     Yes      |
|          |      No     |
|   Number of "@"       | 0 |
|  | 1 |
|  | >1 |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:


| Number of "@" | String validity  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|----------|-------|-------|
| 0 | - | I |  setEmail("enrico.matteieni.it") -> error ||
| >1 | - | I | setEmail("enrico.mattei@@eni.it") -> error ||
| 1 | Yes| V | setEmail("enrico.mattei@eni.it");||
|||| getEmail -> "enrico.mattei@eni.it"||
|   | No | I | Not feasible ||


 ### **Class *User* - method *setReputation***


**Criteria for method *setReputation*:**
	
 - Is an integer number
 - Range

**Predicates for method *setReputation*:**

| Criteria | Predicate |
| -------- | --------- |
|  Is an integer number        |     Yes      |
|          |      No     |
|  Range        |   >= -2147483648   and <= 2147483647   |
|          |     < -2147483648      |
|          |     > 2147483647      |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|     Range     |     -2147483648,  2147483647          |
|          |                 |

**Combination of predicates**:

| Is an integer number | Boundary values | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|Yes    |>= -2147483648   and <= 2147483647| V |  setReputation(100);||
||||getReputation() -> 100 ||
||< -2147483648 | I | setReputation(-2147483649) -> error||
|| > 2147483647| I | setReputation(2147483648) -> error||
|No    |>= -2147483648   and <= 2147483647| I | Not feasible||
||< -2147483648 | I | Not feasible||
|| > 2147483647| I | Not feasible||


### **Class *User* - method *setAdmin***


**Criteria for method *setAdmin*:**

 - Is a boolean

**Predicates for method *setAdmin*:**

| Criteria | Predicate |
| -------- | --------- |
|  Is a boolean       |     Yes      |
|          |      No     |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

| Is a boolean  | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes    | V |  setAdmin(true);||
||| getAdmin() -> false ||:
|No    | I | setAdmin("I'm an admin") -> error||
# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezgas>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||



