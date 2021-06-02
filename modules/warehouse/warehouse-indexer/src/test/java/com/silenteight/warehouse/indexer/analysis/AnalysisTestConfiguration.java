package com.silenteight.warehouse.indexer.analysis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AnalysisConfiguration.class)
public class AnalysisTestConfiguration {

  @Bean
  TestNewSimulationAnalysisHandler testNewAnalysisHandler() {
    return new TestNewSimulationAnalysisHandler();
  }
}
