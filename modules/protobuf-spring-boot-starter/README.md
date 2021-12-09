# Protobuf Spring Boot Starter

## Description
Starter is responsible for providing autoconfiguration for protobuf `content-type` messages serialization/deserialization while sending and receiving messages from AMQP based Broker (e.g. RabbitMQ).

Protobuf Starter autoconfigures the following beans:
- `AmqpProtoMessageConverter` - responsible for converting messages
- `MessageRegistry` - registry that stores all `proto` types. Currently, it takes all classes implementing `com.google.protobuf.Message` from the following packages: 
  - com.silenteight
  - com.google.protobuf
  - com.google.rpc
  - com.google.type 
- `ContentTypeDelegatingMessageConverter` - it configures converters based on `content-type`:
  - `AmqpProtoMessageConverter` - `application/x-protobuf`
  - `Jackson2JsonMessageConverter` - `application/json`

## Usage
All you need to do if you want to auto serialize/deserialize your classes while sending/receiving it to/from is add the following dependency to your `pom.xml` or `build.gradle`

> **Please not that all your proto classes should be placed in `com.silenteight.*` package to be registered correctly**

### Maven
```xml
<dependency>
    <groupId>com.silenteight</groupId>
    <artifactId>protobuf-spring-boot-starter</artifactId>
    <version>1.6.0</version>
</dependency>
```

### Gradle
```
compile(group: 'com.silenteight', name: 'protobuf-spring-boot-starter', version: '1.6.0')
```
