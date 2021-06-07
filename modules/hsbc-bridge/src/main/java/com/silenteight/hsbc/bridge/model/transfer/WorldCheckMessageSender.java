package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.worldcheck.api.v1.ModelPersisted;

public interface WorldCheckMessageSender {

  void send(ModelPersisted modelPersisted);
}
