package com.silenteight.simulator.management.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.State;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.management.common.SimulationResource.toResourceName;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SimulationQuery implements ListSimulationsQuery, SimulationDetailsQuery {

  @NonNull
  private final SimulationEntityRepository repository;

  @NonNull
  private final AnalysisService analysisService;

  @Override
  public List<SimulationDto> list() {
    return repository
        .findAll()
        .stream()
        .map(this::makePair)
        .map(this::toDto)
        .collect(toList());
  }

  private SimulationDto toDto(SimulationStatePair pair) {
    return SimulationDto.builder()
        .id(pair.simulationId())
        .name(toResourceName(pair.simulationId()))
        .simulationName(pair.simulationName())
        .state(pair.simulationState())
        .datasets(pair.datasetNames())
        .model(pair.modelName())
        .progressState(pair.progressState())
        .createdAt(pair.createdAt())
        .createdBy(pair.createdBy())
        .build();
  }

  @Override
  public List<SimulationDto> findByModel(@NonNull String model) {
    Collection<SimulationEntity> simulationEntities = repository.findAllByModelName(model);

    if (simulationEntities.isEmpty())
      throw new InvalidModelNameException(model);

    return simulationEntities
        .stream()
        .map(this::makePair)
        .map(this::toDto)
        .collect(toList());
  }

  @Override
  public SimulationDetailsDto get(@NonNull UUID simulationId) {
    SimulationEntity simulationEntity = repository
        .findBySimulationId(simulationId)
        .orElseThrow(() -> new SimulationNotFoundException(simulationId));
    Analysis analysis = analysisService.getAnalysis(simulationEntity.getAnalysisName());
    return toDetailsDto(simulationEntity, analysis.getState());
  }

  private SimulationDetailsDto toDetailsDto(
      SimulationEntity simulationEntity, State progressState) {

    return SimulationDetailsDto.builder()
        .id(simulationEntity.getSimulationId())
        .name(toResourceName(simulationEntity.getSimulationId()))
        .description(simulationEntity.getDescription())
        .simulationName(simulationEntity.getName())
        .state(simulationEntity.getState())
        .datasets(simulationEntity.getDatasetNames())
        .model(simulationEntity.getModelName())
        .progressState(progressState.toString())
        .createdAt(simulationEntity.getCreatedAt())
        .createdBy(simulationEntity.getCreatedBy())
        .build();
  }

  private SimulationStatePair makePair(SimulationEntity simulationEntity) {
    Analysis analysis = analysisService.getAnalysis(simulationEntity.getAnalysisName());
    return new SimulationStatePair(simulationEntity, analysis.getState());
  }

  @Value
  private static class SimulationStatePair {

    @NonNull
    SimulationEntity simulation;
    @NonNull
    State state;

    UUID simulationId() {
      return simulation.getSimulationId();
    }

    String simulationName() {
      return simulation.getName();
    }

    SimulationState simulationState() {
      return simulation.getState();
    }

    Set<String> datasetNames() {
      return simulation.getDatasetNames();
    }

    String modelName() {
      return simulation.getModelName();
    }

    String progressState() {
      return state.toString();
    }

    OffsetDateTime createdAt() {
      return simulation.getCreatedAt();
    }

    String createdBy() {
      return simulation.getCreatedBy();
    }
  }
}
