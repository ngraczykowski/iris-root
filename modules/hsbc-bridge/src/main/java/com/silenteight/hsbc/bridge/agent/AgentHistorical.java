package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
class AgentHistorical {

  private final HistoricalDecisionMessageSender messageSender;
  private final HistoricalDecisionRequestCreator requestCreator;

  void send(Collection<AlertData> alerts) {
    var historicalDecisionData = requestCreator.create(alerts);

    messageSender.send(historicalDecisionData);
  }
}
