package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.ModelUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AnalysisFacadeConfiguration {

  private final AnalysisRepository analysisRepository;
  private final AnalysisServiceApi analysisServiceApi;
  private final ModelUseCase modelUseCase;

  @Bean
  AnalysisFacade analysisFacade() {
    return new AnalysisFacade(analysisRepository, analysisServiceApi, modelUseCase);
  }
}
