package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.fab.dataprep.infrastructure.grpc.UniversalDataSourceGrpcServiceConfiguration.GrpcProperties;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceAdapter;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;

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

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcProperties.class)
class UniversalDataSourceGrpcServiceConfiguration {

  @GrpcClient(KnownServices.UNIVERSAL_DATA_SOURCE)
  AgentInputServiceBlockingStub agentInputServiceBlockingStub;

  @Bean
  @Profile("!dev")
  AgentInputServiceClient agentInputServiceClient(GrpcProperties grpcProperties) {
    return new AgentInputServiceAdapter(
        agentInputServiceBlockingStub, grpcProperties.getDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  AgentInputServiceClient agentInputServiceClientMock() {
    return new AgentInputServiceClientMock();
  }

  @Validated
  @ConstructorBinding
  @ConfigurationProperties("grpc.client.universal-data-source")
  @Value
  static class GrpcProperties {

    @NotNull
    Duration deadline;
  }
}
