package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchCompletedProperties

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean

class MatchFeatureInputFedReceivedFlowTestConfig {

  static final String SOLVING_TEST_QUEUE_NAME = "test-batch-completed-solving-queue"
  static final String SIMULATION_TEST_QUEUE_NAME = "test-batch-completed-simulation-queue"

  @Bean
  Queue testBatchCompletedSolvingQueue() {
    return QueueBuilder.durable(SOLVING_TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testSolvingBinding(
      DirectExchange batchCompletedExchange, Queue testBatchCompletedSolvingQueue,
      AmqpRegistrationOutgoingNotifyBatchCompletedProperties properties) {
    return BindingBuilder
        .bind(testBatchCompletedSolvingQueue)
        .to(batchCompletedExchange)
        .with(properties.solvingBatchRoutingKey())
  }

  @Bean
  Queue testBatchCompletedSimulationQueue() {
    return QueueBuilder.durable(SIMULATION_TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testSimulationBinding(
      DirectExchange batchCompletedExchange, Queue testBatchCompletedSimulationQueue,
      AmqpRegistrationOutgoingNotifyBatchCompletedProperties properties) {
    return BindingBuilder
        .bind(testBatchCompletedSimulationQueue)
        .to(batchCompletedExchange)
        .with(properties.simulationBatchRoutingKey())
  }
}
