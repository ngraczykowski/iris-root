package com.silenteight.serp.governance.analytics.listener;

import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.serp.governance.common.integration.FeatureVectorSolvedAmqpIntegrationConfiguration.FEATURE_VECTOR_SOLVED_INBOUND_CHANNEL;

@RequiredArgsConstructor
class FeatureVectorSolvedIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final FeatureVectorSolvedMessageHandler featureVectorSolvedMessageHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(FEATURE_VECTOR_SOLVED_INBOUND_CHANNEL)
        .handle(
            FeatureVectorSolvedEvent.class,
            (payload, headers) -> {
              featureVectorSolvedMessageHandler.handle(payload);
              return null;
            });
  }
}
