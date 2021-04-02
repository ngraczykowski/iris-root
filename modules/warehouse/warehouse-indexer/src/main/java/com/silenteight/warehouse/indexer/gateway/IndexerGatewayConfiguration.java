package com.silenteight.warehouse.indexer.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.warehouse.indexer.IndexerIntegrationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@Configuration
@RequiredArgsConstructor
class IndexerGatewayConfiguration {

  public static final String ALERT_INDEXED_OUTBOUND_CHANNEL =
      "alertIndexedOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;
  @NonNull
  private final IndexerIntegrationProperties properties;

  @Bean
  IntegrationFlow alertIndexedOutboundChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        ALERT_INDEXED_OUTBOUND_CHANNEL,
        properties.getAlertIndexedOutbound().getExchangeName(),
        properties.getAlertIndexedOutbound().getRoutingKey());
  }

  private IntegrationFlow createOutputFlow(String channel, String exchange, String routingKey) {
    return flow -> flow
        .channel(channel)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(exchange)
            .routingKey(routingKey));
  }

  @Bean
  GatewayProxyFactoryBean indexingConfirmationGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(IndexedConfirmationGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ALERT_INDEXED_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
