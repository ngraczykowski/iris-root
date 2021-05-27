package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgentExchangeFacade {

  private final RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;
  private final GetFeatureToIdsMapUseCase getFeatureToIdsMapUseCase;

  public void requestMissingFeatureValues(String analysis) {
    requestMissingFeatureValuesUseCase.requestMissingFeatureValues(analysis);
  }

  public Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    return getFeatureToIdsMapUseCase.getFeatureToIdsMap(agentExchangeRequestId);
  }
}
