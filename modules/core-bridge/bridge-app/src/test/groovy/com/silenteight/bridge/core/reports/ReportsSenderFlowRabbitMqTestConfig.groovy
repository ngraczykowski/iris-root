package com.silenteight.bridge.core.reports

import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties

import org.springframework.amqp.core.*
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ReportsSenderFlowRabbitMqTestConfig {

  static def TEST_QUEUE_NAME = "test-report-sender-queue"

  @Bean
  DirectExchange testWarehouseExchange(ReportsOutgoingConfigurationProperties properties) {
    return new DirectExchange(properties.exchangeName())
  }

  @Bean
  Queue testReportSenderQueue() {
    return QueueBuilder.durable(TEST_QUEUE_NAME).build()
  }

  @Bean
  Binding testReportSenderBinding(
      ReportsOutgoingConfigurationProperties properties,
      DirectExchange testWarehouseExchange, Queue testReportSenderQueue) {
    return BindingBuilder
        .bind(testReportSenderQueue)
        .to(testWarehouseExchange)
        .with(properties.routingKey())
  }
}
