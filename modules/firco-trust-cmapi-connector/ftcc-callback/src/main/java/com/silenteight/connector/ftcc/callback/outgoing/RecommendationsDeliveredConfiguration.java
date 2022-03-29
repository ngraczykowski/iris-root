package com.silenteight.connector.ftcc.callback.outgoing;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RecommendationsDeliveredAmqpProperties.class)
public class RecommendationsDeliveredConfiguration {

  @Bean
  RecommendationsDeliveredPublisher recommendationsDeliveredPublisher(
      RabbitTemplate rabbitTemplate, RecommendationsDeliveredAmqpProperties properties) {
    return new RecommendationsDeliveredRabbitPublisher(rabbitTemplate, properties);
  }
}
