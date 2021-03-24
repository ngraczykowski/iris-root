package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.model.ModelUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AnalysisEventConfiguration {

  private final AdjudicationFacade adjudicationFacade;
  private final AnalysisRepository analysisRepository;
  private final ModelUseCase modelUseCase;

  @Bean
  AnalysisEventHandler analysisEventHandler() {
    return new AnalysisEventHandler(analysisRepository);
  }

  @Bean
  BulkPreProcessingFinishedEventHandler bulkPreProcessingFinishedEventHandler() {
    return new BulkPreProcessingFinishedEventHandler(adjudicationFacade, modelUseCase);
  }
}
