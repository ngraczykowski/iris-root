package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.ObjectConverter;
import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
class MatchFacadeConfiguration {

  private final ApplicationEventPublisher eventPublisher;
  private final ObjectConverter objectConverter;
  private final MatchRepository matchRepository;
  private final MatchPayloadRepository matchPayloadRepository;

  @Bean
  MatchFacade matchFacade(EntityManager entityManager, MatchDataMapper matchDataMapper) {
    return MatchFacade.builder()
        .objectConverter(objectConverter)
        .matchRepository(matchRepository)
        .eventPublisher(eventPublisher)
        .matchDataMapper(matchDataMapper)
        .entityManager(entityManager)
        .build();
  }

  @Bean
  DataCleaner matchDataCleaner() {
    return new MatchDataCleaner(matchPayloadRepository);
  }
}
