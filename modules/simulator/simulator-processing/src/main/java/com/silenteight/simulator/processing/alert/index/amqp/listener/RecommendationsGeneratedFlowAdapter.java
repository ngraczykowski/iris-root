package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class RecommendationsGeneratedFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @NonNull
  private final RecommendationsGeneratedMessageHandler handler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(RecommendationsGenerated.class,
            (payload, headers) -> handler.handle(payload))
        .channel(outboundChannel);
  }
}
