package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationDataAccess;

import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class JdbcPendingRecommendationDataAccess implements PendingRecommendationDataAccess {

  private final CreatePendingRecommendationsQuery createPendingRecommendationsQuery;

  @Override
  public int createPendingRecommendations(long analysisId) {
    return createPendingRecommendationsQuery.execute(analysisId);
  }
}
