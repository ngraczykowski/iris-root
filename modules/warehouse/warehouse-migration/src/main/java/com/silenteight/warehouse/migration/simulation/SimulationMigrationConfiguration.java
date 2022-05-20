package com.silenteight.warehouse.migration.simulation;

import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertQueryService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationStorageConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SimulationStorageConfiguration.class)
@ConditionalOnProperty(value = "warehouse.simulation.migration.enabled", havingValue = "true")
class SimulationMigrationConfiguration {

  @Bean
  SimulationMigration simulationMigration(
      SimulationMigrationService migrationService,
      SimulationAlertQueryService simulationAlertQueryService) {
    return new SimulationMigration(migrationService, simulationAlertQueryService);
  }

  @Bean
  SimulationMigrationService simulationMigrationService(
      SimulationDbPartitionFactory simulationDbPartitionFactory,
      SimulationAlertInsertService simulationAlertInsertService) {

    return new SimulationMigrationService(
        simulationAlertInsertService,
        simulationDbPartitionFactory);
  }
}
