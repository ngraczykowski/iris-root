package com.silenteight.serp.governance.vector.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class FeatureVectorConfiguration {

  @Bean
  FeatureVectorService featureVectorService(
      FeatureVectorRepository analyticsFeatureVectorRepository) {

    return new FeatureVectorService(analyticsFeatureVectorRepository);
  }

  @Bean
  FeatureVectorQuery featureVectorQuery(
      FeatureVectorRepository analyticsFeatureVectorRepository) {

    return new FeatureVectorQuery(analyticsFeatureVectorRepository);
  }
}
