package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.data.api.v2.SimulationDataIndexRequest;

public interface SimulationV2IndexClientGateway {

  void indexRequest(SimulationDataIndexRequest dataIndexRequest);
}
