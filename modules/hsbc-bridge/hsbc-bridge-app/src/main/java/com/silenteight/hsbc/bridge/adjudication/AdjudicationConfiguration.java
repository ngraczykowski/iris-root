package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertServiceClient;
import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  private final AlertServiceClient alertServiceClient;
  private final AnalysisFacade analysisFacade;
  private final DatasetServiceClient datasetServiceClient;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  AdjudicationFacade adjudicationFacade() {
    return new AdjudicationFacade(alertService(), analysisFacade, datasetServiceClient);
  }

  private AlertService alertService() {
    return new AlertService(alertServiceClient, eventPublisher);
  }
}
