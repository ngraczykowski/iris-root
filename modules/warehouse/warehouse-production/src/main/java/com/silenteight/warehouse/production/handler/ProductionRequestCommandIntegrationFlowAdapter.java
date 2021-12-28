package com.silenteight.warehouse.production.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@RequiredArgsConstructor
class ProductionRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final ProductionRequestV1CommandHandler productionRequestV1CommandHandler;

  @NonNull
  private final ProductionRequestV2CommandHandler productionRequestV2CommandHandler;

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
                    (payload, headers) -> productionRequestV1CommandHandler.handle(payload)))
            .subFlowMapping(
                com.silenteight.data.api.v2.ProductionDataIndexRequest.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v2.ProductionDataIndexRequest.class,
                    (payload, headers) -> productionRequestV2CommandHandler.handle(payload))))
        .channel(outboundChannel);
  }
}
