package com.silenteight.bridge.core.registration.adapter.incoming.scheduler

import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingDataRetentionProperties

import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class DataRetentionFlowRabbitMqTestConfig {

  static def PERSONAL_INFO_EXPIRED_TEST_QUEUE_NAME = 'personal-info-expired-test-queue'
  static def ALERTS_EXPIRED_TEST_QUEUE_NAME = 'alerts-expired-test-queue'

  @Autowired
  AmqpRegistrationOutgoingDataRetentionProperties properties

  @Bean
  Queue testPersonalInfoExpiredQueue() {
    return QueueBuilder.durable(PERSONAL_INFO_EXPIRED_TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testPersonalInfoExpiredBinding(
      Queue testPersonalInfoExpiredQueue, TopicExchange testBridgeRetentionExchange) {
    return BindingBuilder
        .bind(testPersonalInfoExpiredQueue)
        .to(testBridgeRetentionExchange)
        .with(properties.personalInformationExpiredRoutingKey())
  }

  @Bean
  Queue testAlertsExpiredQueue() {
    return QueueBuilder.durable(ALERTS_EXPIRED_TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testAlertsExpiredBinding(
      Queue testAlertsExpiredQueue, TopicExchange testBridgeRetentionExchange) {
    return BindingBuilder
        .bind(testAlertsExpiredQueue)
        .to(testBridgeRetentionExchange)
        .with(properties.alertsExpiredRoutingKey())
  }
}
