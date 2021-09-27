package com.silenteight.payments.bridge.governance.core.solvingmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelPromotedForProduction;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.ModelPromotedToProductionReceivedGateway;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.ReceiveCurrentModelUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ModelUpdated;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class ReceiveCurrentModelService implements ReceiveCurrentModelUseCase {

  private final ModelPromotedToProductionReceivedGateway modelPromotedToProductionReceivedGateway;

  @Override
  public void handleModelPromotedForProductionMessage(
      ModelPromotedForProduction modelPromotedForProduction) {

    ModelUpdated modelUpdated = ModelUpdated.newBuilder().build();
    modelPromotedToProductionReceivedGateway.send(modelUpdated);
  }
}
