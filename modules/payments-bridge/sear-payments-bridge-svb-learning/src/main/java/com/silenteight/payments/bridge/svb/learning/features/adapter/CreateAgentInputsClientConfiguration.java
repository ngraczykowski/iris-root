package com.silenteight.payments.bridge.svb.learning.features.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;
import com.silenteight.payments.bridge.svb.learning.features.port.outgoing.CreateAgentInputsClient;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CreateAgentInputsClientProperties.class)
class CreateAgentInputsClientConfiguration {

  @Valid
  private final CreateAgentInputsClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("data-source"))
  private Channel categoriesDataChannel;

  @Bean
  CreateAgentInputsClient createAgentInputsClient() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(categoriesDataChannel)
        .withWaitForReady();

    return new CreateAgentInputsAdapter(stub, properties.getTimeout());
  }
}
