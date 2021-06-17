package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.protocol.AnyProtoMessageConverter;
import com.silenteight.hsbc.bridge.protocol.MessageRegistry;
import com.silenteight.hsbc.bridge.protocol.MessageRegistryFactory;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@EnableRabbit
@RequiredArgsConstructor
class AmqpMessagingConfiguration implements RabbitListenerConfigurer, BeanPostProcessor {

  private final BeanFactory beanFactory;

  @Bean
  AmqpProtoMessageConverter protoMessageConverter(MessageRegistry messageRegistry) {
    return AmqpProtoMessageConverter.builder()
        .messageRegistry(messageRegistry)
        .unpackAny(false)
        .wrapWithAny(false)
        .build();
  }

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    var conversionService = new DefaultConversionService();
    conversionService.addConverter(new AnyProtoMessageConverter(messageRegistry()));

    var factory = new DefaultMessageHandlerMethodFactory();
    factory.setBeanFactory(beanFactory);
    factory.setConversionService(conversionService);
    factory.afterPropertiesSet();

    registrar.setMessageHandlerMethodFactory(factory);
  }

  @Bean
  MessageRegistry messageRegistry() {
    return new MessageRegistryFactory(
        "com.silenteight",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    ).create();
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  ContentTypeDelegatingMessageConverter messageConverter(
      AmqpProtoMessageConverter protoConverter,
      Jackson2JsonMessageConverter jsonConverter) {
    var converter = new ContentTypeDelegatingMessageConverter(protoConverter);
    converter.addDelegate("application/x-protobuf", protoConverter);
    converter.addDelegate("application/json", jsonConverter);
    return converter;
  }
}
