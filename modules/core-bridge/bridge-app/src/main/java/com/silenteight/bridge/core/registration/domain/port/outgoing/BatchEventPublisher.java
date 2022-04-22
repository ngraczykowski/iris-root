package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.*;

public interface BatchEventPublisher {

  void publish(BatchError event);

  void publish(SolvingBatchCompleted event);

  void publish(SimulationBatchCompleted event);

  void publish(BatchTimedOut event);

  void publish(BatchDelivered event);
}
