package com.silenteight.simulator.model.archive.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.ModelsArchived;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class ModelsArchivedFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final SimulatorModelsArchivedMessageHandler handler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(ModelsArchived.class, (p, h) -> {
          handler.handle(p);
          return null;
        });
  }
}
