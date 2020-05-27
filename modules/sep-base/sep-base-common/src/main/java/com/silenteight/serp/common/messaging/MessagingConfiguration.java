package com.silenteight.serp.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.common.protocol.AnyProtoMessageConverter;
import com.silenteight.serp.common.protocol.MessageRegistry;
import com.silenteight.serp.common.protocol.MessageRegistryFactory;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@EnableRabbit
@EnableConfigurationProperties(MessagingProperties.class)
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
  ProtoMessageConverter messageConverter(MessageRegistry messageRegistry) {
    ProtoMessageConverter converter = new ProtoMessageConverter(messageRegistry);

    converter.setUnpackAny(false);
    converter.setWrapWithAny(false);

    return converter;
  }

  @Bean
  MessageSenderFactory messageSenderFactory(
      RabbitOperations operations,
      ProtoMessageConverter messageConverter,
      Collection<SendMessageListener> listeners) {

    return new AmqpMessageSenderFactory(operations, messageConverter, listeners);
  }

  @Bean
  ProcessLoggingMessageListener processLoggingMessageListener() {
    return new ProcessLoggingMessageListener();
  }

  @Bean
  RabbitConfiguringPostProcessor containerFactoryConfiguringPostProcessor(
      MessagingProperties properties,
      ObjectProvider<ReceiveMessageListener> listeners) {

    RabbitConfiguringPostProcessor postProcessor =
        new RabbitConfiguringPostProcessor(properties, listeners.stream().collect(toList()));

    postProcessor.setErrorHandler(
        new CustomConditionalRejectingErrorHandler(new CustomExceptionStrategy()));

    return postProcessor;
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
