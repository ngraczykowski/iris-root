package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpOutboundEndpointSpec;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@ConditionalOnProperty(
    prefix = "serp.messaging.error-queue", name = "enabled", havingValue = "true")
public class MessagingErrorConfiguration {

  public static final String ERROR_CHANNEL_NAME = "errorChannel";
  public static final String ERROR_MESSAGE_LISTENER_CHANNEL_NAME = "errorMessageListenerChannel";

  private final MessagingProperties properties;
  private final Jackson2JsonMessageConverter jackson2JsonMessageConverter;
  private final ConnectionFactory connectionFactory;

  @Bean
  @ConditionalOnProperty(prefix = "serp.messaging.error-queue", name = "declare",
      havingValue = "true", matchIfMissing = true)
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
  @ConditionalOnMissingBean(ErrorMessageListener.class)
  ErrorMessageListener errorMessageListener() {
    String logExpression =
        "'Message pushed to " + properties.getErrorQueueName() + " : ' + headers";
    LoggingHandler loggingHandler = new LoggingHandler(Level.WARN);
    loggingHandler.setLogExpressionString(logExpression);
    return new DefaultLoggingErrorMessageListener(loggingHandler);
  }

  @Bean
  @ConditionalOnBean(ErrorMessageListener.class)
  IntegrationFlow errorMessageListeningIntegrationFlow(List<ErrorMessageListener> listeners) {
    return IntegrationFlows
        .from(ERROR_MESSAGE_LISTENER_CHANNEL_NAME)
        .handle(message -> listeners.forEach(l -> l.handleMessage(message)))
        .get();
  }

  @Bean
  IntegrationFlow errorChannelIntegrationFlow() {
    return IntegrationFlows.from(ERROR_CHANNEL_NAME)
        .transform(new ErrorMessageTransformer())
        .wireTap(ERROR_MESSAGE_LISTENER_CHANNEL_NAME)
        .handle(
            ((AmqpOutboundEndpointSpec) Amqp.outboundAdapter(createRabbitTemplate()))
                .routingKey(properties.getErrorQueue().getName()))
        .get();
  }

  private RabbitTemplate createRabbitTemplate() {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jackson2JsonMessageConverter);
    return template;
  }
}
