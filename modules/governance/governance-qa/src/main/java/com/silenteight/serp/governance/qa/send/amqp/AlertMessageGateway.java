package com.silenteight.serp.governance.qa.send.amqp;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface AlertMessageGateway {

  void send(ProductionDataIndexRequest message);
}
