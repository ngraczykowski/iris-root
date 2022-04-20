package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties

import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class NotifyBatchErrorFlowRabbitMqTestConfig {

  static final String TEST_QUEUE_NAME = "test-notify-batch-error-queue"

  @Autowired
  private AmqpRegistrationOutgoingNotifyBatchErrorProperties properties

  @Bean
  Queue testBatchErrorQueue() {
    return QueueBuilder.durable(TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testBinding(DirectExchange batchErrorExchange, Queue testBatchErrorQueue) {
    return BindingBuilder
        .bind(testBatchErrorQueue)
        .to(batchErrorExchange)
        .with(properties.solvingBatchRoutingKey())
  }
}
