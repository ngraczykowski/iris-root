package com.silenteight.simulator.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.backend.common.integration.RabbitModuleConfiguration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.AE_EVENT_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.GOV_EVENTS_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.WH_EVENT_EXCHANGE;
import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BrokerProperties.class)
@Import({ RabbitModuleConfiguration.class })
class SimulatorBrokerConfiguration {

  @Valid
  @NonNull
  private final BrokerProperties properties;

  @Bean
  Declarables recommendationsBinding() {
    return new Declarables(
        binding(
            properties.recommendationsQueueName(),
            AE_EVENT_EXCHANGE,
            properties.recommendationsRoutingKey()));
  }

  @Bean
  Declarables indexResponseBinding() {
    return new Declarables(
        binding(
            properties.indexResponseQueueName(),
            WH_EVENT_EXCHANGE,
            properties.indexResponseRoutingKey()));
  }

  @Bean
  Declarables datasetExpiredBinding() {
    return new Declarables(
        binding(
            properties.datasetExpiredQueueName(),
            AE_EVENT_EXCHANGE,
            properties.datasetExpiredRoutingKey()));
  }

  @Bean
  Declarables  modelsArchivedBinding() {
    return new Declarables(
        binding(
            properties.modelsArchivedQueueName(),
            GOV_EVENTS_EXCHANGE,
            properties.modelsArchivedRoutingKey()));
  }

  private static Binding binding(String queueName, String exchange, String routingKey) {
    return new Binding(queueName, QUEUE, exchange, routingKey, emptyMap());
  }

  @Bean
  Queue recommendationsQueue() {
    return queue(properties.recommendationsQueueName());
  }

  @Bean
  Queue indexResponseQueue() {
    return queue(properties.indexResponseQueueName());
  }

  @Bean
  Queue datasetExpiredQueue() {
    return queue(properties.datasetExpiredQueueName());
  }

  @Bean
  Queue simulatorModelsArchivedQueue() {
    return queue(properties.modelsArchivedQueueName());
  }

  private static Queue queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }
}
