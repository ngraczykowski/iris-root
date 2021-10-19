package com.silenteight.payments.bridge.governance.core.solvingmodel.port;

import com.silenteight.model.api.v1.SolvingModel;

public interface ReceiveCurrentModelUseCase {

  void handleModelPromotedForProductionMessage(
      SolvingModel modelPromotedForProduction);
}
