package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

public interface RecommendationDataAccess {

  PendingAlerts selectPendingAlerts(long analysisId);
}
