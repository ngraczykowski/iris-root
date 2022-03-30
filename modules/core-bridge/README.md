# Core Bridge

## Documentation

[Core Bridge Flow](https://whimsical.com/bridge-flow-v2-RpoCuxSMUszaXijf34na8y) diagram shows all steps needed for batch (and its all recommendations) to be completed and delivered to the client.  

In the `docs/` directory you can find a bit more detailed documentation, such as:
* [Batch Expiration Check Feature](docs/batch_expiration.adoc)

more info soon...

## Usage

### Configuration
Below you can find all configuration parameters that can be set to customize the Core Bridge.

| name                           	| description                                                                     	                            | path                                                            	| type     	| env variable (can be set e.g. via Consul                      | default 	|
|--------------------------------	|---------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------	|----------	|------------------------------------------------------------	|---------	|
| Batch Expiration Check Enabled 	| Specifies whether batch should expire after the specified time. More info [here](docs/batch_expiration.adoc) 	| amqp.registration.outgoing.verify-batch-timeout.timeout-enabled 	| boolean  	| AMQP_REGISTRATION_OUTGOING_VERIFY_BATCH_TIMEOUT_ENABLED    	| true    	|
| Batch Expiration Check Timeout 	| Specifies the time after which the batch should expire. More info [here](docs/batch_expiration.adoc)          | amqp.registration.outgoing.verify-batch-timeout.delay-time      	| duration 	| AMQP_REGISTRATION_OUTGOING_VERIFY_BATCH_TIMEOUT_DELAY_TIME 	| 60m     	|

There are two recommended ways of customizing the Core Bridge settings:
* S8 Nomad environment:
  * Add env variables to the Consul Key/Value according to your namespace. For example if your project is deployed on `mike` namespace you need to edit [these](http://10.8.0.1:8500/ui/dc1/kv/mike/core-bridge/secrets/edit) values.
* On-premise environment:
  * Overwrite values in application.yml file at the appropriate environment specific configuration project, e.g. `ms-sear-installer`.

> Important notice: Since all secrets (env variables) are loaded during the app startup you need to restart Core Bridge to notice changes.  

### Deployment
Core Bridge application runs on **Java 17**
#### Nomad
Nowadays, Core Bridge supports deployment on the Nomad platform.

### gRPC Connectors

They are a convenient way to integrate with the Core Bridge when you don't want to waste your
valuable time on implementing gRPC adapters.

There are 2 connectors that can be used out of the box:
* [Core Bridge Recommendation Connector](https://gitlab.silenteight.com/all-in/core-bridge-recommendation-connector)
* [Core Bridge Registration Connector](https://gitlab.silenteight.com/all-in/core-bridge-registration-connector)

Read their READMEs in order to learn how to add them to your project.
Next step is the configuration described below.

### Core Bridge Recommendation Connector

There is an interface named `RecommendationServiceClient` and its implementation `RecommendationServiceGrpcAdapter`.
The point is to create an instance of `RecommendationServiceGrpcAdapter` what can be achieved
by creating a bean, for example:

```java
import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceGrpcAdapter;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RecommendationGrpcServiceConfiguration {

  @GrpcClient("recommendation")
  RecommendationServiceBlockingStub recommendationServiceBlockingStub;

  @Bean
  RecommendationServiceClient recommendationServiceClient() {
    var deadlineInSeconds = 60;
    return new RecommendationServiceGrpcAdapter(recommendationServiceBlockingStub, deadlineInSeconds);
  }
}
```

After that, the bean is ready to be used.
Just call `RecommendationServiceClient recommendationServiceClient` to make a gRPC request.

#### Core Bridge Registration Connector

Configuring the connector is analogous to the one mentioned before i.e. create a bean of `RegistrationServiceClient`
by creating an instance of `RegistrationServiceGrpcAdapter`.

```java
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceGrpcAdapter;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegistrationGrpcServiceConfiguration {

  @GrpcClient("registration")
  RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @Bean
  RegistrationServiceClient registrationServiceClientGrpcApi() {
    var deadlineInSeconds = 60;
    return new RegistrationServiceGrpcAdapter(registrationServiceBlockingStub, deadlineInSeconds);
  }
}
```

## Development
Core Bridge application uses **Java 17**
### Application ports

| Service  | Port    |
|:---------|:--------|
| HTTP     | 24805   | 
| GRPC     | 24806   |

### Accessing services in Docker

Services are exposed on locally accessible port numbers. The table below shows how to access them.

| Service    | URL                              | User    | Password  |
|:-----------|:---------------------------------|:--------|:----------|
| RabbitMQ   | http://localhost:5681/           | `dev`   | `dev`     |

### Running tests in Gradle

By default, build run all tests. However, some integration tests take too much time. In order to
make work faster, we added the flag `-PunitTests` to run only unit tests. <br>

Example <br>
`./gradlew clean build ` - run build with all tests <br>
`./gradlew clean build -PunitTests` - run build only with unit tests

This same works with command `test` <br>
`./gradlew clean test ` - run all tests <br>
`./gradlew clean test -PunitTests` - run only unit tests

Tests are considered as unit when they have a suffix `Spec` in their class name e.g. `MyServiceSpec`.
On the other hand, tests are considered as integration when they have a suffix `IntegrationSpec` in their class name
e.g. `DatabaseIntegrationTestIntegrationSpec`.
So it's important to name test classes appropriately.
