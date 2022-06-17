package com.silenteight.serp.governance.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.List;
import javax.annotation.PostConstruct;

@RequiredArgsConstructor
class ExchangeRemover {

  @NonNull
  private final RabbitAdmin rabbitAdmin;
  @NonNull
  private final List<String> properties;

  @PostConstruct
  void remove() {
    properties.forEach(this::remove);
  }

  void remove(String exchange) {
    rabbitAdmin.deleteExchange(exchange);
  }
}
