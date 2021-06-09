package com.silenteight.hsbc.bridge.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!dev")
@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
class SenderConfiguration {

  private final AmqpProperties.Outgoing outgoing;
  private final RabbitTemplate rabbitTemplate;
  private final ProtoMessageConverter converter;

  SenderConfiguration(
      AmqpProperties amqpProperties,
      RabbitTemplate rabbitTemplate, MessageRegistry messageRegistry) {
    this.outgoing = amqpProperties.getOutgoing();
    this.rabbitTemplate = rabbitTemplate;
    this.converter = new ProtoMessageConverter(messageRegistry);
  }

  @Bean
  AmqpWarehouseMessageSender messageSender() {
    return AmqpWarehouseMessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(AmqpWarehouseMessageSender.Configuration.builder()
            .exchangeName(outgoing.getWarehouseExchangeName())
            .routingKey(outgoing.getWarehouseRoutingKey())
            .build())
        .messageConverter(converter)
        .build();
  }

  @Bean
  ModelPersistedMessageSender modelPersistedMessageSender() {
    return ModelPersistedMessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(ModelPersistedMessageSender.Configuration.builder()
            .exchangeName(outgoing.getModelPersistedExchangeName())
            .routingKey(outgoing.getModelPersistedRoutingKey())
            .build())
        .messageConverter(converter)
        .build();
  }

  @Bean
  AmqpWatchlistPersistedMessageSender worldCheckNotificationSender() {
    return AmqpWatchlistPersistedMessageSender.builder()
        .configuration(AmqpWatchlistPersistedMessageSender.Configuration
            .builder()
            .exchangeName(outgoing.getWatchlistPersistedExchangeName())
            .routingKey(outgoing.getWatchlistPersistedRoutingKey())
            .build())
        .amqpTemplate(rabbitTemplate)
        .messageConverter(converter)
        .build();
  }
}
