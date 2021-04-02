package com.silenteight.warehouse.indexer.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.indexer.IndexerIntegrationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@RequiredArgsConstructor
class IndexerListenerConfiguration {

  public static final String ALERT_INDEXING_INBOUND_CHANNEL =
      "alertIndexingInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;
  @NonNull
  private final IndexerIntegrationProperties properties;

  @Bean
  IntegrationFlow alertIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        ALERT_INDEXING_INBOUND_CHANNEL,
        properties.getAlertIndexingInbound().getQueueName());
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
  IndexRequestCommandIntegrationFlowAdapter indexRequestCommandIntegrationFlowAdapter(
      IndexRequestCommandHandler indexRequestCommand) {
    return new IndexRequestCommandIntegrationFlowAdapter(indexRequestCommand);
  }
}
