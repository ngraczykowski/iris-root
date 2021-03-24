package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  @Bean
  DatasetService datasetService(DatasetServiceBlockingStub datasetServiceBlockingStub) {
    return new DatasetService(datasetServiceBlockingStub);
  }

  @Bean
  AnalysisService analysisService(
      AnalysisServiceBlockingStub analysisServiceBlockingStub,
      ApplicationEventPublisher eventPublisher) {
    return new AnalysisService(analysisServiceBlockingStub, eventPublisher);
  }

  @Bean
  AlertService alertService(AlertServiceBlockingStub alertServiceBlockingStub) {
    return new AlertService(alertServiceBlockingStub, eventPublisher);
  }

}
