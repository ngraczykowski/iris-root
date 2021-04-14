package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class MatchFacadeConfiguration {

  private final ApplicationEventPublisher eventPublisher;
  private final MatchRepository matchRepository;

  @Bean
  MatchFacade matchFacade() {
    return MatchFacade.builder()
        .matchPayloadConverter(new MatchPayloadConverter())
        .matchRepository(matchRepository)
        .eventPublisher(eventPublisher)
        .build();
  }

  @Bean
  DataCleaner matchDataCleaner() {
    return new MatchDataCleaner(matchRepository);
  }
}
