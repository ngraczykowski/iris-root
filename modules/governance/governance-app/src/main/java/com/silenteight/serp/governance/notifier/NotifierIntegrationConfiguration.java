package com.silenteight.serp.governance.notifier;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.SimpleMessageListenerContainerSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(NotifierIntegrationProperties.class)
public class NotifierIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final NotifierIntegrationProperties properties;
  private final ReasoningBranchesDisabledHandler reasoningBranchesDisabledHandler;

  @Bean
  IntegrationFlow receiveReasoningBranchesDisabledFlow() {
    return IntegrationFlows
        .from(inboundFactory.simpleAdapter().configureContainer(this::configureContainer))
        .handle(
            ReasoningBranchesDisabledEvent.class,
            (p, h) -> reasoningBranchesDisabledHandler.notifyReasoningBranchesDisabled(p))
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(properties.getOutboundExchangeName())
            .routingKey(properties.getOutboundRoutingKey()))
        .get();
  }

  private void configureContainer(SimpleMessageListenerContainerSpec c) {
    c.addQueueNames(properties.getInboundQueueName());
  }
}
