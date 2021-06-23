package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertsPreProcessingCompletedEvent;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationsStoredEvent;
import com.silenteight.hsbc.bridge.recommendation.event.FailedToGetRecommendationsEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class BulkEventListener {

  private final BulkUpdater bulkUpdater;

  @EventListener
  public void onAnalysisTimeoutEvent(AnalysisTimeoutEvent event) {
    bulkUpdater.updateWithAnalysisTimeout(event.getAnalysisId());

    log.debug("AnalysisTimeoutEvent handled, analysisId={}", event.getAnalysisId());
  }

  @EventListener
  public void onAlertRecommendationsStoredEvent(AlertRecommendationsStoredEvent event) {
    bulkUpdater.updateWithCompletedStatus(event.getAnalysis());

    log.debug("AlertRecommendationsStoredEvent handled, analysis={}", event.getAnalysis());
  }

  @EventListener
  public void onAlertsPreProcessingCompletedEvent(AlertsPreProcessingCompletedEvent event) {
    bulkUpdater.updateWithPreProcessedStatus(event.getBulkId());

    log.debug("AlertsPreProcessingCompletedEvent handled, bulkId={}", event.getBulkId());
  }

  @EventListener
  public void onFailedToGetRecommendationsEvent(FailedToGetRecommendationsEvent event) {
    bulkUpdater.updateWithUnavailableRecommendation(event.getAnalysis());

    log.debug("FailedToGetRecommendationsEvent handled, analysis={}", event.getAnalysis());
  }
}
