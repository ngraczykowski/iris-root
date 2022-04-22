package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.AlertMessage;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import javax.validation.Valid;

import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_GATEWAY_CHANNEL;
import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_OUTBOUND_CHANNEL;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE;
import static java.lang.Boolean.TRUE;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DataPrepProperties.class)
public class DataPrepAmqpIntegrationFlowConfiguration {

  @NonNull
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  GatewayProxyFactoryBean dataPrepMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(DataPrepMessageGateway.class);
    factoryBean.setDefaultRequestChannelName(DATA_PREP_GATEWAY_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow dataPrepGatewayAmqpIntegrationFlow() {
    return from(DATA_PREP_GATEWAY_CHANNEL)
        .enrichHeaders(enricher -> enricher
            .priorityFunction(DataPrepAmqpIntegrationFlowConfiguration::getPriority, TRUE))
        .handle(AlertMessage.class, DataPrepAmqpIntegrationFlowConfiguration::logMessage)
        .transform(AlertMessage::toAlertMessageStored)
        .channel(DATA_PREP_OUTBOUND_CHANNEL)
        .get();
  }

  private static AlertMessage logMessage(AlertMessage message, MessageHeaders headers) {
    log.debug(
        "Sending alert message: batchName={}, messageName={}, state={}", message.getBatchName(),
        message.getMessageName(), message.getState());
    return message;
  }

  private static Integer getPriority(Message<AlertMessage> message) {
    return message.getPayload().getPriority();
  }

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
