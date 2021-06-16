package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlertsWithStrategy;

public interface RecommendationDataAccess {

  PendingAlertsWithStrategy selectPendingAlerts(long analysisId);
}
