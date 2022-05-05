package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationDataAccess;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcPendingRecommendationDataAccess implements PendingRecommendationDataAccess {

  private final CreatePendingRecommendationsQuery createPendingRecommendationsQuery;
  private final RemoveSolvedPendingRecommendationsQuery removeSolvedPendingRecommendationsQuery;
  private final RemovePendingRecommendationByAnalysisIdsQuery
      removePendingRecommendationByAnalysisIdsQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)

  public int createPendingRecommendations(long analysisId) {
    return createPendingRecommendationsQuery.execute(analysisId);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public int removeSolvedPendingRecommendations() {
    return removeSolvedPendingRecommendationsQuery.execute();
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<Long> removePendingRecommendationByAnalysisIds(List<Long> analysisIds) {
    return removePendingRecommendationByAnalysisIdsQuery.execute(analysisIds);
  }
}
