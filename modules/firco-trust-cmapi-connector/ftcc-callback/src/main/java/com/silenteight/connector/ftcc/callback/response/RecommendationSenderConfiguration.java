package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.callback.newdecision.DecisionConfigurationHolder;
import com.silenteight.connector.ftcc.callback.newdecision.MapStatusUseCase;
import com.silenteight.connector.ftcc.callback.newdecision.MapStatusUseCaseService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(RecommendationSenderProperties.class)
@RequiredArgsConstructor
class RecommendationSenderConfiguration {

  private final RecommendationSenderProperties properties;

  @Bean
  RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
        .setReadTimeout(properties.getReadTimeout())
        .setConnectTimeout(properties.getConnectionTimeout())
        .build();
  }

  @Bean
  RecommendationSender recommendationSender(RestTemplate restTemplate) {
    return new RecommendationSender(restTemplate, properties.getEndpoint());
  }

  @Bean
  MapStatusUseCase mapStatusUseCase(DecisionConfigurationHolder configurationHolder) {
    return new MapStatusUseCaseService(configurationHolder);
  }
}
