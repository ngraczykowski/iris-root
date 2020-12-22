package com.silenteight.serp.governance.bulkchange.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.*;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BulkChangeAmqpIntegrationProperties.class)
class BulkChangeAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final BulkChangeAmqpIntegrationProperties properties;

  @Bean
  IntegrationFlow receiveCreateBulkChangeFlow() {
    var queue = properties.getCreate().getInboundQueueName();

    return createInputFlow(CREATE_BULK_CHANGE_INBOUND_CHANNEL, queue);
  }

  @Bean
  IntegrationFlow sendCreateBulkChangeOutputFlow() {
    var exchange = properties.getCreate().getOutboundExchangeName();
    var routingKey = properties.getCreate().getOutboundRoutingKey();

    return createOutputFlow(CREATE_BULK_CHANGE_OUTBOUND_CHANNEL, exchange, routingKey);
  }

  @Bean
  IntegrationFlow receiveApplyBulkChangeFlow() {
    var queue = properties.getApply().getInboundQueueName();

    return createInputFlow(APPLY_BULK_CHANGE_INBOUND_CHANNEL, queue);
  }

  @Bean
  IntegrationFlow sendApplyBulkChangeOutputFlow() {
    var exchange = properties.getApply().getOutboundExchangeName();
    var routingKey = properties.getApply().getOutboundRoutingKey();

    return createOutputFlow(APPLY_BULK_CHANGE_OUTBOUND_CHANNEL, exchange, routingKey);
  }

  @Bean
  IntegrationFlow receiveRejectBulkChangeFlow() {
    var queue = properties.getReject().getInboundQueueName();

    return createInputFlow(REJECT_BULK_CHANGE_INBOUND_CHANNEL, queue);
  }

  @Bean
  IntegrationFlow sendRejectBulkChangeOutputFlow() {
    var exchange = properties.getReject().getOutboundExchangeName();
    var routingKey = properties.getReject().getOutboundRoutingKey();

    return createOutputFlow(REJECT_BULK_CHANGE_OUTBOUND_CHANNEL, exchange, routingKey);
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
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
