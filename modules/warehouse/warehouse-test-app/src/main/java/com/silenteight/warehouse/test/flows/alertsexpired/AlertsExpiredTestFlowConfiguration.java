package com.silenteight.warehouse.test.flows.alertsexpired;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.AlertsExpiredClientGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AlertsExpiredTestFlowConfiguration {

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.alerts-expired.enabled", havingValue = "true")
  AlertsExpiredClient alertsExpiredClient(
      AlertsExpiredClientGateway alertsExpiredClientGateway) {

    log.info("AlertsExpiredClient created");
    return new AlertsExpiredClient(alertsExpiredClientGateway, new MessageGenerator());
  }
}
