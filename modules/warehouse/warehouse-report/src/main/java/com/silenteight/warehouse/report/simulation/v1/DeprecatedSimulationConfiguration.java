package com.silenteight.warehouse.report.simulation.v1;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(DeprecatedSimulationProperties.class)
class DeprecatedSimulationConfiguration {


  @Bean
  DeprecatedSimulationReportsDefinitionsUseCase deprecatedSimulationReportsDefinitionsUseCase(
      List<DeprecatedSimulationReportsProvider> deprecatedSimulationReportsProviders,
      @Valid DeprecatedSimulationProperties properties) {

    return new DeprecatedSimulationReportsDefinitionsUseCase(deprecatedSimulationReportsProviders,
        properties.getHiddenTypes());
  }
}
