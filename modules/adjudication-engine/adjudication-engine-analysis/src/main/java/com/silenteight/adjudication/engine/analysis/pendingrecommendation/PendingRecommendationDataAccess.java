package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

public interface PendingRecommendationDataAccess {

  int createPendingRecommendations(long analysisId);

  int removeSolvedPendingRecommendations();
}
