package com.silenteight.bridge.core.recommendation


import org.springframework.amqp.core.*
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class RecommendationFlowRabbitMqTestConfig {

  static final String TEST_QUEUE_NAME = "test-recommendations-queue"

  @Bean
  Queue testRecommendationsReceivedQueue() {
    return QueueBuilder.durable(TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testBinding(
      DirectExchange recommendationsReceivedExchange, Queue testRecommendationsReceivedQueue) {
    return BindingBuilder
        .bind(testRecommendationsReceivedQueue)
        .to(recommendationsReceivedExchange)
        .with('')
  }
}
