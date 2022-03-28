package com.silenteight.adjudication.engine.analysis.agentresponse.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFacade;
import com.silenteight.adjudication.engine.analysis.agentresponse.FeatureIdsProvider;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
class AgentExchangeRequestFeatureIdsProvider implements FeatureIdsProvider {

  private final AgentExchangeFacade facade;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    return facade.getFeatureToIdsMap(agentExchangeRequestId);
  }
}
