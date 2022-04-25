package com.silenteight.warehouse.simulation.processing;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertV1MappingService;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertV2MappingService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchInsertService;

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
  SimulationAlertV1UseCase simulationAlertV1UseCase(
      SimulationAlertInsertService simulationAlertInsertService,
      SimulationAlertV1MappingService simulationAlertV1MappingService,
      SimulationDbPartitionFactory simulationPartitionFactory,
      @Valid SimulationProcessingProperties properties,
      TimeSource timeSource) {

    return new SimulationAlertV1UseCase(
        simulationPartitionFactory,
        simulationAlertV1MappingService,
        simulationAlertInsertService,
        timeSource,
        properties.getSimulationBatchSize());
  }

  @Bean
  SimulationAlertV2UseCase simulationIndexV2UseCase(
      SimulationAlertInsertService simulationAlertInsertService,
      SimulationMatchInsertService simulationMatchInsertService,
      SimulationAlertV2MappingService simulationAlertV2MappingService,
      SimulationDbPartitionFactory simulationPartitionFactory,
      @Valid SimulationProcessingProperties properties,
      TimeSource timeSource) {
    return new SimulationAlertV2UseCase(
        simulationPartitionFactory,
        simulationAlertV2MappingService,
        simulationAlertInsertService,
        simulationMatchInsertService,
        timeSource,
        properties.getSimulationBatchSize());
  }
}
