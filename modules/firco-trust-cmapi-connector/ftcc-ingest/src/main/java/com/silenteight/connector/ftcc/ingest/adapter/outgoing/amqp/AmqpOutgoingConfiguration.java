package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.AlertMessage;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.Message;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE;
import static java.lang.Boolean.TRUE;

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
    return createOutputFlow(properties.outboundRoutingKey());
  }

  private IntegrationFlow createOutputFlow(String routingKey) {
    return flow -> flow
        .enrichHeaders(enricher -> enricher
            .priorityFunction(AmqpOutgoingConfiguration::getPriority, TRUE))
        .transform(AlertMessage::toAlertMessageStored)
        .channel(DATA_PREP_OUTBOUND_CHANNEL)
        .handle(creatOutboundAdapter(routingKey));
  }

  private static Integer getPriority(Message<AlertMessage> message) {
    return message.getPayload().getPriority();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> creatOutboundAdapter(String routingKey) {
    return outboundFactory
        .outboundAdapter()
        .exchangeName(CONNECTOR_COMMAND_EXCHANGE)
        .routingKey(routingKey);
  }

  @Bean
  GatewayProxyFactoryBean createDataPrepMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(DataPrepMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(DATA_PREP_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
