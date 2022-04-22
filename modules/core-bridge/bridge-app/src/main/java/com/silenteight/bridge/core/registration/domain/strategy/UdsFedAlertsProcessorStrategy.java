package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.model.Batch;

import java.util.List;

public interface UdsFedAlertsProcessorStrategy extends BatchStrategyNameProvider {

  void processUdsFedAlerts(Batch batch, List<String> alertNames);
}
