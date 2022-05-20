package com.silenteight.warehouse.retention.simulation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
class RetentionSimulationConfiguration {

  @Bean
  RetentionSimulationUseCase simulationRetentionUseCase(JdbcTemplate jdbcTemplate) {
    return new RetentionSimulationUseCase(jdbcTemplate);
  }
}
