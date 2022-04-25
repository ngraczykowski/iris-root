package com.silenteight.connector.ftcc.app

import com.silenteight.proto.fab.api.v1.AlertMessageStored

import groovy.util.logging.Slf4j
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE
import static java.util.Collections.emptyMap
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE

@TestConfiguration
class IngestConfiguration {

  static final String DATA_PREP_QUEUE = 'dataprep-input'

  @Bean
  Queue testWarehouseInputFedQueue() {
    QueueBuilder.durable(DATA_PREP_QUEUE).build()
  }

  @Bean
  Binding testWarehouseBinding(@Value('${ftcc.data-prep.outbound.routing-key}') String routingKey) {
    new Binding(DATA_PREP_QUEUE, QUEUE, CONNECTOR_COMMAND_EXCHANGE, routingKey, emptyMap())
  }

  @Bean
  DataPrepListener dataPrepListener() {
    new DataPrepListener()
  }

  @Slf4j
  class DataPrepListener {

    List<AlertMessageStored> messages = []

    @RabbitListener(queues = DATA_PREP_QUEUE)
    void subscribe(AlertMessageStored message) {
      log.info("Received new message $message")
      messages << message
    }

    def clear() {
      messages.clear()
    }
  }
}
