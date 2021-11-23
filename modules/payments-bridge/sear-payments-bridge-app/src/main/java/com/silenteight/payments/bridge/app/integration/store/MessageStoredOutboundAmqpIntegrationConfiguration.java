package com.silenteight.payments.bridge.app.integration.store;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(MessageStoredOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class MessageStoredOutboundAmqpIntegrationConfiguration {

  static final String MESSAGE_STORED_OUTBOUND = "messageStoredOutboundChannel";

  @Valid
  private final MessageStoredOutboundAmqpIntegrationProperties properties;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow messageStoredOutboundFlow() {
    return createOutboundFlow(
        MESSAGE_STORED_OUTBOUND,
        properties.getOutboundExchangeName(),
        properties.getAlertStoredRoutingKey());
  }

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

  @Bean(MESSAGE_STORED_OUTBOUND)
  MessageChannel messageStoredOutbound() {
    return new DirectChannel();
  }
}
