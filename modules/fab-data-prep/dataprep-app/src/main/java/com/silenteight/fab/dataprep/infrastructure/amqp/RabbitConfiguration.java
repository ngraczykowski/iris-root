package com.silenteight.fab.dataprep.infrastructure.amqp;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE;

@Configuration
@EnableConfigurationProperties({
    AmqpFeedingOutgoingMatchFeatureInputSetFedProperties.class,
    AlertMessagesProperties.class
})
class RabbitConfiguration {

  @Bean
  Queue alertMessagesQueue(AlertMessagesProperties alertMessagesProperties) {
    return QueueBuilder.durable(alertMessagesProperties.getQueueName()).build();
  }

  @Bean
  DirectExchange alertMessagesExchange() {
    return new DirectExchange(CONNECTOR_COMMAND_EXCHANGE);
  }

  @Bean
  Binding feedingBinding(
      Queue alertMessagesQueue, DirectExchange alertMessagesExchange,
      AlertMessagesProperties alertMessagesProperties) {
    return BindingBuilder
        .bind(alertMessagesQueue)
        .to(alertMessagesExchange)
        .with(alertMessagesProperties.getBindingKey());
  }
}
