package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

import java.util.function.Consumer;

public interface RecommendationDataAccess {

  PendingAlerts selectPendingAlerts(long analysisId);

  int streamAlertRecommendations(
      long analysisId, Consumer<AlertRecommendation> consumer);

  int streamAlertRecommendations(
      long analysisId, long datasetId, Consumer<AlertRecommendation> consumer);

  AlertRecommendation getAlertRecommendation(long recommendationId);
}
