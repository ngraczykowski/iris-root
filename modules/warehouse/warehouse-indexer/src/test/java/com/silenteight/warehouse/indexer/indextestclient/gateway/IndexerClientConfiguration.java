package com.silenteight.warehouse.indexer.indextestclient.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(IndexerClientIntegrationProperties.class)
public class IndexerClientConfiguration {

  public static final String ALERT_INDEXING_OUTBOUND_CHANNEL =
      "alertIndexingOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;
  @NonNull
  private final IndexerClientIntegrationProperties properties;

  @Bean
  GatewayProxyFactoryBean indexClientGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(IndexClientGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ALERT_INDEXING_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  TopicExchange alertIndexExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getAlertIndexingTestClientOutbound().getExchangeName())
        .build();
  }

  @Bean
  IntegrationFlow alertIndexChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        ALERT_INDEXING_OUTBOUND_CHANNEL,
        properties.getAlertIndexingTestClientOutbound().getExchangeName(),
        properties.getAlertIndexingTestClientOutbound().getRoutingKey());
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
