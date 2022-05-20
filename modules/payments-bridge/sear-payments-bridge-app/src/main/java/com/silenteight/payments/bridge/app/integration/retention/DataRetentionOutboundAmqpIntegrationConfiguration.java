package com.silenteight.payments.bridge.app.integration.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(DataRetentionOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class DataRetentionOutboundAmqpIntegrationConfiguration {

  static final String PERSONAL_INFORMATION_EXPIRED_OUTBOUND =
      "personalInformationExpiredOutboundChannel";
  static final String ALERT_EXPIRED_OUTBOUND = "alertExpiredOutboundChannel";

  @Valid
  private final DataRetentionOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow personalInformationExpiredOutboundFlow() {
    return createOutboundFlow(
        PERSONAL_INFORMATION_EXPIRED_OUTBOUND,
        properties.getPersonalInformationCommand().getOutboundExchangeName(),
        properties.getPersonalInformationCommand().getRoutingKey());
  }

  @Bean
  IntegrationFlow alertExpiredOutboundFlow() {
    return createOutboundFlow(
        ALERT_EXPIRED_OUTBOUND,
        properties.getAlertDataCommand().getOutboundExchangeName(),
        properties.getAlertDataCommand().getRoutingKey());
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

}
