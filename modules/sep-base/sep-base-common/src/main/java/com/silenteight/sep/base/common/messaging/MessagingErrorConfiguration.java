package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
public class MessagingErrorConfiguration {

  public static final String ERROR_CHANNEL_NAME = "errorChannel";

  private final AmqpOutboundFactory amqpOutboundFactory;
  private final MessagingProperties properties;

  @Bean
  Queue errorQueue() {
    return QueueBuilder
        .durable(properties.getErrorQueueName())
        .build();
  }

  @Bean
  PublishSubscribeChannel errorChannel() {
    return new PublishSubscribeChannel();
  }

  @Bean
  IntegrationFlow errorChannelIntegrationFlow() {
    return IntegrationFlows.from(ERROR_CHANNEL_NAME)
        .handle(amqpOutboundFactory.outboundAdapter().routingKey(properties.getErrorQueueName()))
        .get();
  }
}
