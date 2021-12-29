package com.silenteight.warehouse.simulation.processing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertMappingService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Valid;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(SimulationProcessingProperties.class)
class SimulationProcessingConfiguration {

  @Bean
  SimulationAlertIndexUseCase simulationAlertIndexUseCase(
      SimulationAlertInsertService simulationAlertInsertService,
      SimulationAlertMappingService simulationAlertMappingService,
      SimulationDbPartitionFactory simulationPartitionFactory,
      @Valid SimulationProcessingProperties properties,
      TimeSource timeSource) {

    return new SimulationAlertIndexUseCase(
        simulationPartitionFactory,
        simulationAlertMappingService,
        simulationAlertInsertService,
        timeSource,
        properties.getSimulationBatchSize());
  }
}
