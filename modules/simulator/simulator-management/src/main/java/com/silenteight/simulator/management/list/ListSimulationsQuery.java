package com.silenteight.simulator.management.list;

import lombok.NonNull;

import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.util.Collection;
import java.util.List;

public interface ListSimulationsQuery {

  List<SimulationDto> list(Collection<SimulationState> states);

  List<SimulationDto> findByModel(@NonNull String modelName);

  List<SimulationDto> findByModels(@NonNull Collection<String> modelNames);

  Collection<String> getAnalysisNames(@NonNull Collection<String> datasetNames);
}
