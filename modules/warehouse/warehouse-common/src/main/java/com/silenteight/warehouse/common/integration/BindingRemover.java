package com.silenteight.warehouse.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.List;
import javax.annotation.PostConstruct;

import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@RequiredArgsConstructor
class BindingRemover {

  @NonNull
  private final RabbitAdmin rabbitAdmin;
  @NonNull
  private final List<BindingProperties> properties;

  @PostConstruct
  void remove() {
    properties.forEach(this::remove);
  }

  void remove(BindingProperties properties) {
    rabbitAdmin.removeBinding(
        binding(
            properties.getQueueName(),
            properties.getExchange(),
            properties.getRoutingKey()));
  }

  private static Binding binding(String queueName, String exchange, String routingKey) {
    return new Binding(queueName, QUEUE, exchange, routingKey, emptyMap());
  }
}
