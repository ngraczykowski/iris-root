/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({ IngestDataRetentionProperties.class })
class DataRetentionRabbitConfiguration {

  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;
  private static final String EMPTY_ROUTING_KEY = "";

  @Bean
  Queue dataRetentionQueue(IngestDataRetentionProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .build();
  }

  @Bean
  Binding dataRetentionBinding(
      @Qualifier("dataRetentionQueue") Queue queue,
      IngestDataRetentionProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(new DirectExchange(properties.exchangeName()))
        .with(properties.routingKey());
  }

  @Bean
  Queue dataRetentionDeadLetterQueue(IngestDataRetentionProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange dataRetentionDeadLetterExchange(IngestDataRetentionProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding dataRetentionDeadLetterBinding(
      @Qualifier("dataRetentionDeadLetterQueue") Queue queue,
      @Qualifier("dataRetentionDeadLetterExchange") DirectExchange exchange,
      IngestDataRetentionProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }
}
