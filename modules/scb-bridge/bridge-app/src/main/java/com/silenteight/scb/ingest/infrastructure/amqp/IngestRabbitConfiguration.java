package com.silenteight.scb.ingest.infrastructure.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.domain.model.Batch.Priority;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AmqpIngestIncomingBatchProcessingProperties.class)
public class IngestRabbitConfiguration {

  public static final String EMPTY_ROUTING_KEY = StringUtils.EMPTY;

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
        .maxPriority(Priority.HIGH.getValue())
        .build();
  }

  @Bean
  Binding batchProcessingBinding(
      DirectExchange batchProcessingExchange, Queue batchProcessingQueue) {
    return BindingBuilder
        .bind(batchProcessingQueue)
        .to(batchProcessingExchange)
        .with(EMPTY_ROUTING_KEY);
  }
}
