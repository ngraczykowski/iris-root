package com.silenteight.simulator.management.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.create.CreateSimulationRequest;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.silenteight.simulator.management.domain.SimulationState.PENDING;

@Slf4j
@RequiredArgsConstructor
public class SimulationService {

  @NonNull
  private final SimulationRepository repository;

  public void createSimulation(
      CreateSimulationRequest request, Set<String> datasets, String analysis) {

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
    simulationEntity.run();

    try {
      repository.save(simulationEntity);
      log.debug("Saved SimulationEntity={}", simulationEntity);
    } catch (DataIntegrityViolationException e) {
      throw new NonUniqueSimulationException(request.getId());
    }
  }

  public boolean exists(String analysis) {
    return repository
        .findByAnalysisName(analysis)
        .isPresent();
  }

  @Transactional
  public void finish(String analysis) {
    SimulationEntity simulationEntity = repository
        .findByAnalysisName(analysis)
        .orElseThrow(() -> new SimulationNotFoundException(analysis));
    simulationEntity.finish();
    log.debug("Saved as 'DONE' SimulationEntity={}", simulationEntity);
  }
}
