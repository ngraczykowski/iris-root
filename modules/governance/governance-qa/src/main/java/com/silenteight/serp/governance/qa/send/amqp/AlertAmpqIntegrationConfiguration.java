package com.silenteight.serp.governance.qa.send.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AlertMessagingProperties.class)
class AlertAmpqIntegrationConfiguration {

  private static final String QA_ALERT_OUTBOUND_CHANNEL = "qaAlertOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  GatewayProxyFactoryBean qaAlertMessageGateway() {
    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(
        AlertMessageGateway.class);
    result.setDefaultRequestChannel(new DirectChannel());
    result.setDefaultRequestChannelName(QA_ALERT_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  IntegrationFlow sendAlertMessageIntegrationFlow(@Valid AlertMessagingProperties properties) {
    return flow -> flow
        .channel(QA_ALERT_OUTBOUND_CHANNEL)
        .handle(outboundFactory
              .outboundAdapter()
              .exchangeName(properties.getExchange())
              .routingKey(properties.getRoutingKey())
        );
  }
}
