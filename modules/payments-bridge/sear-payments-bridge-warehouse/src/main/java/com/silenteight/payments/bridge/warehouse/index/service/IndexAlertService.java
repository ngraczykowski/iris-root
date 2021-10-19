package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRequestId;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import com.google.common.base.Preconditions;
import com.google.protobuf.TextFormat;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

@RequiredArgsConstructor
@Service
@Slf4j
class IndexAlertService implements IndexAlertUseCase {

  private final CommonChannels commonChannels;

  @Setter
  private IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  @Override
  public IndexRequestId index(Iterable<Alert> alerts) {
    var requestId = idGenerator.generateId();
    var indexRequest = ProductionDataIndexRequest.newBuilder()
        .setRequestId(requestId.toString())
        .addAllAlerts(alerts)
        .build();

    Preconditions.checkArgument(indexRequest.getAlertsCount() != 0, "alerts must not be empty");

    if (log.isDebugEnabled()) {
      log.debug("Sending indexing request: {}", TextFormat.shortDebugString(indexRequest));
    }

    commonChannels.warehouseRequested().send(
        MessageBuilder.withPayload(indexRequest).build());

    log.info("Sent payload for indexing: requestId={}, alertCount={}",
        requestId, indexRequest.getAlertsCount());

    return new IndexRequestId(requestId);
  }
}
