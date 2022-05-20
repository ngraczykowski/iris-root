package com.silenteight.hsbc.bridge.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;

import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
@Slf4j
class AnalysisStatusHandler {

  private final AnalysisStatusCalculator statusCalculator;
  private final ApplicationEventPublisher eventPublisher;

  void handleStatusChange(@NonNull String name) {
    log.info("Recalculating analysis status, analysis={}", name);

    statusCalculator.recalculateStatus(name)
        .ifPresent(newStatus -> handleNewStatus(name, newStatus));
  }

  private void handleNewStatus(String name, Status newStatus) {
    if (newStatus == Status.COMPLETED) {
      eventPublisher.publishEvent(new AnalysisCompletedEvent(name));
      log.info("Analysis: {} has been completed", name);
    }
  }
}
