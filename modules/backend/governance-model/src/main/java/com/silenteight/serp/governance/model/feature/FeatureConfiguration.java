package com.silenteight.serp.governance.model.feature;

import com.silenteight.serp.governance.agent.domain.FeaturesProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureConfiguration {

  @Bean
  FeatureSolver featureSolver(FeaturesProvider featuresProvider) {
    return new FeatureSolver(featuresProvider);
  }
}
