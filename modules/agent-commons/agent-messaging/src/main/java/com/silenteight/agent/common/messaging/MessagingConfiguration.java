package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.common.messaging.proto.MessageRegistry;
import com.silenteight.agent.common.messaging.proto.MessageRegistryFactory;
import com.silenteight.agent.common.messaging.proto.ProtoMessageConverter;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@EnableRabbit
public class MessagingConfiguration {

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
}
