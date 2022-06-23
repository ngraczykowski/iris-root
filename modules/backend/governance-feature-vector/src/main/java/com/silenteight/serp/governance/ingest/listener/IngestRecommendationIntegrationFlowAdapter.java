package com.silenteight.serp.governance.ingest.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class IngestRecommendationIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private String inboundChannel;

  @NonNull
  private String outboundChannel;

  @NonNull
  private final IngestDataHandler ingestDataHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .handle(
            ProductionDataIndexRequest.class,
            (payload, headers) -> ingestDataHandler.handle(payload))
        .channel(outboundChannel);
  }
}
