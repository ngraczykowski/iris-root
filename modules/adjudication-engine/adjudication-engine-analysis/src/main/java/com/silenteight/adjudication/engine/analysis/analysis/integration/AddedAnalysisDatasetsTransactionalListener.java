package com.silenteight.adjudication.engine.analysis.analysis.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listens for {@link AddedAnalysisAlerts} application event and passes it to the gateway.
 * <p/>
 * The gateway couldn't be used directly, as the event listener is executed after successful
 * commit.
 */
@RequiredArgsConstructor
@Component
class AddedAnalysisDatasetsTransactionalListener {

  private final AddedAnalysisAlertsGateway gateway;

  @TransactionalEventListener(AddedAnalysisAlerts.class)
  public void onAddedAnalysisAlerts(AddedAnalysisAlerts event) {
    gateway.send(event);
  }
}
