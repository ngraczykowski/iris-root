package com.silenteight.warehouse.indexer.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;
import com.silenteight.warehouse.indexer.simulation.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.simulation.analysis.UniqueAnalysisFactory;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertIndexUseCase implements SimulationIndexRequestCommandHandler {

  @NonNull
  private final SimulationAlertMappingService simulationAlertMappingService;

  @NonNull
  private final AlertIndexService alertIndexService;

  @NonNull
  private final UniqueAnalysisFactory uniqueAnalysisFactory;


  @NonNull
  private final TimeSource timeSource;

  private final int simulationAlertsBatchSize;

  @Override
  public DataIndexResponse handle(SimulationDataIndexRequest request) {
    log.debug("SimulationDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    AnalysisMetadataDto uniqueAnalysis =
        uniqueAnalysisFactory.getUniqueAnalysis(request.getAnalysisName());
    String targetIndexName = uniqueAnalysis.getElasticIndexName();

    partition(request.getAlertsList(), simulationAlertsBatchSize).stream()
        .map(alerts -> simulationAlertMappingService.mapFields(alerts, targetIndexName))
        .forEach(alertIndexService::saveAlerts);

    log.debug("SimulationDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
