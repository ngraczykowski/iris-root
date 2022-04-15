package com.silenteight.simulator.management.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.domain.dto.SimulationDto;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SimulationListDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String simulationName;
  @NonNull
  SimulationState state;
  @NonNull
  Set<String> datasets;
  @NonNull
  String model;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;
  OffsetDateTime updatedAt;
  OffsetDateTime finishedAt;

  public static SimulationListDto toSimulationListDto(SimulationDto simulationDto) {
    return SimulationListDto.builder()
        .id(simulationDto.getId())
        .name(simulationDto.getName())
        .simulationName(simulationDto.getSimulationName())
        .state(simulationDto.getState())
        .datasets(simulationDto.getDatasets())
        .model(simulationDto.getModel())
        .createdBy(simulationDto.getCreatedBy())
        .createdAt(simulationDto.getCreatedAt())
        .updatedAt(simulationDto.getUpdatedAt())
        .finishedAt(simulationDto.getFinishedAt())
        .build();
  }
}
