package com.silenteight.warehouse.report.simulation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class DeprecatedSimulationConfiguration {

  @Bean
  DeprecatedSimulationReportsDefinitionsUseCase deprecatedSimulationReportsDefinitionsUseCase(
      List<DeprecatedSimulationReportsProvider> deprecatedSimulationReportsProviders) {

    return new DeprecatedSimulationReportsDefinitionsUseCase(deprecatedSimulationReportsProviders);
  }
}
