package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Repository
class JdbcRecommendationDataAccess implements RecommendationDataAccess {

  private final SelectPendingAlertsQuery selectPendingAlertsQuery;
  private final StreamAlertRecommendationsQuery streamAlertRecommendationsQuery;
  private final GetAlertRecommendationQuery getAlertRecommendationQuery;
  private final InsertAlertRecommendationsQuery insertAlertRecommendationsQuery;

  @Override
  @Transactional(readOnly = true)
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public PendingAlerts selectPendingAlerts(long analysisId) {
    return selectPendingAlertsQuery.execute(analysisId);
  }

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public int streamAlertRecommendations(
      long analysisId, @Nonnull Consumer<AlertRecommendation> consumer) {
    return streamAlertRecommendationsQuery.execute(analysisId, consumer);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public int streamAlertRecommendations(
      long analysisId, long datasetId, @Nonnull Consumer<AlertRecommendation> consumer) {

    return streamAlertRecommendationsQuery.execute(analysisId, datasetId, consumer);
  }

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public AlertRecommendation getAlertRecommendation(long recommendationId) {
    return getAlertRecommendationQuery.execute(recommendationId);
  }

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<RecommendationResponse> insertAlertRecommendation(
      @Nonnull Collection<InsertRecommendationRequest> alertRecommendation) {
    return insertAlertRecommendationsQuery.execute(alertRecommendation);
  }
}
