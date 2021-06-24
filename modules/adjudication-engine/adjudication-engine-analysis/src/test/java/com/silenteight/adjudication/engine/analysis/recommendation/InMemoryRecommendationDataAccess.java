package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.List;

class InMemoryRecommendationDataAccess implements RecommendationDataAccess {

  private int pendingAlertsCount = 5;

  @Override
  public PendingAlerts selectPendingAlerts(
      long analysisId) {
    if (analysisId == 5)
      return new PendingAlerts(List.of());
    if (pendingAlertsCount-- == 0)
      return new PendingAlerts(List.of());
    return new PendingAlerts(
        List.of(new PendingAlert(1, List.of(FeatureVectorSolution.SOLUTION_FALSE_POSITIVE))));
  }

  @Override
  public List<AlertRecommendation> selectAlertRecommendation(
      long alertId) {
    return null;
  }

  @Override
  public List<AlertRecommendation> selectAlertRecommendation(long alertId, long datasetId) {
    return null;
  }
}
