package com.silenteight.simulator.processing.alert.index.amqp.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.simulator.processing.alert.index.amqp.AlertIndexProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertIndexProperties.class)
public class GatewayConfiguration {

  public static final String RECOMMENDATIONS_OUTBOUND_CHANNEL =
      "recommendationsOutboundChannel";

  public static final String ACK_MESSAGES_OUTBOUND_CHANNEL =
      "ackMessagesOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow recommendationsGeneratedOutboundChannelToExchangeIntegrationFlow(
      @Valid AlertIndexProperties properties) {

    return createOutputFlow(
        RECOMMENDATIONS_OUTBOUND_CHANNEL,
        properties.getRecommendationsOutbound().getExchange(),
        properties.getRecommendationsOutbound().getRoutingKey());
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
