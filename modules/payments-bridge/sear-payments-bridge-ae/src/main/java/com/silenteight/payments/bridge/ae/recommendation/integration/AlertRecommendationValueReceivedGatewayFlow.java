package com.silenteight.payments.bridge.ae.recommendation.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.ae.recommendation.integration.RecommendationChannels.ALERT_RECOMMENDATION_GATEWAY_CHANNEL;
import static com.silenteight.payments.bridge.ae.recommendation.integration.RecommendationChannels.ALERT_RECOMMENDATION_OUTBOUND_CHANNEL;

@SuppressWarnings({ "MethodMayBeStatic", "java:S2325" })
@Component
@Slf4j
class AlertRecommendationValueReceivedGatewayFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERT_RECOMMENDATION_GATEWAY_CHANNEL)
        .handle(Recommendation.class, this::logRequest)
        .channel(ALERT_RECOMMENDATION_OUTBOUND_CHANNEL);
  }

  private Recommendation logRequest(
      Recommendation payload, MessageHeaders headers) {
    log.debug("sending recommendation = {}", payload);

    return payload;
  }
}
