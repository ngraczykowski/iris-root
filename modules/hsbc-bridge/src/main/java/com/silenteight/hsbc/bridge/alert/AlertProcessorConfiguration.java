package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertProcessorConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final MatchFacade matchFacade;

  @Bean
  AlertProcessor alertProcessor() {
    return new AlertProcessor(
        alertPayloadConverter, alertRepository, relationshipProcessor(), matchFacade);
  }

  private RelationshipProcessor relationshipProcessor() {
    return new RelationshipProcessor();
  }
}
