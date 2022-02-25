package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchCreated;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.model.BatchTimedOut;

public interface BatchEventPublisher {

  void publish(BatchError event);

  void publish(BatchCompleted event);

  void publish(BatchCreated event);

  void publish(BatchTimedOut event);
}
