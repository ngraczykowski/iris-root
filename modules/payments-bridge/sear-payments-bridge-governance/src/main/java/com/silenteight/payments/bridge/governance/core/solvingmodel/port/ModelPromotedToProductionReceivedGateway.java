package com.silenteight.payments.bridge.governance.core.solvingmodel.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.ModelUpdated;

public interface ModelPromotedToProductionReceivedGateway {

  void send(ModelUpdated modelUpdated);
}
