package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertIndexService;
import com.silenteight.warehouse.indexer.production.indextracking.IndexByDiscriminatorResolverFactory;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class ProductionAlertIndexUseCase implements ProductionIndexRequestCommandHandler {

  @NonNull
  private final AlertIndexService alertIndexService;

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final IndexByDiscriminatorResolverFactory productionWriteIndexProviderFactory;

  @Override
  public DataIndexResponse handle(ProductionDataIndexRequest request) {
    log.info("ProductionDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    alertIndexService.indexAlerts(
        request.getAlertsList(),
        productionWriteIndexProviderFactory.productionWriteIndexProvider());

    log.debug("ProductionDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
