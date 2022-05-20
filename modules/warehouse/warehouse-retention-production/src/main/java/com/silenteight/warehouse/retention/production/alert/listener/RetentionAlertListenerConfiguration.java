package com.silenteight.warehouse.retention.production.alert.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.common.integration.AmqpInboundProperties;
import com.silenteight.warehouse.retention.production.alert.EraseAlertsUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AlertsIntegrationProperties.class)
class RetentionAlertListenerConfiguration {

  private static final String RETENTION_ALERT_INBOUND_CHANNEL = "retentionAlertInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  @Valid
  private final AlertsIntegrationProperties integrationProperties;

  @Bean
  IntegrationFlow alertsExpiredQueueToChannelIntegrationFlow(AlertsExpiredDataHandler handler) {
    return createInputFlow(
        RETENTION_ALERT_INBOUND_CHANNEL,
        integrationProperties.getAlertsExpiredIndexingInbound(),
        handler);
  }

  private IntegrationFlow createInputFlow(
      String channel,
      AmqpInboundProperties properties,
      AlertsExpiredDataHandler handler) {

    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(properties.getQueueName())))
        .channel(channel)
        .handle(
            AlertsExpired.class,
            (payload, headers) -> {
              handler.handle(payload);
              return null;
            })
        .get();
  }

  @Bean
  AlertsExpiredDataHandler alertsExpiredDataHandler(
      EraseAlertsUseCase eraseAlertsUseCase) {

    return new AlertsExpiredDataHandler(eraseAlertsUseCase);
  }
}
