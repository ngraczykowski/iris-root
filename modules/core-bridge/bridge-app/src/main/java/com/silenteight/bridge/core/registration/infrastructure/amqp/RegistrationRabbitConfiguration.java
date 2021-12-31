package com.silenteight.bridge.core.registration.infrastructure.amqp;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RegistrationRabbitProperties.class)
class RegistrationRabbitConfiguration {

  @Bean
  DirectExchange batchErrorExchange(RegistrationRabbitProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
