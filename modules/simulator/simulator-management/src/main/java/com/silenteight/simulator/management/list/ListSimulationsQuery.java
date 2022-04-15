package com.silenteight.simulator.management.list;

import lombok.NonNull;

import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.domain.dto.SimulationDto;
import com.silenteight.simulator.management.list.dto.SimulationListDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

public interface ListSimulationsQuery {

  default List<SimulationListDto> list(Collection<SimulationState> states) {
    return listDomainDto(states)
        .stream()
        .map(SimulationListDto::toSimulationListDto)
        .collect(toList());
  }

  List<SimulationDto> listDomainDto(Collection<SimulationState> states);

  default List<SimulationListDto> findByModels(@NonNull Collection<String> modelNames) {
    return findDomainDtoByModels(modelNames)
        .stream()
        .map(SimulationListDto::toSimulationListDto)
        .collect(toList());
  }

  List<SimulationDto> findDomainDtoByModels(@NonNull Collection<String> modelNames);

  Collection<String> getAnalysisNames(@NonNull Collection<String> datasetNames);
}
