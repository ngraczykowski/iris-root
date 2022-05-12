package com.silenteight.warehouse.qa.processing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.warehouse.qa.handler.QaRequestCommandHandler;
import com.silenteight.warehouse.qa.processing.mapping.QaAlertMapper;
import com.silenteight.warehouse.qa.processing.update.QaUpdateService;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
class QaDataIndexRequestUseCase implements QaRequestCommandHandler {

  @NonNull
  private final QaAlertMapper qaAlertMapper;
  @NonNull
  private final QaUpdateService persistenceService;
  private final int qaBatchSize;

  @Override
  @Transactional
  public void handle(QaDataIndexRequest request) {
    log.info("QaDataIndexRequest received, requestId={}, alertCount={}, sizeInBytes={}",
        request.getRequestId(), request.getAlertsCount(), request.getSerializedSize());

    partition(request.getAlertsList(), qaBatchSize)
        .stream()
        .flatMap(Collection::stream)
        .map(qaAlertMapper::convertAlertToAttributes)
        .forEach(persistenceService::update);

    log.debug("QaDataIndexRequest processed, requestId={}", request.getRequestId());
  }
}
