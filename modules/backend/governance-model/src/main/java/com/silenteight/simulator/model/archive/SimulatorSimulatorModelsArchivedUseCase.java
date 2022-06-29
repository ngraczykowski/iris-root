package com.silenteight.simulator.model.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.simulator.management.archive.ArchiveSimulationRequest;
import com.silenteight.simulator.management.archive.ArchiveSimulationUseCase;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.list.dto.SimulationListDto;
import com.silenteight.simulator.model.archive.amqp.listener.SimulatorModelsArchivedMessageHandler;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class SimulatorSimulatorModelsArchivedUseCase implements SimulatorModelsArchivedMessageHandler {

  static final String ARCHIVED_BY = "archived_model";

  @NonNull
  private final ListSimulationsQuery listSimulationsQuery;
  @NonNull
  private final ArchiveSimulationUseCase archiveSimulationUseCase;

  @Override
  public void handle(@NonNull ModelsArchived message) {
    Collection<String> modelNames = message.getModelsList();
    log.info("Archived models: modelNames={}" + modelNames);

    List<SimulationListDto> simulations = listSimulationsQuery.findByModels(modelNames);
    log.debug("Simulations to be archived: simulations={}" + simulations);

    simulations
        .stream()
        .map(SimulatorSimulatorModelsArchivedUseCase::toArchiveSimulationRequest)
        .forEach(archiveSimulationUseCase::activate);

    log.info("Simulations cancelled: simulations={}" + simulations);
  }

  private static ArchiveSimulationRequest toArchiveSimulationRequest(SimulationListDto simulation) {
    return ArchiveSimulationRequest.builder()
        .id(simulation.getId())
        .archivedBy(ARCHIVED_BY)
        .build();
  }
}
