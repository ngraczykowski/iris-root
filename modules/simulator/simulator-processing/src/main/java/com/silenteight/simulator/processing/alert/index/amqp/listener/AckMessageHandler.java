package com.silenteight.simulator.processing.alert.index.amqp.listener;

import com.silenteight.data.api.v1.DataIndexResponse;

public interface AckMessageHandler {

  void handle(DataIndexResponse request);
}
