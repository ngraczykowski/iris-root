package com.silenteight.warehouse.simulation.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

@Slf4j
@RequiredArgsConstructor
class SimulationRequestCommandIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final SimulationRequestV1CommandHandler simulationRequestV1CommandHandler;

  @NonNull
  private final SimulationRequestV2CommandHandler simulationRequestV2CommandHandler;

  @NonNull
  private final String inboundChannel;

  @NonNull
  private final String outboundChannel;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(inboundChannel)
        .<Object, Class<?>>route(Object::getClass, m -> m
            .subFlowMapping(
                SimulationDataIndexRequest.class,
                sf -> sf.handle(
                    SimulationDataIndexRequest.class,
                    (payload, headers) -> simulationRequestV1CommandHandler.handle(payload)))
            .subFlowMapping(
                com.silenteight.data.api.v2.SimulationDataIndexRequest.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v2.SimulationDataIndexRequest.class,
                    (payload, headers) -> simulationRequestV2CommandHandler.handle(payload)))
            .defaultSubFlowMapping(
                sf -> sf.handle(
                    Object.class,
                    (payload, headers) -> {
                      log.warn("Unsupported simulation request");
                      return null;
                    })
            ))
        .channel(outboundChannel);
  }
}

