package com.silenteight.simulator.processing.alert.index.amqp.gateway;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

public interface SimulationDataIndexRequestGateway {

  void send(SimulationDataIndexRequest request);
}
