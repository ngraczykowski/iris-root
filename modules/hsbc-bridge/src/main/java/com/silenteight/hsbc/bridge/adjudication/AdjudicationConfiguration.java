package com.silenteight.hsbc.bridge.adjudication;

import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertServiceApi;
import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  private final AlertServiceApi alertServiceApi;
  private final AnalysisFacade analysisFacade;
  private final DatasetServiceApi datasetServiceApi;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AdjudicationEventHandler adjudicationEventHandler() {
    return AdjudicationEventHandler.builder()
        .alertService(alertService())
        .analysisFacade(analysisFacade)
        .datasetServiceApi(datasetServiceApi)
        .eventPublisher(eventPublisher)
        .build();
  }

  @Profile("!dev")
  @Bean
  AdjudicationRecommendationListener adjudicationRecommendationListener(
          ApplicationEventPublisher eventPublisher,
          AnalysisServiceApi analysisServiceApi) {
    return new AdjudicationRecommendationListener(eventPublisher, analysisServiceApi);
  }

  private AlertService alertService() {
    return new AlertService(alertServiceApi, eventPublisher);
  }
}
