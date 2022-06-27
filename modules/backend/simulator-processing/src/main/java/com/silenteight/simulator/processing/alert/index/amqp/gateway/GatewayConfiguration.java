package com.silenteight.simulator.processing.alert.index.amqp.gateway;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;
import com.silenteight.simulator.processing.alert.index.amqp.AlertIndexProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertIndexProperties.class)
public class GatewayConfiguration {

  public static final String RECOMMENDATIONS_OUTBOUND_CHANNEL = "recommendationsOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow recommendationsGeneratedOutboundChannelToExchangeIntegrationFlow(
      @Valid AlertIndexProperties properties) {

    return createOutputFlow(
        RECOMMENDATIONS_OUTBOUND_CHANNEL,
        properties.recommendationsOutboundExchange(),
        properties.recommendationsOutboundRoutingKey());
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
  GatewayProxyFactoryBean createSimulationDataIndexRequestGateway() {
    GatewayProxyFactoryBean factoryBean =
        new GatewayProxyFactoryBean(SimulationDataIndexRequestGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(RECOMMENDATIONS_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
