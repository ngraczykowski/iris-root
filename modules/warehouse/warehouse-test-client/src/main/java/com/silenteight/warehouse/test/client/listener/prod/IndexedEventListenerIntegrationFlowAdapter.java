package com.silenteight.warehouse.test.client.listener.prod;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.DataIndexResponse;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.warehouse.test.client.listener.prod.IndexedListenerConfiguration.ALERT_INDEXED_INBOUND_CHANNEL;

@RequiredArgsConstructor
class IndexedEventListenerIntegrationFlowAdapter extends IntegrationFlowAdapter {

  @NonNull
  private final IndexedEventListener indexedEventListener;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERT_INDEXED_INBOUND_CHANNEL)
        .handle(
            DataIndexResponse.class,
            (payload, headers) -> {
              indexedEventListener.onEvent(payload);
              return null;
            }
        );
  }
}
