# Governance Connector

# Library basic information

This library was generated with java jdk-11.0.7 LTS and Gradle 7.2 building tool.

The main purpose of the library is to cover and handle main implementation in Java language of
classes using GRPC protocol based on protobuf contracts. You do not need to
implement it by yourself, but just use it from client side by simply including library in project
dependencies.

## How to use library from client side

1. Add library to your project by adding it to dependencies.gradle script for example:

```
libraries.silenteight_governance_model_api_library = [group: 'com.silenteight.governance.api.library', name: 'governance-lib', version: 1.0.0-SNAPSHOT]

dependencies {
 implementation libraries.silenteight_governance_model_api_library 
}
```

## Naming convention of library

1. Naming convention for this library follows few rules:

    - REQUEST
        - request type going as an argument to endpoint method have postfix **In** appended at the
          end of the type name for example **ModelPromotedForProductionIn**
        - if in request type have fields based on custom DTO classes they have also postfix
          **In** appended at the end of the type name for example **FeatureIn**

    - RESPONSE
        - response going as a returned type from endpoint method have postfix **Out** appended at
          the end of the type name for example **SolvingModelOut**
        - if in response we have fields based on custom DTO classes they have also postfix **Out**
          appended at the end of the type name for example **FeatureOut**

    - ADAPTER
        - classes that are aggregating BlockingStub field use postfix **GrpcAdapter**
          appended at the end of the class name for example **ModelGrpcAdapter**

    - PORT
        - interfaces that are implemented by adapters use postfix **ServiceClient**
          appended at the end of the interface name for example **ModelServiceClient**

    - UTIL
        - util classes have postfix **Util** appended at the end of the class name

## Summary

For now only endpoints used by Bridge component are implemented, each team responsible for their
component need to add their endpoint implementation to the library.
