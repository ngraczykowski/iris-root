package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.event.WarehouseIndexRequestedEvent;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRequestId;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import com.google.common.base.Preconditions;
import com.google.protobuf.TextFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

@RequiredArgsConstructor
@Service
@Slf4j
class IndexAlertService implements IndexAlertUseCase {

  private final IndexAlertPublisher indexAlertPublisher;

  @Setter
  private IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  @Override
  public IndexRequestId index(Iterable<Alert> alerts, RequestOrigin requestOrigin) {
    var requestId = idGenerator.generateId();
    var indexRequest = ProductionDataIndexRequest.newBuilder()
        .setRequestId(requestId.toString())
        .addAllAlerts(alerts)
        .build();

    Preconditions.checkArgument(indexRequest.getAlertsCount() != 0, "alerts must not be empty");

    if (log.isDebugEnabled()) {
      log.debug("Sending indexing request: {}", TextFormat.shortDebugString(indexRequest));
    }

    indexAlertPublisher.send(
        new WarehouseIndexRequestedEvent(indexRequest, requestOrigin.mapToIndexRequestOrigin()));
    log.info("Sent payload for indexing: requestId={}, alertCount={}",
        requestId, indexRequest.getAlertsCount());

    return new IndexRequestId(requestId);
  }

}
