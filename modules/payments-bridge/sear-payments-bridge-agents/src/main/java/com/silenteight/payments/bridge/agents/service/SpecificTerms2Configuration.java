package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SpecificTerms2Properties.class)
class SpecificTerms2Configuration {

  private final SpecificTerms2Properties properties;

  @Bean
  SpecificTerms2Agent specificTerms2Agent() {
    return new SpecificTerms2Agent(properties.buildAgentMappings());
  }
}
