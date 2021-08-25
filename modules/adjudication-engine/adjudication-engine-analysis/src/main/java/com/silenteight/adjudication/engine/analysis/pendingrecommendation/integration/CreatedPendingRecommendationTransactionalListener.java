package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listens for {@link PendingRecommendations} application event and passes it to the gateway.
 * <p/>
 * The gateway couldn't be used directly, as the event listener is executed after successful
 * commit.
 */
@RequiredArgsConstructor
@Component
class CreatedPendingRecommendationTransactionalListener {

  private final PendingRecommendationGateway
      gateway;

  @TransactionalEventListener(PendingRecommendations.class)
  public void onAddedAnalysisAlerts(PendingRecommendations event) {
    gateway.send(event);
  }
}
