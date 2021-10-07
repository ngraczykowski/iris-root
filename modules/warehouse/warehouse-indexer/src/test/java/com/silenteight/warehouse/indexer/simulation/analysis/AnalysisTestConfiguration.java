package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.environment.EnvironmentModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    AnalysisConfiguration.class,
    EnvironmentModule.class
})
@RequiredArgsConstructor
public class AnalysisTestConfiguration {

  @Bean
  TestNewSimulationAnalysisHandler testNewAnalysisHandler() {
    return new TestNewSimulationAnalysisHandler();
  }
}
