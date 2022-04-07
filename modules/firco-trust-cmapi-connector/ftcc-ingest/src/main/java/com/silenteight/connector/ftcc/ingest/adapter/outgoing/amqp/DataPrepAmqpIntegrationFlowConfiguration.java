package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_OUTBOUND_CHANNEL;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DataPrepProperties.class)
class DataPrepAmqpIntegrationFlowConfiguration {

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow dataPrepOutboundAmqpIntegrationFlow(@Valid DataPrepProperties properties) {
    return from(DATA_PREP_OUTBOUND_CHANNEL)
        .handle(createOutboundAdapter(properties.outboundRoutingKey()))
        .get();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(String routingKey) {
    return outboundFactory
        .outboundAdapter()
        .exchangeName(CONNECTOR_COMMAND_EXCHANGE)
        .routingKey(routingKey);
  }
}
