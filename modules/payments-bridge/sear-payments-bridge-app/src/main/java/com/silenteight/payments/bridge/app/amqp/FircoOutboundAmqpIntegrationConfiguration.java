package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(FircoOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoOutboundAmqpIntegrationConfiguration {

  @Valid
  private final FircoOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  /*
  @Bean
  IntegrationFlow alertMessageGatewayOutboundIntegrationFlow() {
    return createOutboundFlow(
        IngressChannels.ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL,
        properties.getCommand().getOutboundExchangeName(),
        properties.getCommand().getAcceptAlertRoutingKey());
  }

   */

  private StandardIntegrationFlow createOutboundFlow(
      String outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .log(Level.TRACE, getClass().getName() + "." + outboundExchangeName)
        .handle(createOutboundAdapter(outboundExchangeName, outboundRoutingKey))
        .get();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(
      String outboundExchangeName, String outboundRoutingKey) {

    return outboundFactory
        .outboundAdapter()
        .exchangeName(outboundExchangeName)
        .routingKey(outboundRoutingKey);
  }
}
