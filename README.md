# Todo.ly Tests

The goal of this project is to demonstrate a test approach for API test automation.

# Documents
See the /docs folder to find more detailed documents including:
- Test plan
- Defects report 
- Evidence of defects and of suite execution under /docs/evidence

# Automated Test Suite

These tests are written in Java using 'REST-assured' and 'testNG' as a testing framework.
 

# Test Execution

## Preconditions

The requirements to run the automated tests are:
- Maven
- Java 8

### Maven project

This is a maven project. If Java and Maven are correctly configured in the host system, the test suite can be executed via command line. 
```
    $mvn clean test
```
This will run all the tests according to the test suite structure defined in the src/test/resources/runners/testng.xml file.

### Test reports
 Results will be available in the /test-output/${timestamp} folder. Every execution creates a new /${timestamp} subfolder. This way all previous executions are saved for future reference.


### Structure

#### Test classes
All tests extend the BaseTest class, thus allowing the common methods and constants to be reusable and maintainable.
#### Utils classes
The classes under the "utils" package are auxiliary classes with reusable code.
#### Properties file
The "datafile.properties" file in /resources has several parameters used during test execution. This prevents values from being hardcoded in the test classes thus allowing for easier code maintenance.