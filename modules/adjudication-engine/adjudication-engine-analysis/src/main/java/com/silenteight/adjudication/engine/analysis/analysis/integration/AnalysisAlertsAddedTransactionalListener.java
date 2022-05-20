package com.silenteight.adjudication.engine.analysis.analysis.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertsAddedGateway;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class AnalysisAlertsAddedTransactionalListener {

  private final AnalysisAlertsAddedGateway gateway;

  @TransactionalEventListener
  public void onAnalysisAlertsAdded(AnalysisAlertsAdded analysisAlertsAdded) {
    gateway.send(analysisAlertsAdded);
  }
}
