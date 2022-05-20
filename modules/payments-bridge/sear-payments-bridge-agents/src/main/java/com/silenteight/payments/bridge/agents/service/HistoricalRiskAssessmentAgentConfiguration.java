package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.agents.service.decoder.DecodedResourceLoader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
class HistoricalRiskAssessmentAgentConfiguration {

  private final DecodedResourceLoader decodedResourceLoader;

  @Value("classpath:historical-risk-assessment-agent/model-a1.s8a")
  String modelA1Url;

  @Bean
  HistoricalRiskAssessmentUseCase historicalRiskAssessmentAgent() throws IOException {
    var configTupleList = HistoricalRiskAssessmentBlackListCsvReader.getConfigListFromCsv(
        decodedResourceLoader, modelA1Url);

    return new HistoricalRiskAssessmentAgent(configTupleList);
  }
}
