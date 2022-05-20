package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels.ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "true"
)
@Slf4j
public class DummyAnalysisAlertsAddedToPendingRecommendationsIntegrationFlow extends
    IntegrationFlowAdapter {

  private final PendingRecommendationFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ADDED_ANALYSIS_ALERTS_INBOUND_CHANNEL)
        .handle(AnalysisAlertsAdded.class, (payload, headers) -> {
          log.debug(
              "New solving turn off using "
                  + "AnalysisAlertsAddedToPendingRecommendationsIntegrationFlow");
          return null;
        });
  }
}
