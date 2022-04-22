package com.silenteight.bridge.core

import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties

import org.springframework.amqp.core.*
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class AnalysisTimeoutFlowRabbitMqTestConfig {

  static def TEST_BATCH_COMPLETED_QUEUE_NAME = 'test-batch-completed-queue'

  @Bean
  Queue testBatchCompletedQueue() {
    return QueueBuilder.durable(TEST_BATCH_COMPLETED_QUEUE_NAME).build()
  }

  @Bean
  Binding testBatchCompletedBinding(
      AmqpRegistrationOutgoingNotifyBatchCompletedProperties properties,
      DirectExchange batchCompletedExchange,
      Queue testBatchCompletedQueue) {
    return BindingBuilder
        .bind(testBatchCompletedQueue)
        .to(batchCompletedExchange)
        .with(properties.solvingBatchRoutingKey())
  }
}
