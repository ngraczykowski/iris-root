package com.silenteight.warehouse.simulation.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class SimulationRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final SimulationRequestV1CommandHandler simulationRequestV1CommandHandler;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            SimulationDataIndexRequest.class,
            (payload, headers) -> simulationRequestV1CommandHandler.handle(payload))
        .channel(outboundChannel);
  }
}
