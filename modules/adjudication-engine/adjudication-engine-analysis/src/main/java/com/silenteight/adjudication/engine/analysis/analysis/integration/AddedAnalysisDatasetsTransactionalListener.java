package com.silenteight.adjudication.engine.analysis.analysis.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listens for {@link AddedAnalysisDatasets} application event and passes it to the gateway.
 * <p/>
 * The gateway couldn't be used directly, as the event listener is executed after successful commit.
 */
@RequiredArgsConstructor
@Component
class AddedAnalysisDatasetsTransactionalListener {

  private final AddedAnalysisDatasetsGateway gateway;

  @TransactionalEventListener(AddedAnalysisDatasets.class)
  public void onAddedAnalysisDatasets(AddedAnalysisDatasets event) {
    gateway.send(event);
  }
}
