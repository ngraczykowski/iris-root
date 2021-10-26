package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

//TODO (iwnek) Use SQL Bulk update
class InsertAlertRecommendationsQuery {

  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

  private final SqlUpdate sql;

  InsertAlertRecommendationsQuery(JdbcTemplate jdbcTemplate) {
    sql = new SqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(
        "INSERT INTO ae_recommendation ("
            + "analysis_id, alert_id, created_at, recommended_action, match_ids, match_contexts)\n"
            + "VALUES ("
            + ":analysis_id, :alert_id, NOW(), :recommended_action, :match_ids, :match_contexts)\n"
            + "ON CONFLICT DO NOTHING\n"
            + "RETURNING recommendation_id, analysis_id, alert_id;");
    sql.declareParameter(new SqlParameter("alert_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("analysis_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("recommended_action", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("match_ids", Types.ARRAY));
    sql.declareParameter(new SqlParameter("match_contexts", Types.OTHER));
    sql.setReturnGeneratedKeys(true);

    sql.compile();
  }

  List<RecommendationResponse> execute(Collection<InsertRecommendationRequest> requests) {
    List<RecommendationResponse> responses = new ArrayList<>();
    requests.forEach(r -> update(r, responses::add));
    return responses;
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(
      InsertRecommendationRequest alertRecommendation,
      Consumer<RecommendationResponse> consumer) {

    var keyHolder = new GeneratedKeyHolder();
    var paramMap =
        Map.of("alert_id", alertRecommendation.getAlertId(),
            "analysis_id", alertRecommendation.getAnalysisId(),
            "recommended_action", alertRecommendation.getRecommendedAction(),
            "match_ids", alertRecommendation.getMatchIds(),
            "match_contexts", writeMatchContexts(alertRecommendation.getMatchContexts()));

    sql.updateByNamedParam(paramMap, keyHolder);

    ofNullable(keyHolder.getKeys())
        .map(InsertAlertRecommendationsQuery::buildRecommendationResponse)
        .ifPresent(consumer);
  }

  @SuppressWarnings("FeatureEnvy")
  private static RecommendationResponse buildRecommendationResponse(Map<String, Object> it) {
    return RecommendationResponse
        .builder()
        .recommendationId((long) it.get("recommendation_id"))
        .analysisId((long) it.get("analysis_id"))
        .alertId((long) it.get("alert_id"))
        .build();
  }

  private static String writeMatchContexts(ObjectNode[] matchContexts) {
    try {
      return MAPPER.writeValueAsString(matchContexts);
    } catch (JsonProcessingException e) {
      throw new MatchContextsJsonNodeWriteException(e);
    }
  }

  static class MatchContextsJsonNodeWriteException extends RuntimeException {

    private static final long serialVersionUID = 2343279004526563617L;

    MatchContextsJsonNodeWriteException(Throwable cause) {
      super(cause);
    }
  }
}
