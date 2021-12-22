package com.silenteight.simulator.management.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
import static com.silenteight.simulator.management.domain.SimulationState.STREAMING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

@Slf4j
@RequiredArgsConstructor
class SimulationQuery
    implements ListSimulationsQuery, SimulationDetailsQuery, SimulationStateProvider {

  @NonNull
  private final SimulationRepository repository;

  @Override
  public List<SimulationDto> list(Collection<SimulationState> states) {
    log.debug("Listing all SimulationDto by states={}", states);

    if (states.contains(RUNNING))
      states = Stream.concat(states.stream(), of(STREAMING)).collect(toSet());
      
    return repository
        .findAllByStateIn(states)
        .stream()
        .map(SimulationEntity::toDto)
        .collect(toList());
  }

  @Override
  public List<SimulationDto> findByModel(@NonNull String model) {
    log.debug("Listing all SimulationDto by model={}", model);

    Collection<SimulationEntity> simulationEntities = repository.findAllByModelName(model);
    if (simulationEntities.isEmpty())
      throw new InvalidModelNameException(model);

    return simulationEntities
        .stream()
        .map(SimulationEntity::toDto)
        .collect(toList());
  }

  @Override
  public List<SimulationDto> findByModels(@NonNull Collection<String> models) {
    log.debug("Listing all SimulationDto by models={}", models);

    return repository
        .findAllByModelNameIn(models)
        .stream()
        .map(SimulationEntity::toDto)
        .collect(toList());
  }

  @Override
  public SimulationDetailsDto get(@NonNull UUID simulationId) {
    log.debug("Getting SimulationDetailsDto by simulationId={}", simulationId);

    return repository
        .findBySimulationId(simulationId)
        .map(SimulationEntity::toDetailsDto)
        .orElseThrow(() -> new SimulationNotFoundException(simulationId));
  }

  @Override
  public SimulationDetailsDto get(@NonNull String analysisName) {
    log.debug("Getting SimulationDetailsDto by analysisName={}", analysisName);

    return repository
        .findByAnalysisName(analysisName)
        .map(SimulationEntity::toDetailsDto)
        .orElseThrow(() -> new SimulationNotFoundException(analysisName));
  }

  @Override
  public Collection<String> getAnalysisNames(@NonNull Collection<String> datasetNames) {
    return repository.findAllAnalysisNamesByDatasetNames(datasetNames);
  }

  @Override
  public boolean isStreaming(@NonNull String analysis) {
    return repository
        .findByAnalysisName(analysis)
        .map(SimulationEntity::isStreaming)
        .orElseThrow(() -> new SimulationNotFoundException(analysis));
  }
}
