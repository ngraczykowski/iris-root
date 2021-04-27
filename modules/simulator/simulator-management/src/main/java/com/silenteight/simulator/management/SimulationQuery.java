package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.dto.SimulationDto;

import java.util.List;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.management.common.SimulationResource.toResourceName;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SimulationQuery {

  @NonNull
  private final SimulationEntityRepository simulationEntityRepository;
  @NonNull
  private final AnalysisService analysisService;

  List<SimulationDto> list() {
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
        .datasetNames(simulationEntity.getDatasetNames())
        .modelName(simulationEntity.getModelName())
        .progressState(DONE.toString())
        .createdAt(simulationEntity.getCreatedAt())
        .createdBy(simulationEntity.getCreatedBy())
        .build();
  }
}
