package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Repository
public class JdbcRecommendationDataAccess implements RecommendationDataAccess {

  private final SelectPendingAlertsQuery selectPendingAlertsQuery;
  private final StreamAlertRecommendationsQuery streamAlertRecommendationsQuery;

  @Override
  @Transactional(readOnly = true)
  public PendingAlerts selectPendingAlerts(long analysisId) {
    return selectPendingAlertsQuery.execute(analysisId);
  }

  @Override
  public int streamAlertRecommendations(long analysisId, Consumer<AlertRecommendation> consumer) {
    return streamAlertRecommendationsQuery.execute(analysisId, consumer);
  }

  @Override
  public int streamAlertRecommendations(
      long analysisId, long datasetId, @Nonnull Consumer<AlertRecommendation> consumer) {

    return streamAlertRecommendationsQuery.execute(analysisId, datasetId, consumer);
  }

  @Override
  public AlertRecommendation getAlertRecommendation(long recommendationId) {
    return null;
  }
}
