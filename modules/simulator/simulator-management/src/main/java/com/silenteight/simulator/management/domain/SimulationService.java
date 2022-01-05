package com.silenteight.simulator.management.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.simulator.management.create.CreateSimulationRequest;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.management.domain.SimulationState.PENDING;

@Slf4j
@RequiredArgsConstructor
public class SimulationService {

  @NonNull
  private final SimulationRepository repository;
  @NonNull
  private final TimeSource timeSource;

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
      throw new NonUniqueSimulationException(request.getId(), e);
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

    if (simulationEntity.isArchived()) {
      log.info("Simulation is already 'ARCHIVED' SimulationEntity={}", simulationEntity);
    } else {
      simulationEntity.finish(timeSource.offsetDateTime());
      log.debug("Saved as 'DONE' SimulationEntity={}", simulationEntity);
    }
  }

  @Transactional
  public void cancel(@NonNull UUID simulationId) {
    SimulationEntity simulationEntity = getBySimulationId(simulationId);
    simulationEntity.cancel();
    log.debug("Saved as 'CANCELLED' SimulationEntity={}", simulationEntity);
  }

  @Transactional
  public void archive(@NonNull UUID simulationId) {
    SimulationEntity simulationEntity = getBySimulationId(simulationId);
    simulationEntity.archive();
    log.debug("Saved as 'ARCHIVED' SimulationEntity={}", simulationEntity);
  }

  public void updateNumberOfSolvedAlertsInSimulation(long solvedAlerts, UUID simulationId) {
    log.info("Updating simulation with id={} with {} of solved alerts", simulationId, solvedAlerts);

    SimulationEntity simulationEntity = repository
        .findSimulationEntityBySimulationId(simulationId)
        .orElseThrow(() -> new SimulationNotFoundException(simulationId));

    simulationEntity.setSolvedAlerts(solvedAlerts);
    simulationEntity.setCurrentTimeForUpdatedAt();
  }

  public void timeout(UUID id) {
    setErrorStateOnSimulation(id);
  }

  private void setErrorStateOnSimulation(UUID simulationId) {
    SimulationEntity simulationEntity = repository
        .findSimulationEntityBySimulationId(simulationId)
        .orElseThrow(() -> new SimulationNotFoundException(simulationId));
    simulationEntity.timeout(timeSource.offsetDateTime());
  }

  private SimulationEntity getBySimulationId(UUID simulationId) {
    return repository
        .findSimulationEntityBySimulationId(simulationId)
        .orElseThrow(() -> new SimulationNotFoundException(simulationId));
  }

  @Transactional
  public void streaming(@NonNull String analysis) {
    SimulationEntity simulationEntity = repository
        .findByAnalysisName(analysis)
        .orElseThrow(() -> new SimulationNotFoundException(analysis));
    simulationEntity.streaming();
    log.debug("Saved as 'STREAMING' SimulationEntity={}", simulationEntity);
  }
}
