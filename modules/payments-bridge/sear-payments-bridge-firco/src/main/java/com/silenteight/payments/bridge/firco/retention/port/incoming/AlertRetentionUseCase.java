package com.silenteight.payments.bridge.firco.retention.port.incoming;

import java.util.List;
import java.util.UUID;

public interface AlertRetentionUseCase {

  void invoke(List<UUID> alertsExpired);
}
