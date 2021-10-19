package com.silenteight.serp.governance.model.used.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.MessageChannel;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ModelUsedOnProductionMessagingProperties.class)
class ModelUsedOnProductionMessagingConfiguration {

  private static final String MODEL_USED_OUTBOUND_CHANNEL = "modelUsedOutboundChannel";
  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  DirectChannel modelInUseOutboundChannel() {
    return new DirectChannel();
  }

  @Bean
  GatewayProxyFactoryBean modelUsedMessageGateway(MessageChannel modelInUseOutboundChannel) {
    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(
        ModelUsedOnProductionMessageGateway.class);

    result.setDefaultRequestChannel(modelInUseOutboundChannel);
    result.setDefaultRequestChannelName(MODEL_USED_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  IntegrationFlow modelUsedChannelToExchangeIntegrationFlow(
      @Valid ModelUsedOnProductionMessagingProperties properties) {

    return createOutputFlow(
        MODEL_USED_OUTBOUND_CHANNEL,
        properties.getExchange(),
        properties.getRoutingKey());
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
