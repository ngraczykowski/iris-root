package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InsertAlertRecommendationsQuery {

  private final BatchSqlUpdate sql;

  InsertAlertRecommendationsQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(
        "INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action)\n"
            + "VALUES (:analysis_id, :alert_id, now(), :recommended_action)\n"
            + "ON CONFLICT DO NOTHING\n"
            + "RETURNING recommendation_id, analysis_id, alert_id;");
    sql.declareParameter(new SqlParameter("alert_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("analysis_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("recommended_action", Types.VARCHAR));
    sql.setReturnGeneratedKeys(true);

    sql.compile();
  }

  @SuppressWarnings("FeatureEnvy")
  List<RecommendationResponse> execute(Collection<InsertRecommendationRequest> requests) {
    var keyHolder = new GeneratedKeyHolder();
    requests.forEach(r -> update(r, keyHolder));
    sql.flush();
    return keyHolder
        .getKeyList()
        .stream()
        .map(it -> RecommendationResponse
            .builder()
            .recommendationId((long) it.get("recommendation_id"))
            .analysisId((long) it.get("analysis_id"))
            .alertId((long) it.get("alert_id"))
            .build())
        .collect(
            Collectors.toList());
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(InsertRecommendationRequest alertRecommendation, KeyHolder keyHolder) {
    var paramMap =
        Map.of("alert_id", alertRecommendation.getAlertId(),
            "analysis_id", alertRecommendation.getAnalysisId(),
            "recommended_action", alertRecommendation.getRecommendedAction());
    sql.updateByNamedParam(paramMap, keyHolder);
  }
}
