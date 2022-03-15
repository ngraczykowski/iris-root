package com.silenteight.bridge.core

import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties

import org.springframework.amqp.core.TopicExchange
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ExternalExchangesConfigurationIT {

  @Bean
  TopicExchange testRecommendationsGeneratedExchange(
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties) {
    return new TopicExchange(properties.exchangeName())
  }
}
