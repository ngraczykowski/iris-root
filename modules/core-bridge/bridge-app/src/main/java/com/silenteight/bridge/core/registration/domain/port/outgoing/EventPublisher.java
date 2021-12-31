package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.BatchError;

public interface EventPublisher {

  void publish(BatchError event);
}
