package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty
    (name = "facade.amqp.multi-queues.enabled", havingValue = "false", matchIfMissing = true)
@TestConfiguration
@EnableConfigurationProperties(AgentFacadeProperties.class)
@RequiredArgsConstructor
public class RabbitBrokerTestConfiguration {

  static final String TEST_FACADE_OUT_QUEUE = "agent.outQueue";

  private final AmqpAdmin amqpAdmin;

  @Bean("outQueue")
  Queue facadeOutQueue() {
    Queue queue = QueueBuilder
        .durable(TEST_FACADE_OUT_QUEUE)
        .build();
    amqpAdmin.declareQueue(queue);
    return queue;
  }

  @Bean
  Binding facadeOutQueueBinding(
      @Qualifier("outQueue") Queue facadeQueue,
      @Qualifier("outExchange") Exchange facadeExchange) {
    Binding binding = BindingBuilder
        .bind(facadeQueue)
        .to(facadeExchange)
        .with("")
        .noargs();
    amqpAdmin.declareBinding(binding);
    return binding;
  }
}
