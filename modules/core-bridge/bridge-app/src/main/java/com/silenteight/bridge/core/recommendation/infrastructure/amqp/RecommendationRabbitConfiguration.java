package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    RecommendationIncomingRecommendationsGeneratedConfigurationProperties.class,
    RecommendationIncomingNotifyBatchTimeoutConfigurationProperties.class,
    RecommendationOutgoingRecommendationsStoredConfigurationProperties.class,
    AmqpRecommendationProperties.class,
    AlertsStreamProperties.class
})
class RecommendationRabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;

  @Bean
  Queue recommendationsGeneratedQueue(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .maxPriority(queueMaxPriority)
        .build();
  }

  @Bean
  Queue recommendationsGeneratedDeadLetterQueue(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .maxPriority(queueMaxPriority)
        .build();
  }

  @Bean
  DirectExchange recommendationsGeneratedDeadLetterExchange(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding recommendationsGeneratedBinding(
      @Qualifier("recommendationsGeneratedQueue") Queue queue,
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    var topicExchange = new TopicExchange(properties.exchangeName());
    return BindingBuilder
        .bind(queue)
        .to(topicExchange)
        .with(properties.exchangeRoutingKey());
  }

  @Bean
  Binding recommendationsGeneratedDeadLetterBinding(
      @Qualifier("recommendationsGeneratedDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsGeneratedDeadLetterExchange") DirectExchange exchange,
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

  @Bean
  Queue notifyBatchTimeoutQueue(
      RecommendationIncomingNotifyBatchTimeoutConfigurationProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .maxPriority(queueMaxPriority)
        .build();
  }

  @Bean
  Queue notifyBatchTimeoutDeadLetterQueue(
      RecommendationIncomingNotifyBatchTimeoutConfigurationProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .maxPriority(queueMaxPriority)
        .build();
  }

  @Bean
  DirectExchange notifyBatchTimeoutDeadLetterExchange(
      RecommendationIncomingNotifyBatchTimeoutConfigurationProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding notifyBatchTimeoutBinding(
      @Qualifier("notifyBatchTimeoutQueue") Queue queue,
      @Qualifier("batchTimedOutExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding notifyBatchTimeoutDeadLetterBinding(
      @Qualifier("notifyBatchTimeoutDeadLetterQueue") Queue queue,
      @Qualifier("notifyBatchTimeoutDeadLetterExchange") DirectExchange exchange,
      RecommendationIncomingNotifyBatchTimeoutConfigurationProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

  @Bean
  DirectExchange recommendationsStoredExchange(
      RecommendationOutgoingRecommendationsStoredConfigurationProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
