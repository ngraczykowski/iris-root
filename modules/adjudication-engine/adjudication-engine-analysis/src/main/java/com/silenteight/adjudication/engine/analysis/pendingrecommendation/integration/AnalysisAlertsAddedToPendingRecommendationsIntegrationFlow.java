package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels.ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels.PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "false"
)
public class AnalysisAlertsAddedToPendingRecommendationsIntegrationFlow extends
    IntegrationFlowAdapter {

  private final PendingRecommendationFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL)
        .handle(AnalysisAlertsAdded.class, (payload, headers) ->
            facade.handleAnalysisAlertsAdded(payload).orElse(null))
        .channel(PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL);
  }
}
