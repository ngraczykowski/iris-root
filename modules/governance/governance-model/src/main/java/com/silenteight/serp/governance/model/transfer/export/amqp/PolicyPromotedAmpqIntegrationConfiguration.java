package com.silenteight.serp.governance.model.transfer.export.amqp;

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

  private static final String POLICY_PROMOTED_OUTBOUND_CHANNEL = "policyPromotedOutboundChannel";

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  GatewayProxyFactoryBean policyPromotedMessageGateway() {
    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(
        PolicyPromotedMessageGateway.class);
    result.setDefaultRequestChannel(new DirectChannel());
    result.setDefaultReplyChannelName(POLICY_PROMOTED_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  IntegrationFlow sendPolicyPublishMessageIntegrationFlow(
      PolicyPromotedMessagingProperties properties) {
    return flow -> flow
        .channel(POLICY_PROMOTED_OUTBOUND_CHANNEL)
        .handle(outboundFactory
              .outboundAdapter()
              .exchangeName(properties.getExchange())
              .routingKey(properties.getRoutingKey())
        );
  }
}
