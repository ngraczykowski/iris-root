package com.silenteight.warehouse.indexer.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.DataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.warehouse.indexer.listener.IndexerListenerConfiguration.ALERT_INDEXING_INBOUND_CHANNEL;

@RequiredArgsConstructor
class IndexRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final IndexRequestCommandHandler indexRequestCommand;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERT_INDEXING_INBOUND_CHANNEL)
        .handle(
            DataIndexRequest.class,
            (payload, headers) -> {
              indexRequestCommand.handle(payload);
              return null;
            }
        );
  }
}
