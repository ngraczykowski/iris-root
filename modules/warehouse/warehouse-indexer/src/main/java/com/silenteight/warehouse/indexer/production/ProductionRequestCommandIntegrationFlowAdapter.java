package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.production.v1.ProductionIndexRequestV1CommandHandler;
import com.silenteight.warehouse.indexer.production.v2.ProductionIndexRequestV2CommandHandler;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class ProductionRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final ProductionIndexRequestV1CommandHandler productionIndexRequestV1CommandHandler;

  @NonNull
  private final ProductionIndexRequestV2CommandHandler productionIndexRequestV2CommandHandler;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .<Object, Class<?>>route(Object::getClass, m -> m
            .subFlowMapping(
                com.silenteight.data.api.v1.ProductionDataIndexRequest.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v1.ProductionDataIndexRequest.class,
                    (payload, headers) -> productionIndexRequestV1CommandHandler.handle(payload)))
            .subFlowMapping(
                com.silenteight.data.api.v2.ProductionDataIndexRequest.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v2.ProductionDataIndexRequest.class,
                    (payload, headers) -> productionIndexRequestV2CommandHandler.handle(payload))))
        .channel(outboundChannel);
  }
}
