package com.silenteight.searpayments.scb.request;

import com.silenteight.tsaas.bridge.app.rest.mapper.CreateAlertsFromRequestFactory;
import com.silenteight.tsaas.bridge.domain.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
@RequiredArgsConstructor
class HandleRequestConfiguration {

  @Value("${tsaas.max-hits-per-alert}")
  private int maxHitsPerAlert;

  @Bean
  PrevalidateAlertStrategy prevalidateAlertStrategy() {
    return new com.silenteight.tsaas.bridge.app.request.MaxHitsPrevalidateAlertStrategy(maxHitsPerAlert);
  }

  @Bean
  HandleRequestFacade handleRequestFacade(
      CreateAlertsFromRequestFactory createAlertsFromRequestFactory,
      com.silenteight.tsaas.bridge.app.request.ProcessAlerts handleRequestProcessAlerts,
      PublishAlerts publishAlerts,
      AlertService alertService) {
    return new HandleRequestFacade(
        createAlertsFromRequestFactory,
        handleRequestProcessAlerts,
        publishAlerts,
        alertService);
  }

  @Bean
  com.silenteight.tsaas.bridge.app.request.ProcessAlerts handleRequestProcessAlerts(
      AlertService alertService,
      PrevalidateAlertStrategy prevalidateAlertStrategy) {
    return new com.silenteight.tsaas.bridge.app.request.ProcessAlerts(alertService, prevalidateAlertStrategy);
  }

  @Bean
  PublishAlerts publishAlerts(MessageChannel alertStoredChannel) {
    return new PublishAlerts(alertStoredChannel);
  }
}
