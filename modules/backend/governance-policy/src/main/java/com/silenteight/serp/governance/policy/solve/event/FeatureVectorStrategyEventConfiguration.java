package com.silenteight.serp.governance.policy.solve.event;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(FeatureVectorEventStrategyProperties.class)
class FeatureVectorStrategyEventConfiguration {

  @Bean
  FeatureVectorEventStrategyService featureVectorEventStrategyService(
      @Valid FeatureVectorEventStrategyProperties properties) {

    return new FeatureVectorEventStrategyService(
        properties.getStrategy());
  }
}
