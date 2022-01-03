package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    RecommendationIncomingRecommendationsGeneratedConfigurationProperties.class,
    RecommendationOutgoingRecommendationsReceivedConfigurationProperties.class })
class RecommendationRabbitConfiguration {

  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;
  private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
  private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
  private static final String X_MESSAGE_TTL = "x-message-ttl";
  private static final String EMPTY_ROUTING_KEY = "";

  @Bean
  Queue recommendationsGeneratedQueue(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .withArgument(X_DEAD_LETTER_EXCHANGE, properties.deadLetterExchangeName())
        .withArgument(X_DEAD_LETTER_ROUTING_KEY, properties.queueName())
        .build();
  }

  @Bean
  Queue recommendationsReadyDeadLetterQueue(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .withArgument(
            X_MESSAGE_TTL,
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .withArgument(X_DEAD_LETTER_EXCHANGE, EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange recommendationsReadyDeadLetterExchange(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding recommendationsGeneratedBinding(
      @Qualifier("recommendationsGeneratedQueue") Queue queue,
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    var topicExchange = new TopicExchange(properties.exchangeName());
    return BindingBuilder.bind(queue).to(topicExchange).with(properties.exchangeRoutingKey());
  }

  @Bean
  Binding deadLetterBinding(
      @Qualifier("recommendationsReadyDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsReadyDeadLetterExchange") DirectExchange exchange,
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return BindingBuilder.bind(queue).to(exchange).with(properties.queueName());
  }

  @Bean
  DirectExchange recommendationReceivedExchange(
      RecommendationOutgoingRecommendationsReceivedConfigurationProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
