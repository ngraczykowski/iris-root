# Scb Bridge

SCB Bridge application uses **Java 17** 

## Application ports

| Service  | Port  |
|:---------|:------|
| HTTP     | 24220 |
| GRPC     | 24221 |

## Accessing services in Docker

Services are exposed on locally accessible port numbers. The table below shows how to access them.

| Service    | URL                      | User    | Password   |
|:-----------|:-------------------------|:--------|:-----------|
| RabbitMQ   | http://localhost:5681/   | `serp`  | `serp`     |

## Running tests in Gradle

By default, build run all tests. However, some integration tests take too much time. In order to
make work faster, we added the flag `-Punit` to run only unit tests. <br>

Example <br>
`./gradlew clean build ` - run build with all tests <br>
`./gradlew clean build -PunitTests` - run build only with unit tests

This same works with command `test` <br>
`./gradlew clean test ` - run all tests <br>
`./gradlew clean test -PunitTests` - run only unit tests

Tests are considered as integration when they have the word `IT` as postfix in the class name
eg: `DatabaseIntegrationTestIT` or they have the word `Integration` in the class name
eg: `DatabaseIntegrationTest`. So it's important to name test classes appropriately.

## Nomad Deployment