package com.silenteight.hsbc.bridge.report;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface WarehouseMessageSender {

  void send(ProductionDataIndexRequest dataIndexRequest);
}
