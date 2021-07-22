package com.silenteight.searpayments.bridge.recommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageHeaders;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@RequiredArgsConstructor
class RecommendationInboundAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow agentResponseIntegrationFlow() {
    return from(createInboundAdapter("bridge.recommendations"))
        .log(Level.INFO)
        .handle(this::testHandler)
        .get();
  }

  private int testHandler(Object recommendation, MessageHeaders mh) {
    return 0;
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
