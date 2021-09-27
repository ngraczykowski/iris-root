package com.silenteight.payments.bridge.governance.core.solvingmodel.port;

import com.silenteight.model.api.v1.ModelPromotedForProduction;

public interface ReceiveCurrentModelUseCase {

  void handleModelPromotedForProductionMessage(
      ModelPromotedForProduction modelPromotedForProduction);
}
