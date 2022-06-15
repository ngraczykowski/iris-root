package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@Conditional(SingleFacadeEnabledCondition.class)
@TestConfiguration
@EnableConfigurationProperties(AgentFacadeProperties.class)
@RequiredArgsConstructor
public class RabbitBrokerTestConfiguration {

  static final String TEST_FACADE_OUT_QUEUE = "agent.outQueue";

  @Bean("outQueue")
  Queue facadeOutQueue() {
    return QueueBuilder
        .durable(TEST_FACADE_OUT_QUEUE)
        .build();
  }

  @Bean
  Binding facadeOutQueueBinding(
      @Qualifier("outQueue") Queue facadeQueue,
      @Qualifier("outExchange") Exchange facadeExchange) {
    return BindingBuilder
        .bind(facadeQueue)
        .to(facadeExchange)
        .with("")
        .noargs();
  }
}
