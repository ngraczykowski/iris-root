package com.silenteight.warehouse.test.client.listener.prod;

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
  IndexedEventListener indexedEventListener() {
    return new IndexedEventListener();
  }

  @Bean
  IndexedEventListenerIntegrationFlowAdapter indexedEventListenerIntegrationFlowAdapter(
      IndexedEventListener indexedEventListener) {
    return new IndexedEventListenerIntegrationFlowAdapter(indexedEventListener);
  }

  @Bean
  IntegrationFlow productionAlertIndexedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_INDEXED_INBOUND_CHANNEL,
        testProperties.getProductionIndexedEventTestListenerInbound().getQueueName());
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
