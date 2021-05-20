package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.report.AlertFinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertFacadeConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;

  @Bean
  AlertFacade alertFacade() {
    return AlertFacade.builder()
        .alertPayloadConverter(alertPayloadConverter)
        .relationshipProcessor(relationshipProcessor())
        .repository(alertRepository)
        .build();
  }

  @Bean
  AlertFinder alertFinder() {
    return new AlertInfoFinder(alertRepository);
  }

  private RelationshipProcessor relationshipProcessor() {
    return new RelationshipProcessor();
  }
}
