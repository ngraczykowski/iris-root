package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AgentExchangeFacade {

  private final RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;

  public void requestMissingFeatureValues(String analysis) {
    requestMissingFeatureValuesUseCase.requestMissingFeatureValues(analysis);
  }
}
