package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class TimeoutHandler {

  private final AnalysisServiceClient analysisServiceClient;
  private final AnalysisRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Scheduled(fixedRate = 60 * 1000, initialDelay = 2000)
  @Transactional
  void process() {
    findInProgressTimeoutAnalyses().forEach(this::tryToHandleAnalysis);
  }

  private void tryToHandleAnalysis(AnalysisEntity entity) {
    try {
      handleAnalysis(entity);
    } catch (RuntimeException ex) {
      log.error("Error on handling analysis timeout", ex);
      handleTimeoutError(entity);
    }

    repository.save(entity);
  }

  private void handleAnalysis(AnalysisEntity entity) {
    if (hasZeroCompletedAlerts(entity.getName())) {
      handleTimeoutError(entity);
    } else {
      handleCompletedAnalysis(entity);
    }
  }

  private boolean hasZeroCompletedAlerts(String analysisId) {
    return analysisServiceClient.getAnalysis(analysisId).hasZeroAlertsCompleted();
  }

  private void handleCompletedAnalysis(AnalysisEntity entity) {
    entity.setStatus(Status.COMPLETED);
    eventPublisher.publishEvent(new AnalysisCompletedEvent(entity.getName()));
  }

  private void handleTimeoutError(AnalysisEntity entity) {
    entity.setStatus(Status.TIMEOUT_ERROR);
    log.error("Analysis with id: {} and name: {} has timed out.", entity.getId(), entity.getName());
    eventPublisher.publishEvent(new AnalysisTimeoutEvent(entity.getId()));
  }

  private List<AnalysisEntity> findInProgressTimeoutAnalyses() {
    return repository.findByTimeoutAtBeforeAndStatus(OffsetDateTime.now(), Status.IN_PROGRESS);
  }
}
