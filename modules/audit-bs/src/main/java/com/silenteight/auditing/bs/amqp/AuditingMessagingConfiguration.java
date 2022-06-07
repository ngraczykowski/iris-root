package com.silenteight.auditing.bs.amqp;

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

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuditingAmqpProperties.class)
class AuditingMessagingConfiguration {

  private static final String AUDIT_DATA_OUTBOUND_CHANNEL = "auditDataOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean(AuditDataMessageGateway.ID)
  GatewayProxyFactoryBean auditDataMessageGateway(MessageChannel auditDataOutboundChannel) {
    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(AuditDataMessageGateway.class);
    result.setDefaultRequestChannel(auditDataOutboundChannel);
    result.setDefaultRequestChannelName(AUDIT_DATA_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  DirectChannel auditDataOutboundChannel() {
    return new DirectChannel();
  }

  @Bean
  IntegrationFlow auditDataChannelToExchangeIntegrationFlow(
      @Valid AuditingAmqpProperties properties) {

    return createOutputFlow(
        AUDIT_DATA_OUTBOUND_CHANNEL,
        properties.outboundExchange(),
        properties.outboundRoutingKey());
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
