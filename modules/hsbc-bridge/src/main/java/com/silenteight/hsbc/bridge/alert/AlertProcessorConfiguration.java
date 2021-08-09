package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertProcessorConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final MatchFacade matchFacade;
  private final CustomDateTimeFormatter dateTimeFormatter;

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

  private RelationshipProcessor relationshipProcessor() {
    return new RelationshipProcessor();
  }
}
