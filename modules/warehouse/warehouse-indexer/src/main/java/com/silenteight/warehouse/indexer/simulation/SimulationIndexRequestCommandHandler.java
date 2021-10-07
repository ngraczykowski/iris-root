package com.silenteight.warehouse.indexer.simulation;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;

public interface SimulationIndexRequestCommandHandler {

  DataIndexResponse handle(SimulationDataIndexRequest request);
}
