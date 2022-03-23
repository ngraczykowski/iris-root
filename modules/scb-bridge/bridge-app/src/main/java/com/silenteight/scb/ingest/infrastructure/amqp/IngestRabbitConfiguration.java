package com.silenteight.scb.ingest.infrastructure.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AmqpIngestIncomingBatchProcessingProperties.class)
public class IngestRabbitConfiguration {

  @Bean
  DirectExchange batchProcessingExchange(
      AmqpIngestIncomingBatchProcessingProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  Queue batchProcessingQueue(
      AmqpIngestIncomingBatchProcessingProperties properties) {
    return QueueBuilder
        .durable(properties.queueName())
        .build();
  }

  @Bean
  Binding batchProcessingBinding(
      DirectExchange batchProcessingExchange, Queue batchProcessingQueue) {
    return BindingBuilder
        .bind(batchProcessingQueue)
        .to(batchProcessingExchange)
        .withQueueName();
  }
}
