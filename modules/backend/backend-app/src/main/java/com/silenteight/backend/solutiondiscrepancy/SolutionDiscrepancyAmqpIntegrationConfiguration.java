/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;

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
@EnableConfigurationProperties(SolutionDiscrepancyAmqpIntegrationProperties.class)
class SolutionDiscrepancyAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AmqpOutboundFactory outboundFactory;
  private final SolutionDiscrepancyAmqpIntegrationProperties properties;

  @Bean
  IntegrationFlow receiveSolutionDiscrepancyFlow() {
    return IntegrationFlows
        .from(inboundFactory.simpleAdapter().configureContainer(this::configureContainer))
        .channel(SolutionDiscrepancyIntegrationFlowAdapter.SOLUTION_DISCREPANCY_INBOUND_CHANNEL)
        .get();
  }

  private void configureContainer(SimpleMessageListenerContainerSpec c) {
    c.addQueueNames(properties.getInboundQueueName());
  }

  @Bean
  IntegrationFlow sendSolutionDiscrepancyOutputFlow() {
    return flow -> flow
        .channel(SolutionDiscrepancyIntegrationFlowAdapter.SOLUTION_DISCREPANCY_OUTBOUND_CHANNEL)
        .handle(outboundFactory
            .outboundAdapter()
            .routingKey(properties.getReasoningBranchesDisabledRoutingKey())
            .exchangeName(properties.getOutboundExchangeName())
        );
  }
}
