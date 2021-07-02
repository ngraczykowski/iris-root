package com.silenteight.warehouse.indexer.indexing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.analysis.ProductionNamingStrategy;
import com.silenteight.warehouse.indexer.indexing.listener.ProductionIndexRequestCommandHandler;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class ProductionAlertIndexUseCase implements ProductionIndexRequestCommandHandler {

  @NonNull
  private final AlertService alertService;

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final String environmentPrefix;

  @Override
  public DataIndexResponse handle(ProductionDataIndexRequest request) {
    log.debug("ProductionDataIndexRequest received, requestId={}", request.getRequestId());

    ProductionNamingStrategy namingStrategy = new ProductionNamingStrategy(environmentPrefix);
    alertService.indexAlerts(request.getAlertsList(), namingStrategy.getElasticIndexName());

    log.trace("ProductionDataIndexRequest processed, requestId={}, strategy={}",
        request.getRequestId(), namingStrategy);

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
