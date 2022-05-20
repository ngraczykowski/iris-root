package com.silenteight.warehouse.test.client.listener.sim;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.test.client.listener.IndexedEventIntegrationProperties;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(IndexedEventIntegrationProperties.class)
class IndexedSimListenerConfiguration {

  public static final String ALERT_SIM_INDEXED_INBOUND_CHANNEL =
      "alertSimIndexedInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;
  @NonNull
  private final IndexedEventIntegrationProperties testProperties;

  @Bean
  Queue simulationIndexedQueue() {
    return QueueBuilder
        .durable(testProperties.getSimulationIndexedEventTestListenerInbound().getQueueName())
        .build();
  }

  @Bean
  IndexedSimEventListener indexedSimEventListener() {
    return new IndexedSimEventListener();
  }

  @Bean
  IndexedSimEventListenerIntegrationFlowAdapter indexedSimEventListenerIntegrationFlowAdapter(
      IndexedSimEventListener indexedSimEventListener) {
    return new IndexedSimEventListenerIntegrationFlowAdapter(indexedSimEventListener);
  }

  @Bean
  IntegrationFlow simulationAlertIndexedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_SIM_INDEXED_INBOUND_CHANNEL,
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
