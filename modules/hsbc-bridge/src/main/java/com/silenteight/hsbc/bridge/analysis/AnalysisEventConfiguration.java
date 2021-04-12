package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AnalysisEventConfiguration {

  private final AnalysisRepository analysisRepository;

  @Bean
  AnalysisEventHandler analysisEventHandler() {
    return new AnalysisEventHandler(analysisRepository);
  }
}
