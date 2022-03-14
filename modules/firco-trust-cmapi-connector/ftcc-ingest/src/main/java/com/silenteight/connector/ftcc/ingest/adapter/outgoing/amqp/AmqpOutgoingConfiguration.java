package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DataPrepProperties.class)
public class AmqpOutgoingConfiguration {

  public static final String DATA_PREP_OUTBOUND_CHANNEL = "dataPrepOutboundChannel";

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow dataprepOutboundChannelToExchangeIntegrationFlow(
      @Valid DataPrepProperties properties) {

    return createOutputFlow(
        DATA_PREP_OUTBOUND_CHANNEL,
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

  @Bean
  GatewayProxyFactoryBean createDataPrepMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(DataPrepMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(DATA_PREP_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
