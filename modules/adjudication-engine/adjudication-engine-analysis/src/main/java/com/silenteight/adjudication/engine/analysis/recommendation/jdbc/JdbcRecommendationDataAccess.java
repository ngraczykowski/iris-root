package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class JdbcRecommendationDataAccess implements RecommendationDataAccess {

  private final SelectPendingAlertsQuery query;

  @Override
  @Transactional(readOnly = true)
  public PendingAlerts selectPendingAlerts(long analysisId) {
    return query.execute(analysisId);
  }
}
