package com.silenteight.warehouse.indexer.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertCopyDataService;
import com.silenteight.warehouse.indexer.alert.AlertIndexService;
import com.silenteight.warehouse.indexer.alert.FixedIndexedResolver;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;
import com.silenteight.warehouse.indexer.indexing.listener.SimulationIndexRequestCommandHandler;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertIndexUseCase implements SimulationIndexRequestCommandHandler {

  @NonNull
  private final AlertIndexService alertIndexService;

  @NonNull
  private final UniqueAnalysisFactory uniqueAnalysisFactory;

  @NonNull
  private final AlertCopyDataService alertCopyDataService;

  @NonNull
  private final TimeSource timeSource;

  @Override
  public DataIndexResponse handle(SimulationDataIndexRequest request) {
    log.debug("SimulationDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    AnalysisMetadataDto uniqueAnalysis =
        uniqueAnalysisFactory.getUniqueAnalysis(request.getAnalysisName());

    alertCopyDataService.copyProductionIntoSimulation(
        request.getAlertsList(), uniqueAnalysis.getElasticIndexName());

    alertIndexService.indexAlerts(
        request.getAlertsList(), simulationWriteIndexProvider(uniqueAnalysis));

    log.debug("SimulationDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }

  private FixedIndexedResolver simulationWriteIndexProvider(
      AnalysisMetadataDto analysisMetadataDto) {

    return new FixedIndexedResolver(analysisMetadataDto.getElasticIndexName());
  }
}
