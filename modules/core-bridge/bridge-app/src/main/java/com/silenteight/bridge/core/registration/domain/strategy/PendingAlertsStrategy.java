package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.model.Batch;

public interface PendingAlertsStrategy extends BatchStrategyNameProvider {

  boolean hasNoPendingAlerts(Batch batch);
}
