package com.silenteight.serp.governance.vector.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.store.StoreFeatureVectorSolvedUseCase;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

@Slf4j
@RequiredArgsConstructor
public class FeatureVectorSolvedMessageHandler {

  @NonNull
  private final StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase;

  public void handle(FeatureVectorSolvedEvent event) {
    log.debug("Received FV event: {}", event);
    storeFeatureVectorSolvedUseCase.activate(event);
  }
}
