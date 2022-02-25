package com.silenteight.bridge.core

import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationIncomingMatchFeatureInputSetFedProperties

import org.springframework.amqp.core.DirectExchange
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

  @Bean
  DirectExchange testMatchFeatureInputSetFedExchange(
      AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties) {
    return new DirectExchange(properties.exchangeName())
  }
}
