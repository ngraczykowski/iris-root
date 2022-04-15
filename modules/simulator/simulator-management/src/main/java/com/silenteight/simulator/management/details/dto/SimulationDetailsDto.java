package com.silenteight.simulator.management.details.dto;

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
public class SimulationDetailsDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String description;
  @NonNull
  String simulationName;
  @NonNull
  SimulationState state;
  @NonNull
  Set<String> datasets;
  @NonNull
  String model;
  @NonNull
  String analysis;
  @NonNull
  String createdBy;
  long solvedAlerts;
  @NonNull
  OffsetDateTime createdAt;
  OffsetDateTime updatedAt;
  OffsetDateTime finishedAt;

  public static SimulationDetailsDto of(SimulationDto simulationDto) {
    return SimulationDetailsDto.builder()
        .id(simulationDto.getId())
        .name(simulationDto.getName())
        .description(simulationDto.getDescription())
        .simulationName(simulationDto.getSimulationName())
        .state(simulationDto.getState())
        .datasets(simulationDto.getDatasets())
        .model(simulationDto.getModel())
        .analysis(simulationDto.getAnalysis())
        .solvedAlerts(simulationDto.getSolvedAlerts())
        .createdBy(simulationDto.getCreatedBy())
        .createdAt(simulationDto.getCreatedAt())
        .updatedAt(simulationDto.getUpdatedAt())
        .finishedAt(simulationDto.getFinishedAt())
        .build();
  }
}
