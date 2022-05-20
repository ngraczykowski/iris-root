package com.silenteight.warehouse.simulation.processing.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationMappingConfiguration {

  @Bean
  SimulationAlertV1MappingService simulationAlertV1MappingService() {
    return new SimulationAlertV1MappingService(
        new PayloadConverter(new ObjectMapper()));
  }

  @Bean
  SimulationAlertV2MappingService simulationAlertV2MappingService() {
    return new SimulationAlertV2MappingService(
        new PayloadConverter(new ObjectMapper()));
  }
}
