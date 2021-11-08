package com.silenteight.simulator.management.list;

import lombok.NonNull;

import com.silenteight.simulator.management.list.dto.SimulationDto;

import java.util.Collection;
import java.util.List;

public interface ListSimulationsQuery {

  List<SimulationDto> list();

  List<SimulationDto> findByModel(@NonNull String modelName);

  Collection<String> getAnalysisNames(@NonNull Collection<String> datasetNames);
}
