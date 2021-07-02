package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class MatchEventConfiguration {

  @Bean
  MatchUpdater matchUpdater(MatchRepository matchRepository) {
    return new MatchUpdater(matchRepository);
  }

  @Bean
  MatchEventHandler matchEventHandler(MatchUpdater matchUpdater) {
    return new MatchEventHandler(matchUpdater);
  }
}
