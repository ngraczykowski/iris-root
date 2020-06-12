package com.silenteight.sens.webapp.backend.changerequest.cancel;

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

import static com.silenteight.sens.webapp.backend.changerequest.cancel.ChangeRequestCancellationIntegrationChannels.CANCEL_CHANGE_REQUEST_INBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.cancel.ChangeRequestCancellationIntegrationChannels.CANCEL_CHANGE_REQUEST_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ChangeRequestMessagingProperties.class)
public class CancelChangeRequestAmpqIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean cancelChangeRequestMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(
        CancelChangeRequestMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(CANCEL_CHANGE_REQUEST_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendCancelChangeRequestIntegrationFlow(
      ChangeRequestMessagingProperties changeRequestMessagingProperties) {
    return flow -> flow
        .channel(CANCEL_CHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(changeRequestMessagingProperties.exchange())
            .routingKey(changeRequestMessagingProperties.routeCancel())
        );
  }

  @Bean
  IntegrationFlow receiveCancelChangeRequestIntegrationFlow(Queue changeRequestCancelQueue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueues(changeRequestCancelQueue)))
        .channel(CANCEL_CHANGE_REQUEST_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  ChangeRequestCancellationIntegrationFlowAdapter cancelChangeRequestIntegrationFlowAdapter(
      CancelChangeRequestMessageHandler cancelChangeRequestMessageHandler) {
    return new ChangeRequestCancellationIntegrationFlowAdapter(cancelChangeRequestMessageHandler);
  }
}
