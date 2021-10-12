package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class ProductionAlertIndexUseCase implements ProductionIndexRequestCommandHandler {

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
    log.info("ProductionDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    partition(request.getAlertsList(), productionAlertsBatchSize).stream()
        .map(productionAlertIndexResolvingService::getIndex)
        .map(productionAlertMappingService::mapFields)
        .forEach(alertIndexService::saveAlerts);

    log.debug("ProductionDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
