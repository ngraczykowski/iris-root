package com.silenteight.bridge.core.registration.infrastructure.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    AmqpRegistrationIncomingRecommendationReceivedProperties.class,
    AmqpRegistrationIncomingMatchFeatureInputSetFedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchCompletedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchErrorProperties.class
})
class RegistrationRabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final String X_MESSAGE_TTL = "x-message-ttl";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;
  private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
  private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

  @Bean
  DirectExchange batchErrorExchange(AmqpRegistrationOutgoingNotifyBatchErrorProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  DirectExchange batchCompletedExchange(
      AmqpRegistrationOutgoingNotifyBatchCompletedProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  Queue matchFeatureInputSetFedQueue(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .withArgument(X_DEAD_LETTER_EXCHANGE, properties.deadLetterExchangeName())
        .withArgument(X_DEAD_LETTER_ROUTING_KEY, properties.queueName())
        .build();
  }

  @Bean
  Queue matchFeatureInputSetFedDeadLetterQueue(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .withArgument(
            X_MESSAGE_TTL,
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .withArgument(X_DEAD_LETTER_EXCHANGE, EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange matchFeatureInputSetFedDeadLetterExchange(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding matchFeatureInputSetFedBinding(
      @Qualifier("matchFeatureInputSetFedQueue") Queue queue,
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(new DirectExchange(properties.exchangeName()))
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding matchFeatureInputSetFedDeadLetterBinding(
      @Qualifier("matchFeatureInputSetFedDeadLetterQueue") Queue queue,
      @Qualifier("matchFeatureInputSetFedDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return BindingBuilder.bind(queue).to(exchange).with(properties.queueName());
  }

  @Bean
  Queue recommendationsReceivedQueue(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .withArgument(X_DEAD_LETTER_EXCHANGE, properties.deadLetterExchangeName())
        .withArgument(X_DEAD_LETTER_ROUTING_KEY, properties.queueName())
        .build();
  }

  @Bean
  Queue recommendationsReceivedDeadLetterQueue(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .withArgument(
            X_MESSAGE_TTL,
            Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
                .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .withArgument(X_DEAD_LETTER_EXCHANGE, EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange recommendationsReceivedDeadLetterExchange(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding recommendationsReceivedBinding(
      @Qualifier("recommendationsReceivedQueue") Queue queue,
      DirectExchange recommendationsReceivedExchange) {
    return BindingBuilder
        .bind(queue)
        .to(recommendationsReceivedExchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding recommendationsReceivedDeadLetterBinding(
      @Qualifier("recommendationsReceivedDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsReceivedDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return BindingBuilder.bind(queue).to(exchange).with(properties.queueName());
  }

  @Bean
  public SimpleRabbitListenerContainerFactory registrationRabbitAmqpListenerContainerFactory(
      MessageConverter protoMessageConverter,
      ConnectionFactory connectionFactory,
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    var factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setBatchListener(true);
    factory.setBatchSize(properties.batchSize());
    factory.setMessageConverter(protoMessageConverter);
    factory.setConsumerBatchEnabled(true);
    factory.setReceiveTimeout(properties.batchReceiveTimeout().toMillis());
    return factory;
  }
}