package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertFacadeConfiguration {

  private final AlertRepository alertRepository;
  private final AlertPayloadConverter alertPayloadConverter;
  private final AlertPayloadRepository alertPayloadRepository;
  private final WarehouseApi warehouseApi;
  private final AgentApi agentApi;

  @Bean
  AlertFacade alertFacade() {
    return AlertFacade.builder()
        .alertPayloadConverter(alertPayloadConverter)
        .repository(alertRepository)
        .build();
  }

  @Bean
  DataCleaner alertDataCleaner() {
    return new AlertDataCleaner(alertPayloadRepository);
  }

  @Bean
  AlertSender alertSender() {
    return new AlertSender(warehouseApi, agentApi, alertGetter());
  }

  private AlertGetter alertGetter() {
    return new AlertGetter(alertMapper(), alertRepository);
  }

  private AlertMapper alertMapper() {
    return new AlertMapper(alertPayloadConverter);
  }
}
