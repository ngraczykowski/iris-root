package com.silenteight.fab.dataprep.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE;

@Configuration
@EnableConfigurationProperties({
    AmqpFeedingOutgoingMatchFeatureInputSetFedProperties.class,
    AlertMessagesProperties.class
})
class RabbitConfiguration {

  private static final String EMPTY_ROUTING_KEY = "";
  private static final Integer DEFAULT_TTL_IN_MILLISECONDS = 2000;

  @Bean
  Queue alertMessagesQueue(AlertMessagesProperties alertMessagesProperties) {
    return QueueBuilder.durable(alertMessagesProperties.getQueueName())
        .deadLetterExchange(alertMessagesProperties.getDeadLetterExchangeName())
        .deadLetterRoutingKey(alertMessagesProperties.getQueueName())
        .build();
  }

  @Bean
  Queue alertMessagesDeadLetterQueue(AlertMessagesProperties properties) {
    return QueueBuilder.durable(properties.getDeadLetterQueueName())
        .ttl(Optional.ofNullable(properties.getDeadLetterQueueTimeToLiveInMilliseconds())
            .orElse(DEFAULT_TTL_IN_MILLISECONDS))
        .deadLetterExchange(EMPTY_ROUTING_KEY)
        .build();
  }

  @Bean
  DirectExchange alertMessagesDeadLetterExchange(AlertMessagesProperties properties) {
    return new DirectExchange(properties.getDeadLetterExchangeName());
  }

  @Bean
  DirectExchange alertMessagesExchange() {
    return new DirectExchange(CONNECTOR_COMMAND_EXCHANGE);
  }

  @Bean
  Binding feedingBinding(
      @Qualifier("alertMessagesQueue") Queue alertMessagesQueue,
      @Qualifier("alertMessagesExchange") DirectExchange alertMessagesExchange,
      AlertMessagesProperties alertMessagesProperties) {
    return BindingBuilder
        .bind(alertMessagesQueue)
        .to(alertMessagesExchange)
        .with(alertMessagesProperties.getBindingKey());
  }

  @Bean
  Binding alertMessagesDeadLetterBinding(
      @Qualifier("alertMessagesDeadLetterQueue") Queue queue,
      @Qualifier("alertMessagesDeadLetterExchange") DirectExchange exchange,
      AlertMessagesProperties properties) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(properties.getQueueName());
  }
}
