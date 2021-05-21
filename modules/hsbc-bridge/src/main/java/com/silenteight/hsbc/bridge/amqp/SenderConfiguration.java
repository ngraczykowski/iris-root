package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!dev")
@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
@RequiredArgsConstructor
class SenderConfiguration {

  private final AmqpProperties amqpProperties;
  private final RabbitTemplate rabbitTemplate;

  @Bean
  MessageSender messageSender() {
    return MessageSender.builder()
        .amqpTemplate(rabbitTemplate)
        .configuration(MessageSender.Configuration.builder()
            .exchangeName(amqpProperties.getWarehouseExchangeName())
            .routingKey(rabbitTemplate.getRoutingKey())
            .build())
        .messageConverter(new ProtoMessageConverter())
        .build();
  }
}
