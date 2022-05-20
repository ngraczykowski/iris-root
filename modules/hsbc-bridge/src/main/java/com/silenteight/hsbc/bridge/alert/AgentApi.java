package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import java.util.Collection;

public interface AgentApi {

  void sendIsPep(Collection<AlertData> alerts);

  void sendHistorical(Collection<AlertData> alerts);
}
