package com.silenteight.fab.dataprep.adapter.incoming

import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import static com.silenteight.fab.dataprep.adapter.incoming.AlertMessagesRabbitAmqpListener.QUEUE_NAME_PROPERTY

@TestConfiguration
class IngestFlowRabbitMqTestConfig {

  @Bean
  Queue testStoredAlertsAndMatchesQueue(@Value(QUEUE_NAME_PROPERTY) String queueName) {
    QueueBuilder.durable(queueName).build()
  }
}
