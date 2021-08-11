package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.*;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFixture.createAlertRecommendation;

class InMemoryRecommendationDataAccess implements RecommendationDataAccess {

  private int pendingAlertsCount = 5;

  @Override
  public PendingAlerts selectPendingAlerts(long analysisId) {
    if (analysisId == 5)
      return new PendingAlerts(List.of());

    if (pendingAlertsCount-- == 0)
      return new PendingAlerts(List.of());

    return new PendingAlerts(
        List.of(new PendingAlert(1, List.of(FeatureVectorSolution.SOLUTION_FALSE_POSITIVE))));
  }

  @Override
  public int streamAlertRecommendations(long analysisId, Consumer<AlertRecommendation> consumer) {
    if (analysisId == 1) {
      consumer.accept(createAlertRecommendation());
      consumer.accept(createAlertRecommendation());
      return 2;
    }

    return 0;
  }

  @Override
  public int streamAlertRecommendations(
      long analysisId, long datasetId, Consumer<AlertRecommendation> consumer) {

    if (analysisId == 1 && datasetId == 1) {
      consumer.accept(createAlertRecommendation());
      return 1;
    }

    return 0;
  }

  @Override
  public AlertRecommendation getAlertRecommendation(long recommendationId) {
    return null;
  }

  @Override
  public List<RecommendationResponse> insertAlertRecommendation(
      Collection<InsertRecommendationRequest> alertRecommendation) {
    return null;
  }
}
