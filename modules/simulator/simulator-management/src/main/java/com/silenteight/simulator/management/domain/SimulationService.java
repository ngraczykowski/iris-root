package com.silenteight.simulator.management.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.simulator.management.create.CreateSimulationRequest;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Set;

import static com.silenteight.simulator.management.domain.SimulationState.PENDING;

@RequiredArgsConstructor
public class SimulationService {

  @NonNull
  private final SimulationEntityRepository repository;

  public void createSimulation(
      CreateSimulationRequest request,
      Set<String> datasets,
      String analysis) {
    SimulationEntity simulationEntity = SimulationEntity.builder()
        .simulationId(request.getId())
        .createdBy(request.getCreatedBy())
        .name(request.getSimulationName())
        .description(request.getDescription())
        .modelName(request.getModel())
        .analysisName(analysis)
        .datasetNames(datasets)
        .state(PENDING)
        .build();

    try {
      repository.save(simulationEntity);
    } catch (DataIntegrityViolationException e) {
      throw new NonUniqueSimulationException(request.getId());
    }
  }
}
