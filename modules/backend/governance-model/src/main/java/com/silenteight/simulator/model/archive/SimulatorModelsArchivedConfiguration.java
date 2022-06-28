package com.silenteight.simulator.model.archive;

import lombok.NonNull;

import com.silenteight.simulator.management.archive.ArchiveSimulationUseCase;
import com.silenteight.simulator.management.list.ListSimulationsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SimulatorModelsArchivedConfiguration {

  @Bean
  SimulatorSimulatorModelsArchivedUseCase simulatorModelsArchivedUseCase(
      @NonNull ListSimulationsQuery listSimulationsQuery,
      @NonNull ArchiveSimulationUseCase archiveSimulationUseCase) {

    return new SimulatorSimulatorModelsArchivedUseCase(listSimulationsQuery, archiveSimulationUseCase);
  }
}
