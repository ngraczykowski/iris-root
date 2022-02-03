package com.silenteight.payments.bridge.agents.service.contextual;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ContextualLearningProperties.class)
class ContextualLearningConfiguration {

  private final ContextualLearningProperties properties;

  @Bean
  AlertedPartyIdContextAdapter alertedPartyIdContextAdapter() {
    return new AlertedPartyIdContextAdapter(
        properties.getNumberOfTokensLeft(),
        properties.getNumberOfTokensRight(),
        properties.getMinTokens(),
        properties.isLineBreaks());
  }

}
