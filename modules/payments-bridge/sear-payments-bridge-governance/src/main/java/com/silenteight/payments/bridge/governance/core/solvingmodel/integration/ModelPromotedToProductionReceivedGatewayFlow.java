package com.silenteight.payments.bridge.governance.core.solvingmodel.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.payments.bridge.internal.v1.event.ModelUpdated;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.governance.core.solvingmodel.integration.SolvingModelChannels.SOLVING_MODEL_GATEWAY_CHANNEL;
import static com.silenteight.payments.bridge.governance.core.solvingmodel.integration.SolvingModelChannels.SOLVING_MODEL_OUTBOUND_CHANNEL;

@SuppressWarnings({ "MethodMayBeStatic", "java:S2325" })
@Component
@Slf4j
class ModelPromotedToProductionReceivedGatewayFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(SOLVING_MODEL_GATEWAY_CHANNEL)
        .handle(ModelUpdated.class, this::logRequest)
        .channel(SOLVING_MODEL_OUTBOUND_CHANNEL);
  }

  private ModelUpdated logRequest(
      ModelUpdated payload, MessageHeaders headers) {
    log.debug("Sending model updated= {}", payload);

    return payload;
  }
}
