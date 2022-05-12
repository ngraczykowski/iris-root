package com.silenteight.warehouse.qa.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.QaDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class QaRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final QaRequestCommandHandler qaRequestCommandHandler;

  @NonNull
  private final String inboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            QaDataIndexRequest.class,
            (payload, headers) -> {
              qaRequestCommandHandler.handle(payload);
              return null;
            });
  }
}
