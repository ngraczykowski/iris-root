package com.silenteight.warehouse.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_COMMAND_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.GOV_EVENT_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.SIM_COMMAND_EXCHANGE;
import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BrokerProperties.class)
class BrokerConfiguration {

  @Valid
  @NonNull
  private final BrokerProperties properties;

  @Bean
  Declarables alertBackupIndexingBinding() {
    return new Declarables(
        binding(
            properties.alertBackupIndexingQueueName(),
            BRIDGE_COMMAND_EXCHANGE,
            properties.alertBackupIndexingRoutingKey()));
  }

  @Bean
  Declarables qaBackupIndexingBinding() {
    return new Declarables(
        binding(
            properties.qaBackupIndexingQueueName(),
            GOV_EVENT_EXCHANGE,
            properties.qaBackupIndexingRoutingKey()));
  }

  @Bean
  Declarables alertProductionIndexingBinding() {
    return new Declarables(
        binding(
            properties.alertProductionIndexingQueueName(),
            BRIDGE_COMMAND_EXCHANGE,
            properties.alertProductionIndexingRoutingKey()));
  }

  @Bean
  Declarables qaIndexingBinding() {
    return new Declarables(
        binding(
            properties.qaIndexingQueueName(),
            GOV_EVENT_EXCHANGE,
            properties.qaIndexingRoutingKey()));
  }

  @Bean
  Declarables alertSimulationIndexingBinding() {
    return new Declarables(
        binding(
            properties.alertSimulationIndexingQueueName(),
            SIM_COMMAND_EXCHANGE,
            properties.alertSimulationIndexingRoutingKey()));
  }

  private static Binding binding(String queueName, String exchange, String routingKey) {
    return new Binding(queueName, QUEUE, exchange, routingKey, emptyMap());
  }

  @Bean
  Queue alertBackupIndexingQueue() {
    return queue(
        properties.alertBackupIndexingQueueName(),
        properties.alertBackupIndexingMaxPriority());
  }

  @Bean
  Queue qaBackupIndexingQueue() {
    return queue(
        properties.qaBackupIndexingQueueName(),
        properties.qaBackupIndexingMaxPriority());
  }

  @Bean
  Queue alertProductionIndexingQueue() {
    return queue(
        properties.alertProductionIndexingQueueName(),
        properties.alertProductionIndexingMaxPriority());
  }

  @Bean
  Queue qaIndexingQueue() {
    return queue(
        properties.qaIndexingQueueName(),
        properties.qaIndexingMaxPriority());
  }

  @Bean
  Queue alertSimulationIndexingQueue() {
    return queue(
        properties.alertSimulationIndexingQueueName(),
        properties.alertSimulationIndexingMaxPriority());
  }

  private static Queue queue(String queueName, Integer maxPriority) {
    QueueBuilder builder = QueueBuilder.durable(queueName);
    if (maxPriority != null)
      builder.maxPriority(maxPriority);

    return builder.build();
  }

  @Bean
  RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  BindingRemover bindingRemover(RabbitAdmin rabbitAdmin) {
    return new BindingRemover(rabbitAdmin, properties.bindingsToRemove());
  }
}
