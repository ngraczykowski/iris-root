package com.silenteight.serp.governance.decisiontree;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
@RequiredArgsConstructor
class DecisionTreeModuleConfiguration {

  private final DecisionTreeRepository repository;

  @Bean
  GetOrCreateDecisionTreeUseCase getOrCreateDecisionTreeUseCase() {
    return new GetOrCreateDecisionTreeUseCase(repository);
  }

  @Bean
  DecisionTreeFacade decisionTreeFacade(GetOrCreateDecisionTreeUseCase getOrCreateDecisionTree) {
    return new DecisionTreeFacade(
        repository,
        getOrCreateDecisionTree
    );
  }
}
