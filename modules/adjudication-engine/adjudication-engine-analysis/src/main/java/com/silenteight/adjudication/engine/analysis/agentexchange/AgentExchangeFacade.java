package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.DeleteAgentExchangeRequest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgentExchangeFacade {

  private final RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;
  private final GetFeatureToIdsMapUseCase getFeatureToIdsMapUseCase;
  private final RemoveAgentExchangesUseCase removeAgentExchangesUseCase;

  public void requestMissingFeatureValues(String analysis) {
    requestMissingFeatureValuesUseCase.requestMissingFeatureValues(analysis);
  }

  public Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    return getFeatureToIdsMapUseCase.getFeatureToIdsMap(agentExchangeRequestId);
  }

  public void removeReceivedAgentExchanges(
      List<DeleteAgentExchangeRequest> deleteAgentExchangeRequests) {
    removeAgentExchangesUseCase.removeAgentExchanges(deleteAgentExchangeRequests);
  }
}
