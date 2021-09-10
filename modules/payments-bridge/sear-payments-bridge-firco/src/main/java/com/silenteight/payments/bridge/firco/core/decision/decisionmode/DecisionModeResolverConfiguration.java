package com.silenteight.payments.bridge.firco.core.decision.decisionmode;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DecisionModeResolverProperties.class)
@RequiredArgsConstructor
class DecisionModeResolverConfiguration {

  private final DecisionModeResolverProperties properties;

  @Bean
  CsvPatternDecisionModeResolverFactoryBean patternDecisionModeResolver() {
    return new CsvPatternDecisionModeResolverFactoryBean(properties.getCsvFilePath());
  }
}
