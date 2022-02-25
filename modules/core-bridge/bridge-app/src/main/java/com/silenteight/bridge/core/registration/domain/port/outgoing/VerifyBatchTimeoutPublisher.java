package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.VerifyBatchTimeoutEvent;

public interface VerifyBatchTimeoutPublisher {

  void publish(VerifyBatchTimeoutEvent event);
}
