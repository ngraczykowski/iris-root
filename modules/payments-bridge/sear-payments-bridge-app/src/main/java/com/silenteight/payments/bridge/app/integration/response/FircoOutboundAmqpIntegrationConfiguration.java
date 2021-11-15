package com.silenteight.payments.bridge.app.integration.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.integration.ChannelFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(FircoOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoOutboundAmqpIntegrationConfiguration {


  static final String RESPONSE_COMPLETED_OUTBOUND = "responseCompletedOutboundChannel";

  @Valid
  private final FircoOutboundAmqpIntegrationProperties properties;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow responseCompletedOutboundFlow() {
    return createOutboundFlow(
        RESPONSE_COMPLETED_OUTBOUND,
        properties.getOutboundExchangeName(),
        properties.getResponseCompletedRoutingKey());
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

  @Bean(RESPONSE_COMPLETED_OUTBOUND)
  MessageChannel responseCompletedOutbound() {
    return ChannelFactory.createDirectChannel();
  }
}
