package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

/*
AnalysisDatasetsAdded (rabbit) -> List<PendingRecommendation> (db) -> RecommendationsPending (rabbit)
 */
@RequiredArgsConstructor
@Component
class PendingRecommendationsIntegrationFlow extends IntegrationFlowAdapter {

  private final PendingRecommendationFacade pendingRecommendationFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(PendingRecommendationChannels.ADDED_ANALYSIS_DATASETS_INBOUND_CHANNEL)
        .handle(AddedAnalysisDatasets.class, (payload, headers) -> {
          var result = pendingRecommendationFacade.handleAddedAnalysisDatasets(payload);
          return result.orElse(null);
        })
        .channel(PendingRecommendationChannels.PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL);
  }
}
