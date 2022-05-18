package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

public interface NotifyResponseCompletedPort {

  void notify(ResponseCompleted responseCompleted);

}
