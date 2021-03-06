package com.silenteight.serp.governance.model.used.amqp;

import com.silenteight.model.api.v1.SolvingModel;

public interface ModelUsedOnProductionMessageGateway {

  String ID = "modelUsedOnProductionMessageGateway";

  void send(SolvingModel message);
}
