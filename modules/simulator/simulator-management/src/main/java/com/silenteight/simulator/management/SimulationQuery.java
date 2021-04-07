package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.simulator.management.dto.SimulationDto;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SimulationQuery {

  @NonNull
  private final SimulationEntityRepository simulationEntityRepository;
  @NonNull
  private final AnalysisService analysisService;

  List<SimulationDto> list() {
    return simulationEntityRepository.findAll()
        .stream()
        .map(this::toDto)
        .collect(toList());
  }

  private SimulationDto toDto(SimulationEntity simulationEntity) {
    Analysis analysis = analysisService.getAnalysis(simulationEntity.getAnalysisName());
    return SimulationDto.builder()
        .id(simulationEntity.getSimulationId())
        .name(simulationEntity.getName())
        .state(simulationEntity.getState())
        .datasetNames(new ArrayList<>(simulationEntity.getDatasetNames()))
        .modelName(simulationEntity.getModelName())
        .progressState(analysis.getState().toString())
        .createdAt(simulationEntity.getCreatedAt())
        .createdBy(simulationEntity.getCreatedBy())
        .build();
  }
}
