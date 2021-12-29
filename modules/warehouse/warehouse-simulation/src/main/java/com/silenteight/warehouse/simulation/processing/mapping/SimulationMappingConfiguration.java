package com.silenteight.warehouse.simulation.processing.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulationMappingConfiguration {

  @Bean
  SimulationAlertMappingService simulationAlertMappingService() {
    return new SimulationAlertMappingService(
        new PayloadConverter(new ObjectMapper()));
  }
}
