package com.silenteight.simulator.processing.alert.index.amqp.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.simulator.processing.alert.index.amqp.RecommendationsGeneratedProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RecommendationsGeneratedProperties.class)
public class RecommendationsGeneratedGatewayConfiguration {

  public static final String RECOMMENDATIONS_OUTBOUND_CHANNEL =
      "recommendationsOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow recommendationsGeneratedOutboundChannelToExchangeIntegrationFlow(
      @Valid RecommendationsGeneratedProperties properties) {

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
