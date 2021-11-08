package com.silenteight.simulator.retention.dataset.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.simulator.retention.RetentionProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RetentionProperties.class)
class DatasetExpiredListenerConfiguration {

  private static final String DATASETS_EXPIRED_INBOUND_CHANNEL = "datasetsExpiredInboundChannel";
  private static final String ANALYSIS_EXPIRED_OUTBOUND_CHANNEL = "analysisExpiredOutboundChannel";

  @NonNull
  @Valid
  private final RetentionProperties properties;

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow datasetsExpiredMessagesQueueToChannelIntegrationFlow() {
    return createInputFlow(
        DATASETS_EXPIRED_INBOUND_CHANNEL,
        properties.datasetExpiredInboundQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }

  @Bean
  DatasetExpiredFlowAdapter datasetsExpiredMessagesCommandIntegrationFlowAdapter(
      DatasetsExpiredMessageHandler handler) {

    return new DatasetExpiredFlowAdapter(
        DATASETS_EXPIRED_INBOUND_CHANNEL,
        ANALYSIS_EXPIRED_OUTBOUND_CHANNEL,
        handler);
  }

  @Bean
  IntegrationFlow analysisExpiredOutboundChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        ANALYSIS_EXPIRED_OUTBOUND_CHANNEL,
        properties.simulationExpiredOutboundExchange(),
        properties.simulationExpiredOutboundRoutingKey());
  }

  private IntegrationFlow createOutputFlow(String channel, String exchange, String routingKey) {
    return flow -> flow
        .channel(channel)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(exchange)
            .routingKey(routingKey));
  }
}
