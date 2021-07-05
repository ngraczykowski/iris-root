package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

public interface IsPepApi {

  void send(Collection<AlertData> alerts);
}
