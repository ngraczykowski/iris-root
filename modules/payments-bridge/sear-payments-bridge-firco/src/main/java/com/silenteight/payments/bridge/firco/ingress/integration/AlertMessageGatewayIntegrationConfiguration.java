package com.silenteight.payments.bridge.firco.ingress.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.ingress.AcceptAlertGateway;
import com.silenteight.payments.bridge.firco.ingress.DelayedRejectAlertGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Configuration
class AlertMessageGatewayIntegrationConfiguration {

  @Bean(IngressChannels.ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL)
  DirectChannel alertMessageGatewayChannel() {
    return new DirectChannel();
  }

  @Bean(IngressChannels.DELAYED_REJECT_ALERT_COMMAND_GATEWAY_CHANNEL)
  DirectChannel delayedRejectAlertCommandGatewayChannel() {
    return new DirectChannel();
  }

  @Bean
  GatewayProxyFactoryBean acceptAlertGateway() {
    return createGatewayProxyFactory(AcceptAlertGateway.class,
        IngressChannels.ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL);
  }

  @Bean
  GatewayProxyFactoryBean delayedRejectAlertGateway() {
    return createGatewayProxyFactory(
        DelayedRejectAlertGateway.class,
        IngressChannels.DELAYED_REJECT_ALERT_COMMAND_GATEWAY_CHANNEL);
  }

  @Nonnull
  private static GatewayProxyFactoryBean createGatewayProxyFactory(
      Class<?> gatewayClass, String gatewayChannel) {

    var factoryBean = new GatewayProxyFactoryBean(gatewayClass);
    factoryBean.setDefaultRequestChannelName(gatewayChannel);
    return factoryBean;
  }
}
