package com.silenteight.hsbc.bridge.adjudication;

import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertServiceClient;
import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  private final AlertServiceClient alertServiceClient;
  private final AnalysisFacade analysisFacade;
  private final DatasetServiceClient datasetServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AdjudicationEventHandler adjudicationEventHandler() {
    return AdjudicationEventHandler.builder()
        .alertService(alertService())
        .analysisFacade(analysisFacade)
        .datasetServiceClient(datasetServiceClient)
        .eventPublisher(eventPublisher)
        .build();
  }

  @Profile("!dev")
  @Bean
  AdjudicationRecommendationListener adjudicationRecommendationListener(
          ApplicationEventPublisher eventPublisher,
          RecommendationServiceClient recommendationServiceClient) {
    return new AdjudicationRecommendationListener(eventPublisher, recommendationServiceClient);
  }

  private AlertService alertService() {
    return new AlertService(alertServiceClient, eventPublisher);
  }
}
