package com.silenteight.sens.webapp.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.AUDIT_EVENT_EXCHANGE;
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
  Declarables auditLogBinding() {
    return new Declarables(
        binding(
            properties.auditLogQueueName(),
            AUDIT_EVENT_EXCHANGE,
            properties.auditLogRoutingKey()));
  }

  private static Binding binding(String queueName, String exchange, String routingKey) {
    return new Binding(queueName, QUEUE, exchange, routingKey, emptyMap());
  }

  @Bean
  Queue auditLogQueue() {
    return queue(properties.auditLogQueueName());
  }

  private static Queue queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }
}
