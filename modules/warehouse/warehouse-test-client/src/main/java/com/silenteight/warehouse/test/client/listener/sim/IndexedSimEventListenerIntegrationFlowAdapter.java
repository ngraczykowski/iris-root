package com.silenteight.warehouse.test.client.listener.sim;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.warehouse.test.client.listener.sim.IndexedSimListenerConfiguration.ALERT_SIM_INDEXED_INBOUND_CHANNEL;

@RequiredArgsConstructor
class IndexedSimEventListenerIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final IndexedSimEventListener indexedEventListener;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERT_SIM_INDEXED_INBOUND_CHANNEL)
        .<Object, Class<?>>route(Object::getClass, m -> m
            .subFlowMapping(
                com.silenteight.data.api.v1.DataIndexResponse.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v1.DataIndexResponse.class,
                    (payload, headers) -> {
                      indexedEventListener.onEvent(payload.getRequestId());
                      return null;
                    }))
            .subFlowMapping(
                com.silenteight.data.api.v2.DataIndexResponse.class,
                sf -> sf.handle(
                    com.silenteight.data.api.v2.DataIndexResponse.class,
                    (payload, headers) -> {
                      indexedEventListener.onEvent(payload.getRequestId());
                      return null;
                    })));
  }
}
