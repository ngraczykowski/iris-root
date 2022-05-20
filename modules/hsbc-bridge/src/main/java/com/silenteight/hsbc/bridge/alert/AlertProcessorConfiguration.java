package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
class AlertProcessorConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final MatchFacade matchFacade;
  private final CustomDateTimeFormatter dateTimeFormatter;
  private final EntityManager entityManager;

  @Bean
  AlertProcessor alertProcessor() {
    return new AlertProcessor(
        alertPayloadConverter, alertRepository, relationshipProcessor(), matchFacade,
        alertTimeCalculator());
  }

  @Bean
  AlertTimeCalculator alertTimeCalculator() {
    return new AlertTimeCalculator(dateTimeFormatter.getDateTimeFormatter());
  }

  @Bean
  AlertReProcessor alertReProcessor() {
    return new AlertReProcessor(alertRepository, entityManager);
  }

  private RelationshipProcessor relationshipProcessor() {
    return new RelationshipProcessor();
  }
}
