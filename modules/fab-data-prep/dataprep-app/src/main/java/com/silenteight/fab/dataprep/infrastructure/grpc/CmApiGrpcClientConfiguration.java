package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.Value;

import com.silenteight.proto.fab.api.v1.AlertMessageDetailsServiceGrpc.AlertMessageDetailsServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

import static com.silenteight.fab.dataprep.infrastructure.grpc.KnownServices.CM_API_CONNECTOR;
import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableConfigurationProperties(CmApiGrpcClientConfiguration.Properties.class)
class CmApiGrpcClientConfiguration {

  @GrpcClient(CM_API_CONNECTOR)
  private AlertMessageDetailsServiceBlockingStub alertMessageDetailsServiceBlockingStub;

  @Bean
  @Profile("!cmapiconnectormock")
  AlertDetailsServiceClient alertsDetailsServiceClient(
      CmApiGrpcClientConfiguration.Properties properties) {
    return new AlertDetailsServiceClient(alertMessageDetailsServiceBlockingStub.withDeadlineAfter(
        properties.getDeadline().getSeconds(), SECONDS));
  }

  @Bean
  @Profile("cmapiconnectormock")
  AlertDetailsServiceClient alertsDetailsServiceClientMock() {
    return new AlertDetailsServiceClientMock();
  }

  @Validated
  @ConstructorBinding
  @ConfigurationProperties("grpc.client.cm-api-connector")
  @Value
  static class Properties {

    @NotNull
    Duration deadline;
  }
}
