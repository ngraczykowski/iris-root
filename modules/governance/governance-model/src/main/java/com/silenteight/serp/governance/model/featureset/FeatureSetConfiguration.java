package com.silenteight.serp.governance.model.featureset;

import com.silenteight.serp.governance.model.ModelProperties;
import com.silenteight.serp.governance.model.agent.AgentsRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties(ModelProperties.class)
class FeatureSetConfiguration {

  @Bean
  FeatureSetRegistry featureSetRegistry(
      ObjectMapper objectMapper,
      ModelProperties modelProperties,
      ResourceLoader resourceLoader,
      AgentsRegistry agentsRegistry) {

    return new FeatureSetRegistry(
        modelProperties.getFeatureSetSource(),
        objectMapper, resourceLoader, agentsRegistry);
  }
}
