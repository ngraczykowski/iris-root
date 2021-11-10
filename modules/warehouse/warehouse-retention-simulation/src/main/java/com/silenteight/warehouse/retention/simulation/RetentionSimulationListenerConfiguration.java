package com.silenteight.warehouse.retention.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({
    RetentionSimulationIntegrationProperties.class,
    RetentionSimulationProperties.class })
class RetentionSimulationListenerConfiguration {

  private static final String RETENTION_PII_INBOUND_CHANNEL =
      "retentionAnalysisExpiredInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  @Valid
  private final RetentionSimulationIntegrationProperties integrationProperties;

  @Bean
  IntegrationFlow analysisExpiredQueueToChannelIntegrationFlow(
      RetentionSimulationHandler handler) {

    return createInputFlow(
        RETENTION_PII_INBOUND_CHANNEL,
        integrationProperties.getAnalysisExpiredIndexingInbound(),
        handler);
  }

  private IntegrationFlow createInputFlow(
      String channel,
      AmqpInboundProperties properties,
      RetentionSimulationHandler handler) {

    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(properties.getQueueName())))
        .channel(channel)
        .handle(
            AnalysisExpired.class,
            (payload, headers) -> {
              handler.handle(payload);
              return null;
            })
        .get();
  }

  @Bean
  RetentionSimulationHandler retentionSimulationHandler(
      RetentionSimulationUseCase retentionSimulationUseCase) {

    return new RetentionSimulationHandler(retentionSimulationUseCase);
  }
}
