package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class AgentHistorical {

  private final HistoricalDecisionMessageSender messageSender;
  private final HistoricalDecisionRequestCreator requestCreator;

  void send(Collection<AlertData> alerts) {
    var historicalDecisionData = requestCreator.create(alerts);

    log.info(
        "Sending learning data to Historical decisions model with alert count: {}",
        historicalDecisionData.getAlertsCount());
    messageSender.send(historicalDecisionData);
  }
}
