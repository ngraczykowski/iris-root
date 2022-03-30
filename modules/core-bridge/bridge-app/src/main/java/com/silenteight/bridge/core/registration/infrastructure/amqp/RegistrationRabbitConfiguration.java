package com.silenteight.bridge.core.registration.infrastructure.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    AmqpRegistrationIncomingRecommendationReceivedProperties.class,
    AmqpRegistrationIncomingRecommendationDeliveredProperties.class,
    AmqpRegistrationIncomingMatchFeatureInputSetFedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchCompletedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchErrorProperties.class,
    AmqpRegistrationOutgoingVerifyBatchTimeoutProperties.class,
    AmqpRegistrationIncomingVerifyBatchTimeoutProperties.class,
    AmqpRegistrationOutgoingNotifyBatchTimedOutProperties.class,
    AmqpRegistrationProperties.class
})
class RegistrationRabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;

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
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .build();
  }

  @Bean
  Queue matchFeatureInputSetFedDeadLetterQueue(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange matchFeatureInputSetFedDeadLetterExchange(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  DirectExchange matchFeatureInputSetFedExchange(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  Binding matchFeatureInputSetFedBinding(
      @Qualifier("matchFeatureInputSetFedQueue") Queue queue,
      @Qualifier("matchFeatureInputSetFedExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding matchFeatureInputSetFedDeadLetterBinding(
      @Qualifier("matchFeatureInputSetFedDeadLetterQueue") Queue queue,
      @Qualifier("matchFeatureInputSetFedDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

  @Bean
  Queue recommendationsReceivedQueue(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .build();
  }

  @Bean
  Queue recommendationsDeliveredQueue(
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .build();
  }

  @Bean
  Queue recommendationsReceivedDeadLetterQueue(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  Queue recommendationsDeliveredDeadLetterQueue(
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange recommendationsReceivedDeadLetterExchange(
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  DirectExchange recommendationsDeliveredDeadLetterExchange(
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
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
  DirectExchange recommendationsDeliveredExchange(
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  Binding recommendationsDeliveredBinding(
      @Qualifier("recommendationsDeliveredQueue") Queue queue,
      @Qualifier("recommendationsDeliveredExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding recommendationsReceivedDeadLetterBinding(
      @Qualifier("recommendationsReceivedDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsReceivedDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingRecommendationReceivedProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
  }

  @Bean
  Binding recommendationsDeliveredDeadLetterBinding(
      @Qualifier("recommendationsDeliveredDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsDeliveredDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.queueName());
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

  @Bean
  @ConditionalOnProperty("amqp.registration.outgoing.verify-batch-timeout.timeout-enabled")
  DirectExchange verifyBatchTimeoutExchange(
      AmqpRegistrationOutgoingVerifyBatchTimeoutProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  @ConditionalOnProperty("amqp.registration.outgoing.verify-batch-timeout.timeout-enabled")
  Queue verifyBatchTimeoutDelayedQueue(
      AmqpRegistrationIncomingVerifyBatchTimeoutProperties properties,
      AmqpRegistrationOutgoingVerifyBatchTimeoutProperties outgoingProperties
  ) {
    if (outgoingProperties.timeoutEnabled() && outgoingProperties.delayTime() == null) {
      throw new IllegalStateException("""
          Batch timeout handling is enabled, but delay time is not configured.
          More details can be found in the README file: Usage->Configuration.
          """);
    }
    return QueueBuilder.durable(properties.delayedQueueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  @ConditionalOnProperty("amqp.registration.outgoing.verify-batch-timeout.timeout-enabled")
  Binding verifyBatchTimeoutBinding(
      @Qualifier("verifyBatchTimeoutDelayedQueue") Queue queue,
      @Qualifier("verifyBatchTimeoutExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  DirectExchange verifyBatchTimeoutDeadLetterExchange(
      AmqpRegistrationIncomingVerifyBatchTimeoutProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Queue verifyBatchTimeoutQueue(
      AmqpRegistrationIncomingVerifyBatchTimeoutProperties properties) {
    return QueueBuilder.durable(properties.queueName())
        .ttl(properties.queueTimeToLiveInMilliseconds())
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  Queue verifyBatchTimeoutForErrorAlertsQueue(
      AmqpRegistrationIncomingVerifyBatchTimeoutProperties properties) {
    return QueueBuilder.durable(properties.errorAlertsQueueName())
        .ttl(properties.queueTimeToLiveInMilliseconds())
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  Binding verifyBatchTimeoutDeadLetterBinding(
      @Qualifier("verifyBatchTimeoutQueue") Queue queue,
      @Qualifier("verifyBatchTimeoutDeadLetterExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  Binding verifyBatchTimeoutForErrorAlertsDeadLetterBinding(
      @Qualifier("verifyBatchTimeoutForErrorAlertsQueue") Queue queue,
      @Qualifier("verifyBatchTimeoutDeadLetterExchange") DirectExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(EMPTY_ROUTING_KEY);
  }

  @Bean
  DirectExchange batchTimedOutExchange(
      AmqpRegistrationOutgoingNotifyBatchTimedOutProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
