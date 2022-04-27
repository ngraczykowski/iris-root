package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class PendingAlertsSolvingStrategy implements PendingAlertsStrategy {

  private final AlertRepository alertRepository;

  @Override
  public BatchStrategyName getStrategyName() {
    return BatchStrategyName.SOLVING;
  }

  @Override
  public boolean hasNoPendingAlerts(Batch batch) {
    var completedAlertsCount = alertRepository.countAllCompleted(batch.id());
    log.info(
        "[{}] alerts completed ([{}], [{}], [{}]), [{}] alerts count for batch with id [{}].",
        completedAlertsCount, AlertStatus.RECOMMENDED, AlertStatus.ERROR, AlertStatus.DELIVERED,
        batch.alertsCount(), batch.id());

    return batch.alertsCount() == completedAlertsCount;
  }
}
