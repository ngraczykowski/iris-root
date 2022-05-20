package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AgentRequestHandlerProperties.class)
class AgentRequestHandlerConfiguration {

  @Valid
  private final AgentRequestHandlerProperties properties;

  @Bean
  AgentRequestHandler agentExchangeRequestHandler(
      AgentExchangeRequestMessageRepository repository) {

    return new AgentRequestHandler(
        () -> new DefaultFeatureRequestingStrategy(properties.getMaxMessageSize()),
        repository);
  }
}
