package com.silenteight.connector.ftcc.callback;

import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.recommendation.api.library.v1.RecommendationsIn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class GrpcRecommendationConfiguration {

  @Bean
  RecommendationClientApi client(RecommendationServiceClient recommendationServiceClient) {
    return (List<String> alertIds) -> recommendationServiceClient.getRecommendations(
        RecommendationsIn.builder()
            .alertIds(alertIds)
            .build());
  }
}
