package com.silenteight.warehouse.test.client.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(IndexedEventIntegrationProperties.class)
class IndexedListenerConfiguration {

  public static final String ALERT_INDEXED_INBOUND_CHANNEL =
      "alertIndexedInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;
  @NonNull
  private final IndexedEventIntegrationProperties testProperties;

  @Bean
  Queue productionIndexedQueue() {
    return QueueBuilder
        .durable(testProperties.getProductionIndexedEventTestListenerInbound().getQueueName())
        .build();
  }

  @Bean
  Queue simulationIndexedQueue() {
    return QueueBuilder
        .durable(testProperties.getSimulationIndexedEventTestListenerInbound().getQueueName())
        .build();
  }

  @Bean
  IndexedEventListener indexedEventListener() {
    return new IndexedEventListener();
  }

  @Bean
  IndexedEventListenerIntegrationFlowAdapter indexedEventListenerIntegrationFlowAdapter(
      IndexedEventListener indexedEventListener) {
    return new IndexedEventListenerIntegrationFlowAdapter(indexedEventListener);
  }

  @Bean
  IntegrationFlow simulationAlertIndexedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_INDEXED_INBOUND_CHANNEL,
        testProperties.getProductionIndexedEventTestListenerInbound().getQueueName());
  }

  @Bean
  IntegrationFlow productionAlertIndexedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_INDEXED_INBOUND_CHANNEL,
        testProperties.getSimulationIndexedEventTestListenerInbound().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }
}
