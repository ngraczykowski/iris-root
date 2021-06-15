package com.silenteight.serp.governance.analytics.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.analytics.StoreFeatureVectorSolvedUseCase;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

import org.springframework.messaging.MessagingException;

@Slf4j
@RequiredArgsConstructor
public class FeatureVectorSolvedMessageHandler {

  @NonNull
  private final StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase;

  public void handle(FeatureVectorSolvedEvent event) throws MessagingException {
    log.debug("Received FV event: {}", event);
    storeFeatureVectorSolvedUseCase.activate(event);
  }
}
