package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;

import com.silenteight.data.api.v2.DataIndexResponse;

public interface AckMessageHandler {

  void handle(@NonNull DataIndexResponse request);
}
