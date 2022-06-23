package com.silenteight.serp.governance.vector.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.store.StoreFeatureVectorSolvedUseCase;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

@Slf4j
@RequiredArgsConstructor
public class FeatureVectorSolvedMessageHandler {

  @NonNull
  private final StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase;

  public void handle(FeatureVectorSolvedEventBatch event) {
    log.debug("Received a list of FV {} events.", event.getEventsCount());
    storeFeatureVectorSolvedUseCase.activate(event.getEventsList());
  }
}
