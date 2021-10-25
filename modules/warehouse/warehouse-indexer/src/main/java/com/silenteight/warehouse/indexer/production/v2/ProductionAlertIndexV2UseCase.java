package com.silenteight.warehouse.indexer.production.v2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class ProductionAlertIndexV2UseCase implements ProductionIndexRequestV2CommandHandler {

  @NonNull
  private final ProductionAlertIndexResolvingService productionAlertIndexResolvingService;

  @NonNull
  private final ProductionAlertMappingService productionAlertMappingService;

  @NonNull
  private final AlertIndexService alertIndexService;

  @NonNull
  private final TimeSource timeSource;

  private final int productionAlertsBatchSize;

  @Override
  public DataIndexResponse handle(ProductionDataIndexRequest request) {
    log.info("v2.ProductionDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    partition(request.getAlertsList(), productionAlertsBatchSize).stream()
        .map(productionAlertIndexResolvingService::getTargetIndices)
        .map(productionAlertMappingService::mapFields)
        .forEach(alertIndexService::saveAlerts);

    log.debug("v2.ProductionDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
