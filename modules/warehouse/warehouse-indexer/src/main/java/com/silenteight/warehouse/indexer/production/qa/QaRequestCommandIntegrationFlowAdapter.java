package com.silenteight.warehouse.indexer.production.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.QaDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class QaRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final QaIndexRequestCommandHandler qaIndexRequestCommandHandler;

  @NonNull
  private final String inboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            QaDataIndexRequest.class,
            (payload, headers) -> {
              qaIndexRequestCommandHandler.handle(payload);
              return null;
            }
        );
  }
}
