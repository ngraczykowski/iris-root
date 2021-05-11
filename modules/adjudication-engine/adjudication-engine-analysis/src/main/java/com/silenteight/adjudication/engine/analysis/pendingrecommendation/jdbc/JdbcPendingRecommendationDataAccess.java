package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationDataAccess;

import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class JdbcPendingRecommendationDataAccess implements PendingRecommendationDataAccess {

  private final InsertPendingRecommendationsQuery insertPendingRecommendationsQuery;

  @Override
  public int createPendingRecommendations(long analysisId) {
    return insertPendingRecommendationsQuery.execute(analysisId);
  }
}
