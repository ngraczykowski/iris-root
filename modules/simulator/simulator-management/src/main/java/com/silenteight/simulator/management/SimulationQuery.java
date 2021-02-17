package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.dto.SimulationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SimulationQuery {

  @NonNull
  private final SimulationEntityRepository simulationEntityRepository;

  List<SimulationDto> listAllSimulations() {
    return simulationEntityRepository.findAll()
        .stream()
        .map(SimulationEntity::toDto)
        .collect(toList());
  }
}
