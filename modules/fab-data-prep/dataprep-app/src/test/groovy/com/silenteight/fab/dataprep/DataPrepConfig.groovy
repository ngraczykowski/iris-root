package com.silenteight.fab.dataprep

import com.silenteight.data.api.v2.ProductionDataIndexRequest
import com.silenteight.fab.dataprep.infrastructure.amqp.AmqpFeedingOutgoingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed

import groovy.util.logging.Slf4j
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_COMMAND_EXCHANGE
import static java.util.Collections.emptyMap
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE

@TestConfiguration
class DataPrepConfig {

  static final String MATCH_FEATURE_INPUT_FED_QUEUE = 'match-feature-input-fed'
  static final String WAREHOUSE_QUEUE = 'warehouse-input-fed'

  @Bean
  Queue testMatchFeatureInputFedQueue() {
    QueueBuilder.durable(MATCH_FEATURE_INPUT_FED_QUEUE).build()
  }

  @Bean
  Queue testWarehouseInputFedQueue() {
    QueueBuilder.durable(WAREHOUSE_QUEUE).build()
  }

  @Bean
  DirectExchange testMatchFeatureInputFedExchange(
      AmqpFeedingOutgoingMatchFeatureInputSetFedProperties properties) {
    new DirectExchange(properties.exchangeName)
  }

  @Bean
  Binding testBinding(
      DirectExchange testMatchFeatureInputFedExchange,
      Queue testMatchFeatureInputFedQueue) {
    BindingBuilder
        .bind(testMatchFeatureInputFedQueue)
        .to(testMatchFeatureInputFedExchange)
        .with('')
  }

  @Bean
  Binding testWarehouseBinding() {
    new Binding(
        WAREHOUSE_QUEUE, QUEUE, BRIDGE_COMMAND_EXCHANGE, "command.index-request.production",
        emptyMap())
  }

  @Bean
  CoreBridgeListener coreBridgeListener() {
    new CoreBridgeListener()
  }

  @Bean
  WarehouseListener warehouseListener() {
    new WarehouseListener()
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

  @Slf4j
  class WarehouseListener {

    List<ProductionDataIndexRequest> messages = []

    @RabbitListener(queues = WAREHOUSE_QUEUE)
    void subscribe(Message msg) {
      ProductionDataIndexRequest message = ProductionDataIndexRequest.parseFrom(msg.getBody())
      log.info("Received new message $message")
      messages << message
    }

    def clear() {
      messages.clear()
    }
  }
}
