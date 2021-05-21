package com.silenteight.warehouse.indexer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;
import com.silenteight.warehouse.indexer.analysis.NamingStrategy;
import com.silenteight.warehouse.indexer.listener.IndexRequestCommandHandler;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class AlertIndexUseCase implements IndexRequestCommandHandler {

  @NonNull
  private final AlertService alertService;

  @NonNull
  private final AnalysisService analysisService;

  @NonNull
  private final TimeSource timeSource;

  @Override
  public DataIndexResponse handle(
      DataIndexRequest dataIndexRequest, NamingStrategy namingStrategy) {

    log.debug("DataIndexRequest received, requestId={}, strategy={}",
        dataIndexRequest.getRequestId(), namingStrategy);

    AnalysisMetadataDto analysisMetadataDto = analysisService
        .getOrCreateAnalysisMetadata(dataIndexRequest.getAnalysisName(), namingStrategy);

    alertService.indexAlert(dataIndexRequest, analysisMetadataDto.getElasticIndexName());

    log.trace("DataIndexRequest processed, requestId={}, strategy={}, analysis={}",
        dataIndexRequest.getRequestId(), namingStrategy, analysisMetadataDto);

    return DataIndexResponse.newBuilder()
        .setRequestId(dataIndexRequest.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
