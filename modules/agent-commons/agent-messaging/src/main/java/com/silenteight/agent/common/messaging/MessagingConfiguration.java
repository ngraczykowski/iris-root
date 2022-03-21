package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.common.messaging.proto.AnyProtoMessageConverter;
import com.silenteight.agent.common.messaging.proto.MessageRegistry;
import com.silenteight.agent.common.messaging.proto.MessageRegistryFactory;
import com.silenteight.agent.common.messaging.proto.ProtoMessageConverter;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@EnableRabbit
public class MessagingConfiguration implements RabbitListenerConfigurer {

  private final BeanFactory beanFactory;

  @Bean
  MessageRegistry messageRegistry() {
    MessageRegistryFactory factory = new MessageRegistryFactory(
        "com.silenteight.proto",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    );

    return factory.create();
  }

  @Bean
  ProtoMessageConverter protoMessageConverter(MessageRegistry messageRegistry) {
    ProtoMessageConverter converter = new ProtoMessageConverter(messageRegistry);

    converter.setUnpackAny(false);
    converter.setWrapWithAny(false);

    return converter;
  }

  @Bean
  ContentTypeDelegatingMessageConverter messageConverter(
      ProtoMessageConverter protoMessageConverter) {
    var converter = new ContentTypeDelegatingMessageConverter(protoMessageConverter);
    converter.addDelegate("application/x-protobuf", protoMessageConverter);
    return converter;
  }

  @Bean
  RabbitConfiguringPostProcessor containerFactoryConfiguringPostProcessor() {
    var rabbitConfigPostProcessor = new RabbitConfiguringPostProcessor();
    rabbitConfigPostProcessor.setErrorHandler(
        new CustomConditionalRejectingErrorHandler(new CustomExceptionStrategy()));

    return rabbitConfigPostProcessor;
  }

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    ConfigurableConversionService conversionService = new DefaultConversionService();
    conversionService.addConverter(new AnyProtoMessageConverter(messageRegistry()));

    DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
    factory.setBeanFactory(beanFactory);
    factory.setConversionService(conversionService);
    factory.afterPropertiesSet();

    registrar.setMessageHandlerMethodFactory(factory);
  }
}
