package com.silenteight.hsbc.bridge.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.IsPepApi;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

@RequiredArgsConstructor
class IsPep implements IsPepApi {

  private final IsPepMessageSender messageSender;

  @Override
  public void send(Collection<AlertData> alerts) {
    var data = LearningStoreExchangeRequestCreator.create(alerts);
    messageSender.send(data);
  }
}
