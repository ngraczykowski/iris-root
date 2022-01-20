package com.silenteight.warehouse.indexer.production.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.warehouse.indexer.alert.indexing.AlertIndexService;

import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
class QaAlertIndexUseCase implements QaIndexRequestCommandHandler {
  @NonNull
  private final QaAlertMappingService qaAlertMappingService;
  @NonNull
  private final QaAlertIndexResolvingService qaAlertIndexResolvingService;
  @NonNull
  private final AlertIndexService alertIndexService;
  private final int qaAlertsBatchSize;

  @Override
  public void handle(QaDataIndexRequest request) {
    log.info("QaDataIndexRequest received, requestId={}, alertCount={}",
        request.getRequestId(), request.getAlertsCount());

    partition(request.getAlertsList(), qaAlertsBatchSize).stream()
        .map(qaAlertIndexResolvingService::getIndex)
        .map(qaAlertMappingService::mapFields)
        .forEach(alertIndexService::saveAlerts);

    log.debug("QaDataIndexRequest processed, requestId={}", request.getRequestId());
  }
}
