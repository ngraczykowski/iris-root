package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AgentApi;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
class AgentFacade implements AgentApi {

  private final AgentIsPep agentIsPep;
  private final AgentHistorical agentHistorical;

  @Override
  public void sendIsPep(Collection<AlertData> alerts) {
    agentIsPep.send(alerts);
  }

  @Override
  public void sendHistorical(Collection<AlertData> alerts) {
    agentHistorical.send(alerts);
  }
}
