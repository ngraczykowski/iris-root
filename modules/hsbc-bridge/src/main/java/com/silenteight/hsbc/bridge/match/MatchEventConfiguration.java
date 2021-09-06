package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Configuration
class MatchEventConfiguration {

  @Bean
  MatchCategoryUpdater matchUpdater(EntityManager entityManager, MatchRepository matchRepository) {
    return new MatchCategoryUpdater(entityManager, matchRepository);
  }

  @Bean
  MatchEventHandler matchEventHandler(MatchCategoryUpdater matchCategoryUpdater) {
    return new MatchEventHandler(matchCategoryUpdater);
  }
}
