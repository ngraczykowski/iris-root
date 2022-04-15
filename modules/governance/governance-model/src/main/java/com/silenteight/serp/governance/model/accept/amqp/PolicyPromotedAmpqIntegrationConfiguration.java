package com.silenteight.serp.governance.model.accept.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(PolicyPromotedMessagingProperties.class)
class PolicyPromotedAmpqIntegrationConfiguration {

  private static final String MODEL_PROMOTED_OUTBOUND_CHANNEL = "modelPromotedOutboundChannel";

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  GatewayProxyFactoryBean policyPromotedMessageGateway() {
    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(
        ModelPromotedMessageGateway.class);
    result.setDefaultRequestChannel(new DirectChannel());
    result.setDefaultRequestChannelName(MODEL_PROMOTED_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  IntegrationFlow sendModelPublishMessageIntegrationFlow(
      PolicyPromotedMessagingProperties properties) {

    return flow -> flow
        .channel(MODEL_PROMOTED_OUTBOUND_CHANNEL)
        .handle(outboundFactory
              .outboundAdapter()
              .exchangeName(properties.getExchange())
              .routingKey(properties.getRoutingKey())
        );
  }
}
