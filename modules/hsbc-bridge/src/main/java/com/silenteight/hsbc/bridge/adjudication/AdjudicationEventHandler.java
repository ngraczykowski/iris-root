package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class AdjudicationEventHandler {

  private final AlertService alertService;
  private final DatasetServiceApi datasetServiceApi;
  private final AnalysisService analysisService;

  @EventListener
  @Async
  public void onBulkPreProcessingFinishedEvent(BulkPreProcessingFinishedEvent event) {
    log.debug("Received bulkPreProcessingFinishedEvent");

    createAnalysis(event.getAlertMatchIdComposites());
  }

  private void createAnalysis(Collection<AlertMatchIdComposite> alertMatchIdComposites) {
    var alertIdCompositeMap = toCompositeByAlertId(alertMatchIdComposites);

    alertService.registerAlertsWithMatches(alertIdCompositeMap);
    var datasetName = createDataset(alertIdCompositeMap.keySet());
    analysisService.createAnalysisWithDataset(datasetName);

    log.debug("Analysis created");
  }

  private String createDataset(Collection<String> alertIds) {
    return datasetServiceApi.createDataset(alertIds).getName();
  }

  private Map<String, AlertMatchIdComposite> toCompositeByAlertId(
      Collection<AlertMatchIdComposite> alertMatchIds) {
    return alertMatchIds
        .stream()
        .collect(Collectors.toMap(AlertMatchIdComposite::getAlertExternalId, Function.identity()));
  }
}
