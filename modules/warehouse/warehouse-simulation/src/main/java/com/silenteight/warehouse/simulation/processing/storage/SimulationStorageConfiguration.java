package com.silenteight.warehouse.simulation.processing.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class SimulationStorageConfiguration {

  @Bean
  SimulationAlertInsertService simulationAlertInsertService(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new SimulationAlertInsertService(namedParameterJdbcTemplate);
  }
}
