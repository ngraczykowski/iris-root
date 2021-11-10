package com.silenteight.warehouse.retention.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.simulation.analysis.SimulationNamingStrategy;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.partition;

@RequiredArgsConstructor
@Slf4j
class RetentionSimulationUseCase {

  private static final String ERASED_VALUE = "";

  @NonNull
  private final AlertIndexService alertIndexService;
  @NonNull
  private final SimulationNamingStrategy simulationNamingStrategy;
  @NonNull
  private final AllDocumentsInIndexProcessor allDocumentsInIndexProcessor;
  @NonNull
  private final RetentionSimulationProperties retentionSimulationProperties;
  private final int updateBatchSize;

  public void activate(AnalysisExpired analysisExpired) {
    doLogging(analysisExpired);

    analysisExpired
        .getAnalysisList()
        .stream()
        .map(simulationNamingStrategy::getElasticIndexNameForAnalysisResource)
        .peek(indexName -> log.info("Clear PII data from '{}' index.", indexName))
        .forEach(index -> allDocumentsInIndexProcessor.processAllAlertsIds(
            index, documentIds -> erasePiiData(documentIds, index, createPayload())));
  }

  private void doLogging(AnalysisExpired analysisExpired) {
    if (analysisExpired.getAnalysisList().isEmpty())
      log.warn("Received an empty analysis expired list");
    else
      log.info("Received {} analysis to expire", analysisExpired.getAnalysisCount());
  }

  private void erasePiiData(
      Collection<String> documentIds, String index, Map<String, Object> payload) {

    log.debug("Erase PII data in {} documents from index '{}' with payload {}.",
              documentIds.size(), index, payload);
    partition(new ArrayList<>(documentIds), updateBatchSize)
        .forEach(ids -> batchErasePiiData(ids, index, payload));
  }

  private void batchErasePiiData(
      List<String> documentIds, String index, Map<String, Object> payload) {
    log.trace("Batch erase PII data in {} documents from index '{}'.", documentIds.size(), index);
    List<MapWithIndex> mapWithIndexList = documentIds
        .stream()
        .map(document -> new MapWithIndex(index, document, payload))
        .collect(toList());
    alertIndexService.saveAlerts(mapWithIndexList);
  }

  private Map<String, Object> createPayload() {
    Map<String, Object> erasedMap = new HashMap<>();
    retentionSimulationProperties
        .getFieldsToErase()
        .forEach(field -> erasedMap.put(field, ERASED_VALUE));

    return erasedMap;
  }
}
