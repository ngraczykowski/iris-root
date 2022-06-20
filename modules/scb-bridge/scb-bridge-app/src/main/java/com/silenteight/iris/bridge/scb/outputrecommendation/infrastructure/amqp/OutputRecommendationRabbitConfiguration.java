/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(value = {
    OutputNotifyBatchCompletedRabbitProperties.class,
    OutputBatchErrorRabbitProperties.class,
    OutputRecommendationDeliveredProperties.class,
    AmqpOutputRecommendationProperties.class
})
class OutputRecommendationRabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;

  // batch completed

  @Bean
  Queue batchCompletedQueue(OutputNotifyBatchCompletedRabbitProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .maxPriority(properties.queueMaxPriority())
        .build();
  }

  @Bean
  Binding batchCompletedBinding(
      @Qualifier("batchCompletedQueue") Queue queue,
      OutputNotifyBatchCompletedRabbitProperties properties) {
    DirectExchange exchange = new DirectExchange(properties.exchangeName());
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.batchRoutingKey());
  }

  @Bean
  DirectExchange batchCompletedDeadLetterExchange(
      OutputNotifyBatchCompletedRabbitProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Queue batchCompletedDeadLetterQueue(OutputNotifyBatchCompletedRabbitProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .maxPriority(properties.queueMaxPriority())
        .build();
  }

  @Bean
  Binding batchCompletedDeadLetterBinding(
      @Qualifier("batchCompletedDeadLetterQueue") Queue queue,
      @Qualifier("batchCompletedDeadLetterExchange") DirectExchange exchange,
      OutputNotifyBatchCompletedRabbitProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

  // batch error

  @Bean
  Queue batchErrorQueue(
      OutputBatchErrorRabbitProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .build();
  }

  @Bean
  Binding batchErrorBinding(
      @Qualifier("batchErrorQueue") Queue queue, OutputBatchErrorRabbitProperties properties) {
    DirectExchange exchange = new DirectExchange(properties.exchangeName());
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.batchRoutingKey());
  }

  @Bean
  DirectExchange batchErrorDeadLetterExchange(
      OutputBatchErrorRabbitProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Queue batchErrorDeadLetterQueue(
      OutputBatchErrorRabbitProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  Binding batchErrorDeadLetterBinding(
      @Qualifier("batchErrorDeadLetterQueue") Queue queue,
      @Qualifier("batchErrorDeadLetterExchange") DirectExchange exchange,
      OutputBatchErrorRabbitProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }
}
