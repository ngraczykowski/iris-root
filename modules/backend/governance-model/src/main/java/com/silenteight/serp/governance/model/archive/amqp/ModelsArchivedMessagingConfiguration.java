package com.silenteight.serp.governance.model.archive.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.MessageChannel;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
class ModelsArchivedMessagingConfiguration {

  private static final String MODELS_ARCHIVED_OUTBOUND_CHANNEL = "modelsArchivedOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  DirectChannel modelsArchivedOutboundChannel() {
    return new DirectChannel();
  }

  @Bean
  GatewayProxyFactoryBean modelsArchivedMessageGateway(
      MessageChannel modelsArchivedOutboundChannel) {

    GatewayProxyFactoryBean result = new GatewayProxyFactoryBean(
        ModelsArchivedMessageGateway.class);
    result.setDefaultRequestChannel(modelsArchivedOutboundChannel);
    result.setDefaultRequestChannelName(MODELS_ARCHIVED_OUTBOUND_CHANNEL);
    return result;
  }

  @Bean
  IntegrationFlow modelsArchivedChannelToExchangeIntegrationFlow(
      @Valid ModelArchivingProperties properties) {

    return createOutputFlow(
        MODELS_ARCHIVED_OUTBOUND_CHANNEL,
        properties.modelArchivingOutboundExchangeName(),
        properties.modelArchivingOutboundRoutingKey());
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
