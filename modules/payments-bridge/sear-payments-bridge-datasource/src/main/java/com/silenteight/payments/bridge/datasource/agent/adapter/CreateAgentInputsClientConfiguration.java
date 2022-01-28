package com.silenteight.payments.bridge.datasource.agent.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

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
        .newBlockingStub(categoriesDataChannel)
        .withWaitForReady();

    return new CreateAgentInputsAdapter(stub, properties.getTimeout());
  }
}
