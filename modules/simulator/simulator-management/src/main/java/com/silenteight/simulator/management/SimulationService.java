package com.silenteight.simulator.management;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Set;

import static com.silenteight.simulator.management.dto.SimulationState.PENDING;

@RequiredArgsConstructor
class SimulationService {

  @NonNull
  private final SimulationEntityRepository repository;

  void createSimulation(CreateSimulationRequest request, String analysisName) {
    SimulationEntity simulationEntity = SimulationEntity.builder()
        .simulationId(request.getId())
        .createdBy(request.getCreatedBy())
        .name(request.getName())
        .description(request.getDescription())
        .modelName(request.getModelName())
        .analysisName(analysisName)
        .datasetNames(Set.of(request.getDatasetName()))
        .state(PENDING)
        .build();

    try {
      repository.save(simulationEntity);
    } catch (DataIntegrityViolationException e) {
      throw new NonUniqueSimulationException(request.getId());
    }
  }
}
