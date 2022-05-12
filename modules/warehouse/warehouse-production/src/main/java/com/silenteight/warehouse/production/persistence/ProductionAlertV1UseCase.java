package com.silenteight.warehouse.production.persistence;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.handler.ProductionRequestV1CommandHandler;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
class ProductionAlertV1UseCase implements ProductionRequestV1CommandHandler {

  @NonNull
  private final AlertMapper alertMapper;
  @NonNull
  private final PersistenceService persistenceService;
  @NonNull
  private final TimeSource timeSource;

  private final int productionAlertsBatchSize;

  @Override
  @Transactional
  public DataIndexResponse handle(ProductionDataIndexRequest request) {
    log.info("v1.ProductionDataIndexRequest received, requestId={}, alertCount={}, sizeInBytes={}",
        request.getRequestId(), request.getAlertsCount(), request.getSerializedSize());

    partition(request.getAlertsList(), productionAlertsBatchSize)
        .stream()
        .flatMap(Collection::stream)
        .map(alertMapper::toAlertDefinition)
        .forEach(persistenceService::insert);

    log.debug("v1.ProductionDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
