package com.silenteight.payments.bridge.firco.callback.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

public interface SendResponseUseCase {

  void send(ResponseCompleted message);

}
