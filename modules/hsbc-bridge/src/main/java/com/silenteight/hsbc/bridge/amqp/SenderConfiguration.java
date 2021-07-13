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

  SenderConfiguration(
      AmqpProperties amqpProperties,
      RabbitTemplate rabbitTemplate) {
    this.outgoing = amqpProperties.getOutgoing();
    this.rabbitTemplate = rabbitTemplate;
  }

  @Bean
  AmqpWarehouseMessageSender messageSender() {
    return AmqpWarehouseMessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(AmqpWarehouseMessageSender.Configuration.builder()
            .exchangeName(outgoing.getWarehouseExchangeName())
            .routingKey(outgoing.getWarehouseRoutingKey())
            .build())
        .build();
  }

  @Bean
  AmqpIsPepMessageSender isPepMessageSender() {
    return AmqpIsPepMessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(AmqpIsPepMessageSender.Configuration.builder()
            .exchangeName(outgoing.getIsPepExchangeName())
            .routingKey(outgoing.getIsPepRoutingKey())
            .build())
        .build();
  }

  @Bean
  AmqpHistoricalDecisionMessageSender historicalDecisionMessageSender() {
    return AmqpHistoricalDecisionMessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(AmqpHistoricalDecisionMessageSender.Configuration.builder()
            .exchangeName(outgoing.getHistoricalDecisionExchangeName())
            .routingKey(outgoing.getHistoricalDecisionRoutingKey())
            .build())
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
        .build();
  }
}
