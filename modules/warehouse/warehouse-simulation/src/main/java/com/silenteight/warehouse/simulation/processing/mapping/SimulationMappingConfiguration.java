package com.silenteight.warehouse.simulation.processing.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationMappingConfiguration {

  @Bean
  SimulationAlertV1MappingService simulationAlertMappingService() {
    return new SimulationAlertV1MappingService(
        new PayloadConverter(new ObjectMapper()));
  }
}
