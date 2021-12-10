# Core Bridge Recommendation Connector

# Library basic information

This library was generated with java jdk-17 LTS and Gradle 7.3.

The main purpose of the library is to cover and handle main implementation in Java language of
classes using GRPC protocol based on protobuf contracts. You do not need to implement those by
yourself, just use it from client side by simply including this library in project dependencies.

## How to use library from client side

1. Add library to your project by adding it to dependencies.gradle script for example:

```
libraries.recommendation_api_library = [group: 'com.silenteight.recommendation.api.library', name: 'recommendation-lib', version: 1.0.0-SNAPSHOT]

dependencies {
 implementation libraries.recommendation_api_library
}
```

## Naming convention of library

1. Naming convention for this library follows a few rules:

    - REQUEST
        - request type going as an argument to endpoint method have postfix **In** appended at the
          end of the type name for example **RecommendationsIn**

    - RESPONSE
        - response going as a returned type from endpoint method have postfix **Out** appended at
          the end of the type name for example **RecommendationOut**
        - if in response we have fields based on custom DTO classes they have also postfix **Out**
          appended at the end of the type name for example **RecommendationOut**

    - ADAPTER
        - classes that are aggregating BlockingStub field use postfix **GrpcAdapter**
          appended at the end of the class name for example **RecommendationServiceGrpcAdapter**

    - PORT
        - interfaces that are implemented by adapters use postfix **ServiceClient**
          appended at the end of the interface name for example **RecommendationServiceClient**

    - UTIL
        - util classes have postfix **Util** appended at the end of the class name for example
          **TimestampUtil**
        - 
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
