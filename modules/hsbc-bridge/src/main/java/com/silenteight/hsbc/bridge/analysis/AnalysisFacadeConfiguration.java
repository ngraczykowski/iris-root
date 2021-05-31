package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(AnalysisProperties.class)
@Configuration
@RequiredArgsConstructor
class AnalysisFacadeConfiguration {

  private final AnalysisProperties analysisProperties;
  private final AnalysisRepository analysisRepository;
  private final AnalysisServiceClient analysisServiceClient;
  private final ModelServiceClient modelServiceClient;

  @Bean
  AnalysisFacade analysisFacade() {
    return new AnalysisFacade(
        analysisRepository, registerer(), analysisProperties.getAlertTimeoutDuration());
  }
  @Bean
  AnalysisEventListener analysisEventListener() {
    return new AnalysisEventListener(analysisRepository);
  }

  private Registerer registerer() {
    return new Registerer(analysisServiceClient, modelServiceClient);
  }
}
