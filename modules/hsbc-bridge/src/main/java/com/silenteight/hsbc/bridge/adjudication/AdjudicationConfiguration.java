package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  private final AnalysisServiceApi analysisServiceApi;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AdjudicationFacade adjudicationFacade(
      AlertService alertService, DatasetService dataService, AnalysisService analysisService) {
    return new AdjudicationFacade(
        alertService, dataService, analysisService);
  }

  @Profile("!dev")
  @Bean
  AlertService alertService(AdjudicationApi adjudicationApiGrpc) {
    return new AlertService(adjudicationApiGrpc, eventPublisher);
  }

  @Profile("dev")
  @Bean
  AlertService alertServiceMock(AdjudicationApi adjudicationApiMock) {
    return new AlertService(adjudicationApiMock, eventPublisher);
  }

  @Profile("!dev")
  @Bean
  DatasetService datasetService(AdjudicationApi adjudicationApiGrpc) {
    return new DatasetService(adjudicationApiGrpc);
  }

  @Profile("dev")
  @Bean
  DatasetService datasetServiceMock(AdjudicationApi adjudicationApiMock) {
    return new DatasetService(adjudicationApiMock);
  }

  @Profile("!dev")
  @Bean
  AnalysisService analysisService() {
    return new AnalysisService(analysisServiceApi, eventPublisher);
  }

  @Profile("dev")
  @Bean
  AnalysisService analysisServiceMock() {
    return new AnalysisService(analysisServiceApi, eventPublisher);
  }

  @Bean
  AdjudicationApi adjudicationApiGrpc(
      AlertServiceBlockingStub alertServiceBlockingStub,
      DatasetServiceBlockingStub datasetServiceBlockingStub) {
    return new AdjudicationApiGrpc(
        alertServiceBlockingStub, datasetServiceBlockingStub);
  }

  @Bean
  AdjudicationApi adjudicationApiMock() {
    return new AdjudicationApiMock();
  }

  @Profile("!dev")
  @Bean
  AdjudicationRecommendationListener adjudicationRecommendationListener(
      ApplicationEventPublisher eventPublisher) {
    return new AdjudicationRecommendationListener(eventPublisher);
  }
}
