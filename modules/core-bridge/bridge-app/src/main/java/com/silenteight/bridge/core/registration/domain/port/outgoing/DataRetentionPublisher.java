package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionPersonalInformationExpiredEvent;

public interface DataRetentionPublisher {

  void publish(DataRetentionAlertsExpiredEvent event);

  void publish(DataRetentionPersonalInformationExpiredEvent event);
}
