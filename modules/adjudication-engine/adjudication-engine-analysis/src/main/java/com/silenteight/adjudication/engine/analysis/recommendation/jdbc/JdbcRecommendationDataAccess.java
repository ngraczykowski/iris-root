package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcRecommendationDataAccess implements RecommendationDataAccess {

  private final SelectPendingAlertsQuery query;

  @Override
  public List<PendingAlert> selectPendingAlerts(long analysisId) {
    return query.execute(analysisId);
  }
}
