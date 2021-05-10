package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.internal.model.CaseInformation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

  private RelationshipProcessor relationshipProcessor() {
    return new RelationshipProcessor();
  }
}
