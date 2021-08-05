package com.silenteight.payments.bridge.firco.ingress.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.ingress.AlertMessageGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
@Configuration
class AlertMessageGatewayIntegrationConfiguration {

  @Bean(IngressChannels.ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL)
  DirectChannel alertMessageGatewayChannel() {
    return new DirectChannel();
  }

  @Bean
  GatewayProxyFactoryBean agentExchangeRequestGateway() {
    var factoryBean = new GatewayProxyFactoryBean(AlertMessageGateway.class);
    factoryBean.setDefaultRequestChannelName(IngressChannels.ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
