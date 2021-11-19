package com.silenteight.simulator.model.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.simulator.management.archive.ArchiveSimulationRequest;
import com.silenteight.simulator.management.archive.ArchiveSimulationUseCase;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.list.dto.SimulationDto;
import com.silenteight.simulator.model.archive.amqp.listener.ModelsArchivedMessageHandler;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class ModelsArchivedUseCase implements ModelsArchivedMessageHandler {

  static final String ARCHIVED_BY = "archived_model";

  @NonNull
  private final ListSimulationsQuery listSimulationsQuery;
  @NonNull
  private final ArchiveSimulationUseCase archiveSimulationUseCase;

  @Override
  public void handle(@NonNull ModelsArchived message) {
    Collection<String> modelNames = message.getModelsList();
    log.info("Archived models: modelNames={}" + modelNames);

    List<SimulationDto> simulations = listSimulationsQuery.findByModels(modelNames);
    log.debug("Simulations to be archived: simulations={}" + simulations);

    simulations
        .stream()
        .map(ModelsArchivedUseCase::toArchiveSimulationRequest)
        .forEach(archiveSimulationUseCase::activate);

    log.info("Simulations cancelled: simulations={}" + simulations);
  }

  private static ArchiveSimulationRequest toArchiveSimulationRequest(SimulationDto simulation) {
    return ArchiveSimulationRequest.builder()
        .id(simulation.getId())
        .archivedBy(ARCHIVED_BY)
        .build();
  }
}
