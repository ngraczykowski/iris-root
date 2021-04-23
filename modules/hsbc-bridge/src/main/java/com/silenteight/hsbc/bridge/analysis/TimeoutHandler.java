package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class TimeoutHandler {
  
  private final AnalysisServiceApi analysisServiceApi;
  private final AnalysisRepository repository;

  @Scheduled(fixedRate = 60 * 1000, initialDelay = 2000)
  @Transactional
  void process() {
    findInProgressAnalysisAfterTimeout().forEach(this::updateStatusOfAnalysis);
  }

  private void updateStatusOfAnalysis(AnalysisEntity entity) {
    entity.setStatus(hasPendingAlerts(entity.getName()) ? Status.ERROR : Status.COMPLETED);
    repository.save(entity);
  }

  private List<AnalysisEntity> findInProgressAnalysisAfterTimeout() {
    return repository.findByTimeoutAtBeforeAndStatus(OffsetDateTime.now(), Status.IN_PROGRESS);
  }

  private boolean hasPendingAlerts(String analysis) {
    return analysisServiceApi.getAnalysis(analysis).hasPendingAlerts();
  }
}
