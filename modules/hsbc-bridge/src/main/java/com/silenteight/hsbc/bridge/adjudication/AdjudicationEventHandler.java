package com.silenteight.hsbc.bridge.adjudication;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@Slf4j
class AdjudicationEventHandler {

  private final AlertService alertService;
  private final DatasetServiceApi datasetServiceApi;
  private final AnalysisService analysisService;
  @Getter(AccessLevel.PROTECTED)
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  @Async
  public void onBulkPreProcessingFinishedEvent(BulkPreProcessingFinishedEvent event) {
    log.debug("Received bulkPreProcessingFinishedEvent, bulkId={}", event.getBulkId());

    var alertIdCompositeMap = toCompositeByAlertId(event.getAlertMatchIdComposites());

    try {
      registerAlertsAndCreateAnalysis(alertIdCompositeMap);
    } catch (RuntimeException exception) {
      handleRuntimeException(event, exception);
    }
  }

  private void handleRuntimeException(BulkPreProcessingFinishedEvent event, RuntimeException exception) {
    var bulkId = event.getBulkId();
    log.error("Error on handling adjudication event, bulkId={}", bulkId, exception);

    eventPublisher.publishEvent(new AdjudicateFailedEvent(bulkId, exception.getMessage()));
  }

  private void registerAlertsAndCreateAnalysis(Map<String, AlertMatchIdComposite> alertMatchIds) {
    var alerts = alertService.registerAlertsWithMatches(alertMatchIds);
    var datasetName = datasetServiceApi.createDataset(alerts);
    analysisService.createAnalysisWithDataset(datasetName);
  }

  private Map<String, AlertMatchIdComposite> toCompositeByAlertId(
      Collection<AlertMatchIdComposite> alertMatchIds) {
    return alertMatchIds
        .stream()
        .collect(Collectors.toMap(AlertMatchIdComposite::getAlertExternalId, Function.identity()));
  }
}
