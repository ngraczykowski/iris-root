package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AgentExchangeFacade {

  private final RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;

  public void requestMissingFeatureValues(PendingRecommendations pendingRecommendations) {
    requestMissingFeatureValuesUseCase.requestMissingFeatureValues(pendingRecommendations);
  }
}
