package com.silenteight.searpayments.bridge.ae.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.bridge.ae.AlertFacade;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AlertReadyToSolveIntegrationFlow extends IntegrationFlowAdapter {

  private final AlertFacade alertFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from("alertReadyForCallbackChannel")
        .handle(this::handleReadyAlert);
  }

  private int handleReadyAlert(long alertId, MessageHeaders mh) {
    alertFacade.solveAlert(alertId);
    return 0;
  }
}
