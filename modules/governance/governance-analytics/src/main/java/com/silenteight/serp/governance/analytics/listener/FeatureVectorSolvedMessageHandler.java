package com.silenteight.serp.governance.analytics.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.governance.api.v1.FeatureVectorSolvedEvent;
import com.silenteight.serp.governance.analytics.StoreFeatureVectorSolvedUseCase;

import org.springframework.messaging.MessagingException;

@Slf4j
@RequiredArgsConstructor
public class FeatureVectorSolvedMessageHandler {

  @NonNull
  private final StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase;

  public void handle(FeatureVectorSolvedEvent event) throws MessagingException {
    storeFeatureVectorSolvedUseCase.activate(event);
  }
}
