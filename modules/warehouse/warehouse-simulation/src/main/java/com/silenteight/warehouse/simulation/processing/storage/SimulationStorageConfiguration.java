package com.silenteight.warehouse.simulation.processing.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SimulationStorageConfiguration {

  @Bean
  SimulationAlertInsertService simulationAlertInsertService(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new SimulationAlertInsertService(namedParameterJdbcTemplate);
  }

  @Bean
  SimulationMatchInsertService simulationMatchInsertService(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new SimulationMatchInsertService(namedParameterJdbcTemplate);
  }

  @Bean
  SimulationAlertQueryService simulationAlertQueryService(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new SimulationAlertQueryService(namedParameterJdbcTemplate);
  }
}
