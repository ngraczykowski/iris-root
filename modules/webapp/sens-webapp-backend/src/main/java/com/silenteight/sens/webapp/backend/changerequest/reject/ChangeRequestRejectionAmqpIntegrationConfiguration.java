package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.sens.webapp.backend.changerequest.reject.ChangeRequestRejectionIntegrationChannels.REJECT_CHANGE_REQUEST_INBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.reject.ChangeRequestRejectionIntegrationChannels.REJECT_CHANGE_REQUEST_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
class ChangeRequestRejectionAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean rejectChangeRequestMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(
        RejectChangeRequestMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(REJECT_CHANGE_REQUEST_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendRejectChangeRequestIntegrationFlow(
      @Value("${messaging.exchange.change-request}") String changeRequestExchangeName,
      @Value("${messaging.route.change-request.reject}") String routeChangeRequestReject) {
    return flow -> flow
        .channel(REJECT_CHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(changeRequestExchangeName)
            .routingKey(routeChangeRequestReject)
        );
  }

  @Bean
  IntegrationFlow receiveRejectChangeRequestIntegrationFlow(Queue changeRequestRejectQueue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueues(changeRequestRejectQueue)))
        .channel(REJECT_CHANGE_REQUEST_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  ChangeRequestRejectionIntegrationFlowAdapter rejectChangeRequestIntegrationFlowAdapter(
      RejectChangeRequestMessageHandler rejectChangeRequestMessageHandler) {
    return new ChangeRequestRejectionIntegrationFlowAdapter(rejectChangeRequestMessageHandler);
  }
}
