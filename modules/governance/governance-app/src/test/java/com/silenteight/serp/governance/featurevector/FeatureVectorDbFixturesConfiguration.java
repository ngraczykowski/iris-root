package com.silenteight.serp.governance.featurevector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { FeatureVectorModule.class })
public class FeatureVectorDbFixturesConfiguration {

  @Bean
  FeatureVectorDbFixturesService featureVectorDbFixturesService(
      FeatureVectorRepository featureVectorRepository) {
    return new FeatureVectorDbFixturesService(featureVectorRepository);
  }
}
