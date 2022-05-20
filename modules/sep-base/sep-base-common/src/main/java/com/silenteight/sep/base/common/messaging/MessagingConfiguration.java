package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.MessagingProperties.Compression;
import com.silenteight.sep.base.common.messaging.compression.CompressionBundle;
import com.silenteight.sep.base.common.messaging.compression.Lz4CompressionBundleConfigurer;
import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageDecrypter;
import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;
import com.silenteight.sep.base.common.messaging.encryption.MessagingEncryptionProperties;
import com.silenteight.sep.base.common.messaging.postprocessing.SepMessagePostProcessors;
import com.silenteight.sep.base.common.messaging.postprocessing.SepMessagePostProcessorsConfigurer;
import com.silenteight.sep.base.common.protocol.AnyProtoMessageConverter;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.util.Collection;
import java.util.Optional;

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
  ProtoMessageConverter protoMessageConverter(MessageRegistry messageRegistry) {
    ProtoMessageConverter converter = new ProtoMessageConverter(messageRegistry);

    converter.setUnpackAny(false);
    converter.setWrapWithAny(false);

    return converter;
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "serp.messaging.encryption",
      name = "enabled",
      havingValue = "true"
  )
  MessagingEncryptionProperties messagingEncryptionProperties(MessagingProperties props) {

    return props.getEncryption();
  }

  @Bean
  SepMessagePostProcessors sepMessagePostProcessors(
      MessagingProperties properties,
      ObjectProvider<AmqpMessageDecrypter> decrypter,
      ObjectProvider<AmqpMessageEncypter> encrypter) {
    var configurer = new SepMessagePostProcessorsConfigurer();

    decrypter.ifAvailable(configurer::setMessageDecrypter);
    encrypter.ifAvailable(configurer::setMessageEncrypter);

    tryConfigureCompression(properties.getCompression())
        .ifPresent(configurer::setCompressionBundle);

    return configurer.configure();
  }

  private static Optional<CompressionBundle> tryConfigureCompression(Compression compression) {
    if (compression.isEnabled()) {
      var configurer = new Lz4CompressionBundleConfigurer(compression.getLevel());
      return Optional.of(configurer.configure());
    }

    return Optional.empty();
  }

  @Bean
  MessageSenderFactory messageSenderFactory(
      RabbitOperations operations,
      ContentTypeDelegatingMessageConverter messageConverter,
      Collection<SendMessageListener> listeners) {

    return new AmqpMessageSenderFactory(operations, messageConverter, listeners);
  }

  @Bean
  ProcessLoggingMessageListener processLoggingMessageListener() {
    return new ProcessLoggingMessageListener();
  }

  @Bean
  RabbitConfiguringPostProcessor containerFactoryConfiguringPostProcessor(
      SepMessagePostProcessors sepMessagePostProcessors,
      ObjectProvider<ReceiveMessageListener> listeners) {

    var rabbitConfigPostProcessor = new RabbitConfiguringPostProcessor(
        sepMessagePostProcessors,
        listeners.stream().collect(toList())
    );

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

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  ContentTypeDelegatingMessageConverter messageConverter(
      ProtoMessageConverter protoMessageConverter,
      Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    var converter = new ContentTypeDelegatingMessageConverter(protoMessageConverter);
    converter.addDelegate("application/x-protobuf", protoMessageConverter);
    converter.addDelegate("application/json", jackson2JsonMessageConverter);
    return converter;
  }
}
