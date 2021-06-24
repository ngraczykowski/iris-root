package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

import java.util.List;

public interface RecommendationDataAccess {

  PendingAlerts selectPendingAlerts(long analysisId);

  List<AlertRecommendation> selectAlertRecommendation(long alertId);

  List<AlertRecommendation> selectAlertRecommendation(long alertId, long datasetId);
}
