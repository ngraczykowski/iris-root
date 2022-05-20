package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface RecommendationDataAccess {

  PendingAlerts selectPendingAlerts(long analysisId);

  int streamAlertRecommendations(
      long analysisId, Consumer<AlertRecommendation> consumer);

  int streamAlertRecommendations(
      long analysisId, long datasetId, Consumer<AlertRecommendation> consumer);

  AlertRecommendation getAlertRecommendation(long recommendationId);

  List<RecommendationResponse> insertAlertRecommendation(
      Collection<InsertRecommendationRequest> alertRecommendation);
}
