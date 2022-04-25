package com.silenteight.warehouse.simulation.handler;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.SimulationDataIndexRequest;

public interface SimulationRequestV2CommandHandler {

  DataIndexResponse handle(SimulationDataIndexRequest request);
}
