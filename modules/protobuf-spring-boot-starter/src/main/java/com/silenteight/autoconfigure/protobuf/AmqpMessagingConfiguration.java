package com.silenteight.autoconfigure.protobuf;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@RequiredArgsConstructor
class AmqpMessagingConfiguration {

  @Bean
  @ConditionalOnMissingBean
  AmqpProtoMessageConverter protoMessageConverter(MessageRegistry messageRegistry) {
    return AmqpProtoMessageConverter.builder()
        .messageRegistry(messageRegistry)
        .unpackAny(false)
        .wrapWithAny(false)
        .build();
  }

  @Bean
  @ConditionalOnMissingBean
  MessageRegistry messageRegistry() {
    return new MessageRegistryFactory(
        "com.silenteight",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    ).create();
  }

  @Bean
  @ConditionalOnMissingBean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  @ConditionalOnMissingBean
  ContentTypeDelegatingMessageConverter messageConverter(
      AmqpProtoMessageConverter protoConverter,
      Jackson2JsonMessageConverter jsonConverter) {
    var converter = new ContentTypeDelegatingMessageConverter(protoConverter);
    converter.addDelegate("application/x-protobuf", protoConverter);
    converter.addDelegate("application/json", jsonConverter);
    return converter;
  }
}
