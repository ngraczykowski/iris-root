package com.silenteight.searpayments.scb.request;

import com.silenteight.searpayments.scb.mapper.CreateAlertsFromRequestFactory;
import com.silenteight.searpayments.scb.domain.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@Configuration
@RequiredArgsConstructor
class HandleRequestConfiguration {

  @Value("${tsaas.max-hits-per-alert:10}")
  private int maxHitsPerAlert;

  @Bean
  PrevalidateAlertStrategy prevalidateAlertStrategy() {
    return new MaxHitsPrevalidateAlertStrategy(maxHitsPerAlert);
  }

  @Bean
  HandleRequestFacade handleRequestFacade(
      CreateAlertsFromRequestFactory createAlertsFromRequestFactory,
      ProcessAlerts handleRequestProcessAlerts,
      PublishAlerts publishAlerts,
      AlertService alertService) {
    return new HandleRequestFacade(
        createAlertsFromRequestFactory,
        handleRequestProcessAlerts,
        publishAlerts,
        alertService);
  }

  @Bean
  ProcessAlerts handleRequestProcessAlerts(
      AlertService alertService,
      PrevalidateAlertStrategy prevalidateAlertStrategy) {
    return new ProcessAlerts(alertService, prevalidateAlertStrategy);
  }

  @Bean
  PublishAlerts publishAlerts(MessageChannel alertStoredChannel) {
    return new PublishAlerts(alertStoredChannel);
  }
}
