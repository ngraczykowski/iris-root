package com.silenteight.serp.governance.qa.retention.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AlertsExpiredIntegrationProperties.class)
class RetentionAlertListenerConfiguration {

  private static final String QA_RETENTION_ALERT_INBOUND_CHANNEL =
      "qaRetentionAlertInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow qaRetentionAlertQueueToChannelIntegrationFlow(
      @Valid AlertsExpiredIntegrationProperties properties,
      AlertsExpiredCommandHandler alertsExpiredCommandHandler) {

    return createInputFlow(QA_RETENTION_ALERT_INBOUND_CHANNEL, properties.getReceive(),
        alertsExpiredCommandHandler);
  }

  private IntegrationFlow createInputFlow(String channel,
      AmqpInboundProperties properties, AlertsExpiredCommandHandler handler) {

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
  AlertsExpiredCommandHandler alertsExpiredCommandHandler(EraseAlertUseCase eraseAlertUseCase) {
    return new AlertsExpiredCommandHandler(eraseAlertUseCase);
  }
}
