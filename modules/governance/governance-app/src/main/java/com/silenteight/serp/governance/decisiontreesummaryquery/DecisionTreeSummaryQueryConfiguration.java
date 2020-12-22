package com.silenteight.serp.governance.decisiontreesummaryquery;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@RequiredArgsConstructor
public class DecisionTreeSummaryQueryConfiguration {

  private final DecisionTreeQueryRepository repository;

  @Bean
  DecisionTreeSummaryFinder decisionTreeSummaryFinder() {
    return new DecisionTreeSummaryFinder(repository);
  }
}
