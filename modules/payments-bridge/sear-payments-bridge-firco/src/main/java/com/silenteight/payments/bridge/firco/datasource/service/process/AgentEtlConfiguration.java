package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AgentEtlProperties.class)
class AgentEtlConfiguration {

  @Valid
  private final AgentEtlProperties properties;

  @Setter(onMethod_ = @GrpcClient("data-source"))
  private Channel dataSourceChannel;

  @Bean
  NameAgentEtlProcess nameAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new NameAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  GeoAgentEtlProcess geoAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new GeoAgentEtlProcess(stub, properties.getTimeout());
  }

}
