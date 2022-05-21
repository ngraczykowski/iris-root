package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class AgentIsPep {

  private final IsPepMessageSender messageSender;
  private final IsPepRequestCreator requestCreator;

  void send(Collection<AlertData> alerts) {
    var isPepData = requestCreator.create(alerts);

    log.info("Sending learning data to IsPep with alert count: {}", isPepData.getAlertsCount());
    messageSender.send(isPepData);
  }
}
