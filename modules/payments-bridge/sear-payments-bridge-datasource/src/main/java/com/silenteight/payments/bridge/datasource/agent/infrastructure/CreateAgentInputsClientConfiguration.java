package com.silenteight.payments.bridge.datasource.agent.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CreateAgentInputsClientProperties.class)
class CreateAgentInputsClientConfiguration {

  @Valid
  private final CreateAgentInputsClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel categoriesDataChannel;

  @Bean
  CreateAgentInputsClient createAgentInputsClient() {
    var stub = AgentInputServiceGrpc
        .newStub(categoriesDataChannel)
        .withWaitForReady();

    return new CreateAgentInputsClient(stub, properties.getTimeout());
  }
}
