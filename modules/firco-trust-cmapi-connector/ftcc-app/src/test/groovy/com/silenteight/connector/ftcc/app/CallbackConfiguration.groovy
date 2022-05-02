package com.silenteight.connector.ftcc.app


import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered

import groovy.util.logging.Slf4j
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class CallbackConfiguration {

  static final String CORE_BRIDGE_QUEUE = 'core-bridge-input'

  @Bean
  Queue testCoreBridgeInputFedQueue() {
    QueueBuilder.durable(CORE_BRIDGE_QUEUE).build()
  }

  @Bean
  DirectExchange alertMessagesExchange(
      @Value('${ftcc.core-bridge.outbound.recommendations-delivered.exchange}') String exchange) {
    return new DirectExchange(exchange)
  }

  @Bean
  Binding testCoreBridgeBinding(
      Queue testCoreBridgeInputFedQueue,
      DirectExchange alertMessagesExchange) {
    return BindingBuilder
        .bind(testCoreBridgeInputFedQueue)
        .to(alertMessagesExchange)
        .with("")
  }

  @Bean
  CoreBridgeListener coreBridgeListener() {
    new CoreBridgeListener()
  }

  @Slf4j
  class CoreBridgeListener {

    List<RecommendationsDelivered> messages = []

    @RabbitListener(queues = CORE_BRIDGE_QUEUE)
    void subscribe(RecommendationsDelivered message) {
      log.info("Received new message $message")
      messages << message
    }

    def clear() {
      messages.clear()
    }
  }
}
