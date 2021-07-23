package com.silenteight.warehouse.indexer.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertCopyDataService;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.SimulationNamingStrategy;
import com.silenteight.warehouse.indexer.analysis.UniqueAnalysisFactory;
import com.silenteight.warehouse.indexer.indexing.listener.SimulationIndexRequestCommandHandler;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertIndexUseCase implements SimulationIndexRequestCommandHandler {

  @NonNull
  private final AlertService alertService;

  @NonNull
  private final UniqueAnalysisFactory uniqueAnalysisFactory;

  @NonNull
  private final AlertCopyDataService alertCopyDataService;

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final String environmentPrefix;

  @Override
  public DataIndexResponse handle(SimulationDataIndexRequest request) {
    log.debug("SimulationDataIndexRequest received, requestId={}", request.getRequestId());

    SimulationNamingStrategy namingStrategy = new SimulationNamingStrategy(environmentPrefix);
    AnalysisMetadataDto analysisMetadataDto = uniqueAnalysisFactory.getUniqueAnalysis(
        request.getAnalysisName(), namingStrategy);

    alertCopyDataService.copyProductionIntoSimulation(
        request.getAlertsList(), analysisMetadataDto.getElasticIndexName());
    alertService.indexAlerts(request.getAlertsList(), analysisMetadataDto.getElasticIndexName());

    log.debug("SimulationDataIndexRequest processed, requestId={}, strategy={}, analysis={}",
        request.getRequestId(), namingStrategy, analysisMetadataDto);

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
