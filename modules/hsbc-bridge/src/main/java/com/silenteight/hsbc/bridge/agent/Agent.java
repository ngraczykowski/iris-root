package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AgentApi;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
class Agent implements AgentApi {

  private final IsPepMessageSender isPepMessageSender;
  private final HistoricalDecisionMessageSender historicalMessageSender;

  @Override
  public void send(Collection<AlertData> alerts) {
    var isPepData = LearningStoreExchangeRequestCreator.create(alerts);
    var historicalDecisionData = HistoricalDecisionRequestCreator.create(alerts);

    isPepMessageSender.send(isPepData);
    historicalMessageSender.send(historicalDecisionData);
  }
}
