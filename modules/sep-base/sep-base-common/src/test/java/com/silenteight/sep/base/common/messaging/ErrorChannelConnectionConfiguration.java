package com.silenteight.sep.base.common.messaging;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class ErrorChannelConnectionConfiguration {

  @Primary
  @Bean
  RabbitTemplate template(ConnectionFactory connectionFactory) {
    var template = new RabbitTemplate(connectionFactory);

    template.setMessageConverter(new SimpleMessageConverter());

    return template;
  }
}
