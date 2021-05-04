package com.silenteight.simulator.management.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.util.Collection;
import java.util.List;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.management.common.SimulationResource.toResourceName;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SimulationQuery implements ListSimulationsQuery {

  @NonNull
  private final SimulationEntityRepository simulationEntityRepository;
  @NonNull
  private final AnalysisService analysisService;

  @Override
  public List<SimulationDto> list() {
    return simulationEntityRepository
        .findAll()
        .stream()
        .map(this::toDto)
        .collect(toList());
  }

  private SimulationDto toDto(SimulationEntity simulationEntity) {
    // TODO(mmastylo): Uncomment when feature is available in AE
    // Analysis analysis = analysisService.getAnalysis(simulationEntity.getAnalysisName());
    return SimulationDto.builder()
        .id(simulationEntity.getSimulationId())
        .name(toResourceName(simulationEntity.getSimulationId()))
        .simulationName(simulationEntity.getName())
        .state(simulationEntity.getState())
        .datasets(simulationEntity.getDatasetNames())
        .model(simulationEntity.getModelName())
        .progressState(DONE.toString())
        .createdAt(simulationEntity.getCreatedAt())
        .createdBy(simulationEntity.getCreatedBy())
        .build();
  }

  @Override
  public List<SimulationDto> findByModel(@NonNull String model) {
    Collection<SimulationEntity> simulationEntities = simulationEntityRepository
        .findAllByModelName(model);
    if (simulationEntities.isEmpty()) {
      throw new InvalidModelNameException(model);
    }
    return simulationEntities
        .stream()
        .map(this::toDto)
        .collect(toList());
  }
}
