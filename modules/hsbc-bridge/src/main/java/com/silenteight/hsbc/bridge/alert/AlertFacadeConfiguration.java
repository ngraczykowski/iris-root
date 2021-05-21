package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.report.AlertFinder;
import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertFacadeConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertPayloadRepository alertPayloadRepository;

  @Bean
  AlertFacade alertFacade() {
    return AlertFacade.builder()
        .alertPayloadConverter(alertPayloadConverter)
        .repository(alertRepository)
        .build();
  }

  @Bean
  AlertFinder alertFinder() {
    return new AlertInfoFinder(alertRepository);
  }

  @Bean
  DataCleaner alertDataCleaner() {
    return new AlertDataCleaner(alertPayloadRepository);
  }
}
