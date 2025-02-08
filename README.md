## Project Overview

This project is a coding exercise for the Branch App.

## Prerequisites

Make sure you have the following installed:

- [Java 23](https://openjdk.org/projects/jdk/23/)
- A supported IDE such as IntelliJ IDEA (recommended)

## Running

To clean the project and start the application, use the following command:

```shell
./gradlew clean bootRun
```

To verify the application is running, use the following command:
```shell
curl localhost:8080/actuator/health
```

It will return the following JSON response if available:
```json
{
  "status": "UP"
}
```

## Open API Documentation

Open API documentation is available at `http://localhost:8080/docs` when the application is running. 

## Testing

### Running Unit Tests

To execute the unit tests, run:

```shell
./gradlew clean test
```

### Running Integration Tests

To execute the integration tests, run:

```shell
./gradlew clean integrationTest
```

### Running All Tests

To run all unit, integration, and other test types, run:

```shell
./gradlew clean check
```

## Notes
- Make sure all dependencies are correctly downloaded during the Gradle build process.
- Follow standard coding practices while modifying the code.
