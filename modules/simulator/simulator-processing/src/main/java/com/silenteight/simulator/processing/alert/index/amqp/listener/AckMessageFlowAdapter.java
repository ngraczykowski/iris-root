package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.DataIndexResponse;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class AckMessageFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @NonNull
  private final AckMessageHandler handler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(DataIndexResponse.class, (p, h) -> {
          handler.handle(p);
          return p;
        })
        .channel(outboundChannel);
  }
}
