package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.feature.DummyFeature;
import com.silenteight.fab.dataprep.domain.feature.Feature;
import com.silenteight.fab.dataprep.domain.feature.GenderFeature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureConfiguration {

  @Bean
  @ConditionalOnProperty("feeding.features.dummy-feature.enabled")
  Feature dummyFeature() {
    return new DummyFeature();
  }

  @Bean
  @ConditionalOnProperty("feeding.features.gender-feature.enabled")
  Feature genderFeature(AgentInputServiceClient agentInputServiceClient) {
    return new GenderFeature(agentInputServiceClient);
  }
}
