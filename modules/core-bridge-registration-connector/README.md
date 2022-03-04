# Core Bridge Registration Connector

# Library basic information

This library was generated with java jdk-17 LTS (downgraded to jdk-11 from 1.14.0 version) and Gradle 7.3.

The main purpose of the library is to cover and handle main implementation in Java language of
classes using GRPC protocol based on protobuf contracts. You do not need to implement those by
yourself, just use it from client side by simply including this library in project dependencies.

## Naming convention of library

1. Naming convention for this library follows a few rules:

    - REQUEST
        - request type going as an argument to endpoint method have postfix **In** appended at the
          end of the type name for example **RegisterAlertsAndMatchesIn**
        - if in request type have fields based on custom DTO classes they have also postfix
          **In** appended at the end of the type name for example **AlertWithMatchesIn**

    - RESPONSE
        - response going as a returned type from endpoint method have postfix **Out** appended at
          the end of the type name for example **RegisterAlertsAndMatchesOut**
        - if in response we have fields based on custom DTO classes they have also postfix **Out**
          appended at the end of the type name for example **RegisteredAlertWithMatchesOut**

    - ADAPTER
        - classes that are aggregating BlockingStub field use postfix **GrpcAdapter**
          appended at the end of the class name for example **RegistrationServiceGrpcAdapter**

    - PORT
        - interfaces that are implemented by adapters use postfix **ServiceClient**
          appended at the end of the interface name for example **RegistrationServiceClient**

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
