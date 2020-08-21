package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(AmqpOutboundEndpoint.class)
public class IntegrationConfiguration {

  private final ObjectProvider<SimpleRabbitListenerContainerFactory>
      simpleRabbitListenerContainerFactories;

  private final ObjectProvider<DirectRabbitListenerContainerFactory>
      directRabbitListenerContainerFactories;

  private final ContentTypeDelegatingMessageConverter messageConverter;
  private final RabbitTemplate rabbitTemplate;

  @Bean
  AmqpInboundFactory amqpInboundChannelAdapterFactory() {
    var factory = new AmqpInboundFactory(messageConverter);

    simpleRabbitListenerContainerFactories.ifAvailable(
        factory::setSimpleMessageListenerContainerFactory);
    directRabbitListenerContainerFactories.ifAvailable(
        factory::setDirectMessageListenerContainerFactory);

    factory.setErrorHandler(
        new CustomConditionalRejectingErrorHandler(new CustomExceptionStrategy()));

    return factory;
  }

  @Bean
  AmqpOutboundFactory amqpOutboundFactory() {
    return new AmqpOutboundFactory(rabbitTemplate);
  }
}
