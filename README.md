Employee Project
Description
This project is a Java Spring Boot application implementing CRUD operations using Spring Data JPA's CrudRepository. It includes unit tests using JUnit in combination with Mockito for mocking dependencies, and integration tests utilizing TestContainers for database testing.

Setup Instructions
Clone the Repository:
git clone <repository-url>

Build the Project:
cd <project-directory>
mvn clean install

Run Tests:
mvn test

This will execute both unit and integration tests.

Technologies Used
Java
Spring Boot
Spring Data JPA
JUnit
Mockito
TestContainers
Folder Structure
src/main/java: Contains the source code of the Spring Boot application.
src/test/java: Includes unit and integration tests.
src/main/resources: Configuration files and static resources.
Configuration
Modify application.properties for configuring database connections or any other environment-specific configurations.
Unit tests are located under src/test/java and should be created accordingly.
Running the Application

To run the Spring Boot application:
mvn spring-boot:run

Michael Royf





