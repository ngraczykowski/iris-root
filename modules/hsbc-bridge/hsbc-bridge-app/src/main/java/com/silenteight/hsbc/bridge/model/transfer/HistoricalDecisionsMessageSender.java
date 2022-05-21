package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.proto.historicaldecisions.model.v1.api.ModelPersisted;

public interface HistoricalDecisionsMessageSender {

  void send(ModelPersisted modelPersisted);
}
