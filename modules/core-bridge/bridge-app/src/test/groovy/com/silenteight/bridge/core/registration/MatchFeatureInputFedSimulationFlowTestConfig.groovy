package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean

class MatchFeatureInputFedSimulationFlowTestConfig {

  static final String TEST_QUEUE_NAME = "test-batch-completed-queue"

  @Bean
  Queue testBatchCompletedQueue() {
    return QueueBuilder.durable(TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testBinding(
      DirectExchange batchCompletedExchange, Queue testBatchCompletedQueue,
      AmqpRegistrationOutgoingNotifyBatchCompletedProperties properties) {
    return BindingBuilder
        .bind(testBatchCompletedQueue)
        .to(batchCompletedExchange)
        .with(properties.simulationBatchRoutingKey())
  }
}
