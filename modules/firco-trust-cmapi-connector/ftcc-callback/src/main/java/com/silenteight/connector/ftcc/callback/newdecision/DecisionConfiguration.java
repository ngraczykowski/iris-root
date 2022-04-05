package com.silenteight.connector.ftcc.callback.newdecision;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DecisionConfigurationProperties.class)
class DecisionConfiguration {

  @Bean
  DecisionConfigurationHandlerBeanFactory decisionConfigurationHandlerBeanFactory(
      DecisionConfigurationProperties decisionConfigurationProperties) {
    return new DecisionConfigurationHandlerBeanFactory(
        decisionConfigurationProperties.resourceLocation);
  }

  @Bean
  DecisionMapperUseCase decisionMapperUseCase(DecisionConfigurationHolder configurationHolder) {
    return new DecisionMapperUseCaseService(configurationHolder);
  }
}
