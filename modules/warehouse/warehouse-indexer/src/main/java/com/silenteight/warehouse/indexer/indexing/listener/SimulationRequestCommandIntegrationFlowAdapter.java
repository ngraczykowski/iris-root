package com.silenteight.warehouse.indexer.indexing.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class SimulationRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final SimulationIndexRequestCommandHandler simulationIndexRequestCommandHandler;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            SimulationDataIndexRequest.class,
            (payload, headers) -> simulationIndexRequestCommandHandler.handle(payload))
        .channel(outboundChannel);
  }
}
