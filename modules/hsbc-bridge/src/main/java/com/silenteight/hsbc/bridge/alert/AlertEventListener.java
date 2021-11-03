package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertsPreProcessingCompletedEvent;
import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;
import com.silenteight.hsbc.bridge.analysis.event.RecalculateAnalysisStatusEvent;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationsStoredEvent;
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class AlertEventListener {

  private final AlertProcessor alertProcessor;
  private final AlertUpdater updater;
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void onUpdateAlertEventWithNameEvent(UpdateAlertWithNameEvent event) {
    log.trace("Received updateAlertEventWithNameEvent, {}", event.getAlertIdToName());

    updater.updateNames(event.getAlertIdToName());
  }

  @EventListener
  public void onAlertRecommendationReadyEvent(RecommendationsGeneratedEvent event) {
    var alerts = event.getAlertRecommendationInfos().stream()
        .map(AlertRecommendationInfo::getAlert)
        .collect(toList());

    log.info("Received Recommendations analysis={}, no of alerts={}", event.getAnalysis(), alerts.size());

    updater.updateWithRecommendationReadyStatus(alerts);
    eventPublisher.publishEvent(new RecalculateAnalysisStatusEvent(event.getAnalysis()));
  }

  @EventListener
  public void onAlertRecommendationsStoredEvent(AlertRecommendationsStoredEvent event) {
    log.debug("Received alertRecommendationStoreEvent");

    updater.updateWithCompletedStatus(event.getAlerts());
  }

  @EventListener
  @Async
  public void onBulkStoredEvent(BulkStoredEvent event) {
    var bulkId = event.getBulkId();

    if (event.isLearning()) {
      alertProcessor.preProcessAlertsWithinLearningBulk(bulkId);
    } else {
      alertProcessor.preProcessAlertsWithinSolvingBulk(bulkId);
    }
    eventPublisher.publishEvent(new AlertsPreProcessingCompletedEvent(bulkId));

    log.debug("BatchStoredEvent handled, batchId={}", bulkId);
  }
}
