package com.silenteight.hsbc.bridge.adjudication;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.AnalysisFacade;
import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;
import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.bulk.event.BulkProcessingStartedEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Builder
@Slf4j
class AdjudicationEventHandler {

  private final AlertService alertService;
  private final DatasetServiceClient datasetServiceClient;
  private final AnalysisFacade analysisFacade;
  @Getter(AccessLevel.PROTECTED)
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void onBulkPreProcessingFinishedEvent(BulkPreProcessingFinishedEvent event) {
    var bulkId = event.getBulkId();
    log.debug("Received bulkPreProcessingFinishedEvent, bulkId={}", bulkId);

    var alertIdCompositeMap = toCompositeByAlertId(event.getAlertMatchIdComposites());

    try {
      var alerts = alertService.registerAlertsWithMatches(alertIdCompositeMap);
      var analysis = adjudicateAlerts(alerts);

      eventPublisher.publishEvent(new BulkProcessingStartedEvent(bulkId, analysis.getId()));
    } catch (RuntimeException exception) {
      handleRuntimeException(bulkId, exception);
    }
  }

  private void handleRuntimeException(String bulkId, RuntimeException exception) {
    log.error("Error on handling adjudication event, bulkId={}", bulkId, exception);

    eventPublisher.publishEvent(new AdjudicateFailedEvent(bulkId, exception.getMessage()));
  }

  private AnalysisDto adjudicateAlerts(Collection<String> alerts) {
    var datasetName = datasetServiceClient.createDataset(alerts);
    return analysisFacade.createAnalysisWithDataset(datasetName);
  }

  private Map<String, AlertMatchIdComposite> toCompositeByAlertId(
      Collection<AlertMatchIdComposite> alertMatchIds) {
    return alertMatchIds.stream()
        .collect(toMap(AlertMatchIdComposite::getAlertExternalId, identity()));
  }
}
