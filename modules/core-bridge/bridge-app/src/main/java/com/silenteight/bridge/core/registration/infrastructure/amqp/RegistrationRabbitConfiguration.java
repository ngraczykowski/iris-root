package com.silenteight.bridge.core.registration.infrastructure.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({
    AmqpRegistrationIncomingRecommendationStoredProperties.class,
    AmqpRegistrationIncomingRecommendationDeliveredProperties.class,
    AmqpRegistrationIncomingMatchFeatureInputSetFedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchCompletedProperties.class,
    AmqpRegistrationOutgoingNotifyBatchErrorProperties.class,
    AmqpRegistrationOutgoingVerifyBatchTimeoutProperties.class,
    AmqpRegistrationIncomingVerifyBatchTimeoutProperties.class,
    AmqpRegistrationOutgoingNotifyBatchTimedOutProperties.class,
    AmqpRegistrationProperties.class,
    AmqpRegistrationOutgoingNotifyBatchDeliveredProperties.class,
    RegistrationVerifyBatchTimeoutProperties.class,
    AmqpRegistrationOutgoingDataRetentionProperties.class
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
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .maxPriority(queueMaxPriority)
        .build();
  }

  @Bean
  Queue matchFeatureInputSetFedDeadLetterQueue(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .maxPriority(queueMaxPriority)
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
  Queue recommendationsStoredQueue(
      AmqpRegistrationIncomingRecommendationStoredProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.queueName())
        .deadLetterExchange(properties.deadLetterExchangeName())
        .deadLetterRoutingKey(properties.queueName())
        .maxPriority(queueMaxPriority)
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
  Queue recommendationsStoredDeadLetterQueue(
      AmqpRegistrationIncomingRecommendationStoredProperties properties,
      @Value("${silenteight.bridge.amqp.queue-max-priority}") Integer queueMaxPriority) {
    return QueueBuilder.durable(properties.deadLetterQueueName())
        .ttl(Optional.ofNullable(properties.deadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .maxPriority(queueMaxPriority)
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
  DirectExchange recommendationsStoredDeadLetterExchange(
      AmqpRegistrationIncomingRecommendationStoredProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  DirectExchange recommendationsDeliveredDeadLetterExchange(
      AmqpRegistrationIncomingRecommendationDeliveredProperties properties) {
    return new DirectExchange(properties.deadLetterExchangeName());
  }

  @Bean
  Binding recommendationsStoredBinding(
      @Qualifier("recommendationsStoredQueue") Queue queue,
      DirectExchange recommendationsStoredExchange) {
    return BindingBuilder
        .bind(queue)
        .to(recommendationsStoredExchange)
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
  Binding recommendationsStoredDeadLetterBinding(
      @Qualifier("recommendationsStoredDeadLetterQueue") Queue queue,
      @Qualifier("recommendationsStoredDeadLetterExchange") DirectExchange exchange,
      AmqpRegistrationIncomingRecommendationStoredProperties properties) {
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
  @ConditionalOnProperty("registration.verify-batch-timeout.enabled")
  DirectExchange verifyBatchTimeoutExchange(
      AmqpRegistrationOutgoingVerifyBatchTimeoutProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }

  @Bean
  @ConditionalOnProperty("registration.verify-batch-timeout.enabled")
  Queue verifyBatchTimeoutDelayedQueue(
      AmqpRegistrationIncomingVerifyBatchTimeoutProperties properties,
      RegistrationVerifyBatchTimeoutProperties timeoutProperties
  ) {
    if (timeoutProperties.enabled() && timeoutProperties.delayTime() == null) {
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
  @ConditionalOnProperty("registration.verify-batch-timeout.enabled")
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

  @Bean
  DirectExchange batchDeliveredExchange(
      AmqpRegistrationOutgoingNotifyBatchDeliveredProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
