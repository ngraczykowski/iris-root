package com.silenteight.warehouse.indexer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.alert.AlertService;
import com.silenteight.warehouse.indexer.gateway.IndexedConfirmationGateway;
import com.silenteight.warehouse.indexer.listener.IndexRequestCommandHandler;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;

@Slf4j
@RequiredArgsConstructor
public class AlertIndexUseCase implements IndexRequestCommandHandler {

  @NonNull
  private final IndexedConfirmationGateway indexedConfirmationGateway;

  @NonNull
  private final AlertService alertService;

  @NonNull
  private final TimeSource timeSource;

  public void activate(DataIndexRequest dataIndexRequest) {
    log.debug("DataIndexRequestReceived, requestId={}", dataIndexRequest.getRequestId());

    alertService.indexAlert(dataIndexRequest);

    DataIndexResponse response = DataIndexResponse.newBuilder()
        .setRequestId(dataIndexRequest.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
    indexedConfirmationGateway.alertIndexed(response);
  }

  @Override
  public void handle(DataIndexRequest dataIndexRequest) {
    this.activate(dataIndexRequest);
  }
}
