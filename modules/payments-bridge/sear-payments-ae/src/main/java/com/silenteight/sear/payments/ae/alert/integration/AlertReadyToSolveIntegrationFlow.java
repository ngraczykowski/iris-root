package com.silenteight.sear.payments.ae.alert.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sear.payments.ae.alert.AlertFacade;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertReadyToSolveIntegrationFlow extends IntegrationFlowAdapter {

  private final AlertFacade alertFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from("alertReadyForCallbackChannel")
        .handle(this::handleReadyAlert);
  }

  private int handleReadyAlert(long alertId, MessageHeaders mh) {
    alertFacade.soleAlert(alertId);
    return 0;
  }
}
