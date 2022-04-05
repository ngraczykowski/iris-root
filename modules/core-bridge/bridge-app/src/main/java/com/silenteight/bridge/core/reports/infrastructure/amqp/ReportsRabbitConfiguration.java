package com.silenteight.bridge.core.reports.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConditionalOnProperty(value = "reports.enabled", havingValue = "true")
class ReportsRabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;

  @Bean
  Queue batchDeliveredForReportsQueue(ReportsIncomingBatchDeliveredProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .build();
  }

  @Bean
  Binding batchDeliveredForReportsBinding(
      ReportsIncomingBatchDeliveredProperties properties,
      @Qualifier("batchDeliveredForReportsQueue") Queue queue) {
    var exchange = new DirectExchange(properties.exchangeName());
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Queue batchDeliveredForReportsDeadLetterQueue(
      ReportsIncomingBatchDeliveredProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange batchDeliveredForReportsDeadLetterExchange(
      ReportsIncomingBatchDeliveredProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding batchDeliveredDeadLetterForReportsBinding(
      @Qualifier("batchDeliveredForReportsDeadLetterQueue") Queue queue,
      @Qualifier("batchDeliveredForReportsDeadLetterExchange") DirectExchange exchange,
      ReportsIncomingBatchDeliveredProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

}
