package com.silenteight.adjudication.engine.analysis.agentresponse.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFacade;
import com.silenteight.adjudication.engine.analysis.agentresponse.FeatureIdsProvider;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
class AgentExchangeRequestFeatureIdsProvider implements FeatureIdsProvider {

  private final AgentExchangeFacade facade;

  @Override
  public Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    return facade.getFeatureToIdsMap(agentExchangeRequestId);
  }
}
