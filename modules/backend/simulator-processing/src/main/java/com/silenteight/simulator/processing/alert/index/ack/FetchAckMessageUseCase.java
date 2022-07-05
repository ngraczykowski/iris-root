package com.silenteight.simulator.processing.alert.index.ack;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.simulator.processing.alert.index.amqp.listener.AckMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

@Slf4j
@RequiredArgsConstructor
class FetchAckMessageUseCase implements AckMessageHandler {

  @NonNull private final IndexedAlertService indexedAlertService;

  @Override
  public void handle(DataIndexResponse request) {
    String requestId = request.getRequestId();
    log.debug("Data feed acknowledged: requestId={}", requestId);

    indexedAlertService.ack(requestId);
  }
}
