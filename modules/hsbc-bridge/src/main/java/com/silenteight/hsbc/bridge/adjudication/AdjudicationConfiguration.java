package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertServiceApi;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;
import com.silenteight.hsbc.bridge.model.ModelUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  private final AlertServiceApi alertServiceApi;
  private final DatasetServiceApi datasetServiceApi;
  private final ModelUseCase modelUseCase;
  private final AnalysisServiceApi analysisServiceApi;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AdjudicationEventHandler adjudicationEventHandler() {
    return new AdjudicationEventHandler(alertService(), datasetServiceApi, analysisService());
  }

  @Profile("!dev")
  @Bean
  AdjudicationRecommendationListener adjudicationRecommendationListener(
      ApplicationEventPublisher eventPublisher) {
    return new AdjudicationRecommendationListener(eventPublisher);
  }

  private AlertService alertService() {
    return new AlertService(alertServiceApi, eventPublisher);
  }

  private AnalysisService analysisService() {
    return new AnalysisService(modelUseCase, analysisServiceApi, eventPublisher);
  }
}
