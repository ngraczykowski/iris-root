package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    RecommendationIncomingDataRetentionConfigurationProperties.class
})
class RecommendationDataRetentionRabbitConfiguration {

  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;
  private static final String EMPTY_ROUTING_KEY = "";

  @Bean
  Queue recommendationAlertsExpiredQueue(
      RecommendationIncomingDataRetentionConfigurationProperties properties) {
    return QueueBuilder.durable(properties.alertsExpiredQueueName())
        .build();
  }

  @Bean
  Binding recommendationAlertsExpiredBinding(
      @Qualifier("recommendationAlertsExpiredQueue") Queue queue,
      RecommendationIncomingDataRetentionConfigurationProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(new DirectExchange(properties.exchangeName()))
        .with(properties.alertsExpiredRoutingKey());
  }

  @Bean
  Queue recommendationAlertsExpireDeadLetterQueue(
      RecommendationIncomingDataRetentionConfigurationProperties properties) {
    return QueueBuilder.durable(properties.alertsExpiredDeadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange recommendationAlertsExpiredDeadLetterExchange(
      RecommendationIncomingDataRetentionConfigurationProperties properties) {
    return new DirectExchange(properties.alertsExpiredDeadLetterExchangeName());
  }

  @Bean
  Binding recommendationAlertsExpiredDeadLetterBinding(
      @Qualifier("recommendationAlertsExpireDeadLetterQueue") Queue queue,
      @Qualifier("recommendationAlertsExpiredDeadLetterExchange") DirectExchange exchange,
      RecommendationIncomingDataRetentionConfigurationProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.alertsExpiredQueueName());
  }
}
