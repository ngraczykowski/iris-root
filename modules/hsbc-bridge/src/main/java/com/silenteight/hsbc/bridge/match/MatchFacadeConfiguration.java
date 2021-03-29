package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

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
        .matchRawMapper(new MatchRawMapper())
        .matchRepository(matchRepository)
        .eventPublisher(eventPublisher)
        .build();
  }
}
