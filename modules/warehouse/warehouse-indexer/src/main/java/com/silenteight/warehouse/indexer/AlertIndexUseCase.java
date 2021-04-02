package com.silenteight.warehouse.indexer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.warehouse.indexer.gateway.IndexedConfirmationGateway;
import com.silenteight.warehouse.indexer.listener.IndexRequestCommandHandler;

@RequiredArgsConstructor
@Slf4j
public class AlertIndexUseCase implements IndexRequestCommandHandler {

  @NonNull
  private final IndexedConfirmationGateway indexedConfirmationGateway;

  public void activate(DataIndexRequest dataIndexRequest) {
    log.info(": {}", dataIndexRequest);

    DataIndexResponse response = DataIndexResponse.newBuilder()
        .setRequestId(dataIndexRequest.getRequestId())
        .build();

    indexedConfirmationGateway.alertIndexed(response);
  }

  @Override
  public void handle(DataIndexRequest dataIndexRequest) {
    this.activate(dataIndexRequest);
  }
}
