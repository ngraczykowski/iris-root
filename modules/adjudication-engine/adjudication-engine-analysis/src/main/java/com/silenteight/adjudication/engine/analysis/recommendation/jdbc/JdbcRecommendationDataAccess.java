package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcRecommendationDataAccess implements RecommendationDataAccess {

  private final SelectPendingAlertsQuery selectPendingAlertsQuery;
  private final SelectAlertRecommendationQuery selectAlertRecommendationQuery;

  @Override
  @Transactional(readOnly = true)
  public PendingAlerts selectPendingAlerts(long analysisId) {
    return selectPendingAlertsQuery.execute(analysisId);
  }

  @Override
  public List<AlertRecommendation> selectAlertRecommendation(long analysisId) {
    return selectAlertRecommendationQuery.execute(analysisId);
  }

  @Override
  public List<AlertRecommendation> selectAlertRecommendation(long alertId, long datasetId) {
    return selectAlertRecommendationQuery.execute(alertId, datasetId);
  }
}
