package com.silenteight.serp.governance.common.integration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ExchangeProperties.class)
class ExchangeConfiguration {

  @Valid
  @NonNull
  private final ExchangeProperties properties;

  @Bean
  Declarables governanceRabbitSchemaDeclarables() {
    return new Declarables(
        topicExchange(properties.getAnalytics()),
        topicExchange(properties.getModel()),
        topicExchange(properties.getNotification()),
        topicExchange(properties.getSolutionDiscrepancy()));
  }

  private static TopicExchange topicExchange(String topicName) {
    return ExchangeBuilder
        .topicExchange(topicName)
        .durable(true)
        .build();
  }
}
