package com.silenteight.warehouse.production.handler.v2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
class ProductionAlertV2UseCase implements ProductionRequestV2CommandHandler {

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
    log.info("v2.ProductionDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    partition(request.getAlertsList(), productionAlertsBatchSize)
        .stream()
        .flatMap(Collection::stream)
        .map(alertMapper::toAlertDefinition)
        .forEach(persistenceService::insert);

    log.debug("v2.ProductionDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
