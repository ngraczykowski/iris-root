package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class MatchEventConfiguration {

  @Bean
  MatchEventHandler matchEventHandler(MatchRepository matchRepository) {
    return new MatchEventHandler(matchRepository);
  }
}
