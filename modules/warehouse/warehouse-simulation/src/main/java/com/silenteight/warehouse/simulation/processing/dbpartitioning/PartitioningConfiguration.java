package com.silenteight.warehouse.simulation.processing.dbpartitioning;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class PartitioningConfiguration {

  @Bean
  SimulationDbPartitionFactory uniqueAnalysisFactory(
      PartitioningSimulationNamingStrategy partitioningSimulationNamingStrategy,
      JdbcTemplate jdbcTemplate) {

    return new SimulationDbPartitionFactory(partitioningSimulationNamingStrategy, jdbcTemplate);
  }

  @Bean
  PartitioningSimulationNamingStrategy partitioningSimulationNamingStrategy() {
    return new PartitioningSimulationNamingStrategy();
  }
}
