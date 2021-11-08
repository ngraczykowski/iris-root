package com.silenteight.simulator.retention.dataset.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.dataretention.api.v1.DatasetsExpired;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class DatasetExpiredFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @NonNull
  private final DatasetsExpiredMessageHandler handler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            DatasetsExpired.class,
            (p, h) -> handler.handle(p))
        .channel(outboundChannel);
  }
}
