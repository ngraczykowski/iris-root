package com.silenteight.connector.ftcc.callback.newdecision;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DecisionConfigurationProperties.class)
public class DecisionConfiguration {

  @Bean
  DecisionConfigurationHandlerBeanFactory decisionConfigurationHandlerBeanFactory(
      DecisionConfigurationProperties decisionConfigurationProperties) {
    return new DecisionConfigurationHandlerBeanFactory(
        decisionConfigurationProperties.resourceLocation);
  }
}
