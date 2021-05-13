package com.silenteight.warehouse.indexer.indextestclient.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

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
  Queue alertIndexedQueue() {
    return QueueBuilder
        .durable(testProperties.getAlertIndexedEventTestListenerInbound().getQueueName())
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
  IntegrationFlow alertIndexedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_INDEXED_INBOUND_CHANNEL,
        testProperties.getAlertIndexedEventTestListenerInbound().getQueueName());
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
