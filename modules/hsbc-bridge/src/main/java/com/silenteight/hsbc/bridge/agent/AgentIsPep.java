package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
class AgentIsPep {

  private final IsPepMessageSender messageSender;
  private final IsPepRequestCreator requestCreator;

  void send(Collection<AlertData> alerts) {
    var isPepData = requestCreator.create(alerts);

    messageSender.send(isPepData);
  }
}
