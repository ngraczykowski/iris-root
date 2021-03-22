package com.silenteight.hsbc.bridge.adjudication;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnalysisConfiguration {

  @Bean
  AnalysisService analysisService(AnalysisServiceBlockingStub analysisServiceBlockingStub) {
    return new AnalysisService(analysisServiceBlockingStub);
  }

}
