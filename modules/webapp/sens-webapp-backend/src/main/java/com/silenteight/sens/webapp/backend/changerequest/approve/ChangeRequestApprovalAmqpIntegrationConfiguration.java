package com.silenteight.sens.webapp.backend.changerequest.approve;

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

import static com.silenteight.sens.webapp.backend.changerequest.approve.ChangeRequestApprovalIntegrationChannels.APPROVE_CHANGE_REQUEST_INBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.approve.ChangeRequestApprovalIntegrationChannels.APPROVE_CHANGE_REQUEST_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Configuration
class ChangeRequestApprovalAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;

  @Bean
  public GatewayProxyFactoryBean approveChangeRequestMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(
        ApproveChangeRequestMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(APPROVE_CHANGE_REQUEST_OUTBOUND_CHANNEL);
    return factoryBean;
  }

  @Bean
  IntegrationFlow sendApproveChangeRequestIntegrationFlow(
      @Value("${messaging.exchange.change-request}") String changeRequestExchangeName,
      @Value("${messaging.route.change-request.approve}") String routeChangeRequestApprove) {
    return flow -> flow
        .channel(APPROVE_CHANGE_REQUEST_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(changeRequestExchangeName)
            .routingKey(routeChangeRequestApprove)
        );
  }

  @Bean
  IntegrationFlow receiveApproveChangeRequestIntegrationFlow(Queue changeRequestApproveQueue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueues(changeRequestApproveQueue)))
        .channel(APPROVE_CHANGE_REQUEST_INBOUND_CHANNEL)
        .get();
  }

  @Bean
  ChangeRequestApprovalIntegrationFlowAdapter approveChangeRequestIntegrationFlowAdapter(
      ApproveChangeRequestMessageHandler approveChangeRequestMessageHandler) {
    return new ChangeRequestApprovalIntegrationFlowAdapter(approveChangeRequestMessageHandler);
  }
}
