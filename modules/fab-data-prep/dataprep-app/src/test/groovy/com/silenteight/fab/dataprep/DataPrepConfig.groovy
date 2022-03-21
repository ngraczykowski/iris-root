package com.silenteight.fab.dataprep

import com.silenteight.fab.dataprep.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed

import groovy.util.logging.Slf4j
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class DataPrepConfig {

  static final String MATCH_FEATURE_INPUT_FED_QUEUE = 'match-feature-input-fed'

  @Bean
  Queue testMatchFeatureInputFedQueue() {
    QueueBuilder.durable(MATCH_FEATURE_INPUT_FED_QUEUE).build()
  }

  @Bean
  DirectExchange testMatchFeatureInputFedExchange(AmqpFeedingOutgoingMatchFeatureInputSetFedProperties properties) {
    new DirectExchange(properties.exchangeName)
  }

  @Bean
  Binding testBinding(
      DirectExchange testMatchFeatureInputFedExchange, Queue testMatchFeatureInputFedQueue) {
    BindingBuilder
        .bind(testMatchFeatureInputFedQueue)
        .to(testMatchFeatureInputFedExchange)
        .with('')
  }

  @Bean
  CoreBridgeListener coreBridgeListener() {
    new CoreBridgeListener()
  }

  @Slf4j
  class CoreBridgeListener {

    List<MessageAlertMatchesFeatureInputFed> messages = []

    @RabbitListener(queues = MATCH_FEATURE_INPUT_FED_QUEUE)
    void subscribe(MessageAlertMatchesFeatureInputFed message) {
      log.info("Received new message $message")
      messages << message
    }

    def clear() {
      messages.clear()
    }
  }
}
