package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AdjudicationConfiguration {

  @Bean
  AdjudicationFacade adjudicationFacade(
      AlertService alertService, DatasetService dataService, AnalysisService analysisService) {
    return new AdjudicationFacade(
        alertService, dataService, analysisService);
  }

}
