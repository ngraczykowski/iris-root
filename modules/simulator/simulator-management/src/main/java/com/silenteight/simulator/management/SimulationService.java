package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.dto.SimulationState;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Set;

@AllArgsConstructor
class SimulationService {

  @NonNull
  private SimulationEntityRepository repository;

  void createSimulation(CreateSimulationRequest request) {
    SimulationEntity simulationEntity = SimulationEntity.builder()
        .simulationId(request.getId())
        .createdBy(request.getCreatedBy())
        .name(request.getName())
        .description(request.getDescription())
        .policyId(request.getPolicyId())
        .datasetIds(Set.of(request.getDatasetId()))
        .state(SimulationState.PENDING)
        .build();

    try {
      repository.save(simulationEntity);
    } catch (DataIntegrityViolationException e) {
      throw new NonUniqueSimulationException(request.getId());
    }
  }
}
