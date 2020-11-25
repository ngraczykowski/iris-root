package com.silenteight.serp.governance.featurevector;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories
@EntityScan
class FeatureVectorConfiguration {

  private final FeatureVectorRepository repository;

  @Bean
  FeatureVectorFinder featuresVectorFinder() {
    return new FeatureVectorFinder(repository);
  }

  @Bean
  FeatureVectorService featuresVectorService(ApplicationEventPublisher events) {
    return new FeatureVectorService(repository);
  }
}
