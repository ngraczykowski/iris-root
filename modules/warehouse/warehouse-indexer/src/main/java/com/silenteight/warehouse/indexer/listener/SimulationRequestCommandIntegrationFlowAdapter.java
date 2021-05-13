package com.silenteight.warehouse.indexer.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.warehouse.indexer.analysis.NamingStrategy;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class SimulationRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final IndexRequestCommandHandler indexRequestCommand;

  @NonNull
  private final NamingStrategy namingStrategy;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            DataIndexRequest.class,
            (payload, headers) -> indexRequestCommand.handle(payload, namingStrategy))
        .channel(outboundChannel);
  }
}
