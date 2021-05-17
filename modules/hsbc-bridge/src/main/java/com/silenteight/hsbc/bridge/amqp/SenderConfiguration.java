package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.report.WarehouseClient;

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
  WarehouseClient warehouseClient() {
    return new MessageSender(rabbitTemplate, amqpProperties.getWarehouseExchangeName());
  }
}
