package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import java.util.List;

public interface PendingRecommendationDataAccess {

  int createPendingRecommendations(long analysisId);

  int removeSolvedPendingRecommendations();

  List<Long> removePendingRecommendationByAnalysisIds(List<Long> analysisIds);

}
