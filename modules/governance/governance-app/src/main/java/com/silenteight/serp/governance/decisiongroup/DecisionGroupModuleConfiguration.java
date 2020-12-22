package com.silenteight.serp.governance.decisiongroup;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@RequiredArgsConstructor
@EntityScan
@EnableJpaRepositories
@Configuration
class DecisionGroupModuleConfiguration {

  private final DecisionGroupRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  DecisionGroupService decisionGroupService(DecisionGroupCache decisionGroupCache) {
    return new DecisionGroupService(repository, decisionGroupCache, eventPublisher);
  }

  @Bean
  DecisionGroupFinder decisionGroupFinder() {
    return new DecisionGroupFinder(repository);
  }

  @Bean
  DecisionGroupCache decisionGroupCache() {
    return new DecisionGroupCache(repository);
  }
}
