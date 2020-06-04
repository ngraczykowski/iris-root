package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.changerequest.message.ChangeRequestMessagingProperties;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.sens.webapp.backend.changerequest.create.ChangeRequestCreationIntegrationChannels.CREATED_CHANGE_REQUEST_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.create.ChangeRequestCreationIntegrationChannels.CREATE_CHANGE_REQUEST_INBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.create.ChangeRequestCreationIntegrationChannels.CREATE_CHANGE_REQUEST_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ChangeRequestMessagingProperties.class)
class ChangeRequestCreationAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean createChangeRequestMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(
        CreateChangeRequestMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(CREATE_CHANGE_REQUEST_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendCreateChangeRequestIntegrationFlow(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return flow -> flow
        .channel(CREATE_CHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(changeRequestMessagingProperties.exchange())
            .routingKey(changeRequestMessagingProperties.routeCreate()));
  }

  @Bean
  IntegrationFlow receiveCreateChangeRequestIntegrationFlow(Queue changeRequestCreateQueue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueues(changeRequestCreateQueue)))
        .channel(CREATE_CHANGE_REQUEST_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  ChangeRequestCreationIntegrationFlowAdapter createBulkChangeIntegrationFlowAdapter(
      CreateChangeRequestMessageHandler createChangeRequestMessageHandler) {
    return new ChangeRequestCreationIntegrationFlowAdapter(createChangeRequestMessageHandler);
  }

  @Bean
  IntegrationFlow createdChangeRequestEmptyIntegrationFlow() {
    return flow -> flow
        .channel(CREATED_CHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(message -> { });
  }
}
