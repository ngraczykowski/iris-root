package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecommendationRabbitProperties.class)
class RecommendationRabbitConfiguration {

  @Bean
  DirectExchange recommendationReceivedExchange(RecommendationRabbitProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
