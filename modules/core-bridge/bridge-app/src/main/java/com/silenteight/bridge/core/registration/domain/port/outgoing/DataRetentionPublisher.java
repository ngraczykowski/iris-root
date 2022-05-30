package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent;

public interface DataRetentionPublisher {

  void publish(DataRetentionAlertsExpiredEvent event);

}
