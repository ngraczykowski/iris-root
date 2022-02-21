package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;
import com.silenteight.adjudication.engine.analysis.recommendation.jdbc.SelectPendingAlertsQuery.MatchContextsJsonNodeReadException;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.dto.AnalysisRecommendationContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Types;
import java.util.*;
import java.util.function.Consumer;

import static com.silenteight.adjudication.engine.analysis.recommendation.transform.AlertMetaDataTransformer.transferToRecommendationMetaData;
import static java.util.Optional.ofNullable;

//TODO (iwnek) Use SQL Bulk update
@Slf4j
class InsertAlertRecommendationsQuery {

  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  public static final String MATCH_CONTEXTS_COLUMN = "match_contexts";
  public static final String MATCH_IDS_COLUMN = "match_ids";
  public static final String RECOMMENDED_ACTION_COLUMN = "recommended_action";
  public static final String ANALYSIS_ID_COLUMN = "analysis_id";
  public static final String ALERT_ID_COLUMN = "alert_id";
  public static final String COMMENT = "comment";

  private final SqlUpdate sql;

  InsertAlertRecommendationsQuery(JdbcTemplate jdbcTemplate) {
    sql = new SqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(
        "INSERT INTO ae_recommendation ("
            + "analysis_id, alert_id, created_at, recommended_action, match_ids, "
            + "match_contexts, comment"
            + ")\n"
            + "VALUES ("
            + ":analysis_id, :alert_id, now(), :recommended_action, :match_ids, :match_contexts, "
            + ":comment)\n"
            + "ON CONFLICT DO NOTHING\n"
            + "RETURNING recommendation_id, analysis_id, alert_id;");
    sql.declareParameter(new SqlParameter(ALERT_ID_COLUMN, Types.BIGINT));
    sql.declareParameter(new SqlParameter(ANALYSIS_ID_COLUMN, Types.BIGINT));
    sql.declareParameter(new SqlParameter(RECOMMENDED_ACTION_COLUMN, Types.VARCHAR));
    sql.declareParameter(new SqlParameter(MATCH_IDS_COLUMN, Types.ARRAY));
    sql.declareParameter(new SqlParameter(MATCH_CONTEXTS_COLUMN, Types.OTHER));
    sql.declareParameter(new SqlParameter(COMMENT, Types.VARCHAR));
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
        Map.of(ALERT_ID_COLUMN, alertRecommendation.getAlertId(),
            ANALYSIS_ID_COLUMN, alertRecommendation.getAnalysisId(),
            RECOMMENDED_ACTION_COLUMN, alertRecommendation.getRecommendedAction(),
            MATCH_IDS_COLUMN, alertRecommendation.getMatchIds(),
            MATCH_CONTEXTS_COLUMN, writeMatchContexts(alertRecommendation.getMatchContexts()),
            COMMENT, alertRecommendation.getComment());

    sql.updateByNamedParam(paramMap, keyHolder);

    ofNullable(keyHolder.getKeys())
        .map(InsertAlertRecommendationsQuery::buildRecommendationResponse)
        .ifPresent(consumer);
  }

  private static void debug(final String message, final Object... parameters) {
    if (log.isDebugEnabled()) {
      log.debug(message, parameters);
    }
  }

  private static MatchContext[] readMatchContext(String matchContexts) {
    if (StringUtils.isBlank(matchContexts)) {
      debug("Match context is empty!");
      return new MatchContext[0];
    }

    try {
      return MAPPER.readValue(matchContexts, MatchContext[].class);
    } catch (JsonProcessingException e) {
      throw new MatchContextsJsonNodeReadException(e);
    }
  }

  @SuppressWarnings("FeatureEnvy")
  private static RecommendationResponse buildRecommendationResponse(Map<String, Object> it) {
    var analysisId = (long) it.get(ANALYSIS_ID_COLUMN);
    var recommendationId = (long) it.get("recommendation_id");
    var alertId = (long) it.get(ALERT_ID_COLUMN);
    var matchIds = ((long[]) it.get(MATCH_IDS_COLUMN));
    var matchContexts = (String) it.get(MATCH_CONTEXTS_COLUMN);
    var objectNodes = readMatchContext(matchContexts);
    var recommendationMetadata =
        transferToRecommendationMetaData(
            new AnalysisRecommendationContext(Arrays.asList(objectNodes), analysisId,
                recommendationId,
                alertId, matchIds));

    return RecommendationResponse
        .builder()
        .recommendationId(recommendationId)
        .analysisId(analysisId)
        .alertId(alertId)
        .metaData(recommendationMetadata)
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
