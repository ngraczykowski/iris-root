package com.silenteight.scb.feeding.infrastructure.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.scb.feeding.infrastructure.util.KnownServices;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceAdapter;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UniversalDataSourceGrpcConfigurationProperties.class)
class UniversalDataSourceGrpcServiceConfiguration {

  @GrpcClient(KnownServices.UNIVERSAL_DATA_SOURCE)
  AgentInputServiceBlockingStub agentInputServiceBlockingStub;

  @Bean
  @Profile("!dev")
  AgentInputServiceClient agentInputServiceClient(
      UniversalDataSourceGrpcConfigurationProperties grpcProperties) {
    return new AgentInputServiceAdapter(
        agentInputServiceBlockingStub, grpcProperties.udsDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  AgentInputServiceClient agentInputServiceClientMock() {
    return new AgentInputServiceClientMock();
  }
}
