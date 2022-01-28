package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TestApplicationConfiguration {

  @Bean
  CreateAgentInputsClient createAgentInputsClient() {
    return new CreateAgentInputsClientMock();
  }
}
