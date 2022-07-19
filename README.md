# ABN Code Challenge

## Requirements

For building and running the application you need:

- [JDK 11](https://adoptium.net/temurin/releases/?version=11) and [JAVA_HOME](https://docs.oracle.com/cd/E19182-01/821-0917/inst_jdk_javahome_t/index.html#:~:text=To%20set%20JAVA_HOME%2C%20do%20the,6.0_02.) set in environmental variables
- [Maven 3](https://maven.apache.org) optional as MVN wrapper is added in source code.
- Open Command Prompt pointing to root director of the source code location.

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.abn.food.recipe.AbnRecipeApplication` class from your IDE.

Alternatively you can use the **Spring Boot Maven plugin** like so:

```shell
.\mvnw spring-boot:run
```

## Running the Integration Tests

Integration tests are written as part of unit testing and can be found in class `AbnRecipeApplicationIntegrationTests`.\
The test cases are written in order of execution for performing CRUD operations and can be run with command.
```shell
.\mvnw test
```

## Reading the API end-points documentation
API endpoints and response schema can be seen and tested via [Open-API Swagger UI](http://localhost:8080/swagger-ui/index.html).\
Swagger Json schema can be accessed via [JSON](http://localhost:8080/v3/api-docs/) & [YAML](http://localhost:8080/v3/api-docs.yaml) format.

## Architectural decisions
* The API end-points have been designed keeping in my mind user interaction of doing CRUD operations as Admin and displaying inventory to the end-user with filtering options available.
* The data is persisted using relational DB(H2 in-memory) for assignment purpose but the solution is scalable to NOSQL databases also.
* The rest resources are defined according to Open-API specifications.
* The solution is designed and developed to be also a cloud deployable component(Azure App-services) with minimal changes.
* The performance of the application is also considered by guaranteeing response time at O(n) - response time in milliseconds.
* Code Quality & coding conventions are followed.
* In /GET all call, instruction & ingredient fields mapping has been skipped considering UI loading time as content might have images or long text.
* Git commit messages conventions have been followed