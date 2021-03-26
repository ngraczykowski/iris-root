package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

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
  AnalysisService analysisService(
      AdjudicationApi adjudicationApiGrpc,
      ApplicationEventPublisher eventPublisher) {
    return new AnalysisService(adjudicationApiGrpc, eventPublisher);
  }

  @Profile("dev")
  @Bean
  AnalysisService analysisServiceMock(
      AdjudicationApi adjudicationApiMock,
      ApplicationEventPublisher eventPublisher) {
    return new AnalysisService(adjudicationApiMock, eventPublisher);
  }

  @Bean
  AdjudicationApi adjudicationApiGrpc(
      AlertServiceBlockingStub alertServiceBlockingStub,
      AnalysisServiceBlockingStub analysisServiceBlockingStub,
      DatasetServiceBlockingStub datasetServiceBlockingStub) {
    return new AdjudicationApiGrpc(
        alertServiceBlockingStub, analysisServiceBlockingStub, datasetServiceBlockingStub);
  }

  @Bean
  AdjudicationApi adjudicationApiMock() {
    return new AdjudicationApiMock();
  }

}
