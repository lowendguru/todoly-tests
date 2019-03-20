# Todo.ly Tests

# Documents

# Automated Test Suite

These tests are written in Java using 'REST-assured' and 'testNG' as a testing framework.
 

# Test Execution

## Preconditions

The requirements to run the automated tests are:
- Maven
- Java 8

### Maven project

This is a maven project, so the test suite can be executed via command line. 
```
    $mvn clean test
```
This will run all the tests according to the test suite structure defined in the testng.xml file.

### Test reports
 Results will be available in the /test-output/${timestamp} folder. Every execution creates a new /${timestamp} subfolder. This way all previous executions are saved for future reference.


### Structure

#### Test classes
All tests extend the BaseTest class, thus allowing its methods to be reusable and maintainable.
#### Utils classes
The classes under the "utils" package are auxiliary classes with reusable code.
#### Properties file
The "datafile.properties" file has several parameters used during test execution. This prevents values from being hardcoded in the test classes thus allowing for easy code maintenance.