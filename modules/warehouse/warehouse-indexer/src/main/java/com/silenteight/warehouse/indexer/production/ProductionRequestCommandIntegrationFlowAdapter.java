package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class ProductionRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final ProductionIndexRequestCommandHandler productionIndexRequestCommandHandler;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            ProductionDataIndexRequest.class,
            (payload, headers) -> productionIndexRequestCommandHandler.handle(payload))
        .channel(outboundChannel);
  }
}
