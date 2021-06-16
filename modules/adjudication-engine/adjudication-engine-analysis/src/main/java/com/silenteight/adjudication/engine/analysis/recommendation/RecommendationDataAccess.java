package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;

import java.util.List;

public interface RecommendationDataAccess {

  List<PendingAlert> selectPendingAlerts(long analysisId);
}
