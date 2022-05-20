package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;
import com.silenteight.adjudication.engine.analysis.recommendation.jdbc.SelectPendingAlertsQuery.MatchContextsJsonNodeReadException;
import com.silenteight.adjudication.engine.analysis.recommendation.jdbc.SelectPendingAlertsQuery.MatchIdsArrayReadException;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.dto.AnalysisRecommendationContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;
import java.util.function.Consumer;

import static com.silenteight.adjudication.engine.analysis.recommendation.transform.AlertMetaDataTransformer.transferToRecommendationMetaData;
import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromSqlTimestamp;
import static java.util.Optional.ofNullable;

//TODO (iwnek) Use SQL Bulk update
@Slf4j
class InsertAlertRecommendationsQuery {

  public static final String MATCH_CONTEXTS_COLUMN = "match_contexts";
  public static final String MATCH_IDS_COLUMN = "match_ids";
  public static final String RECOMMENDED_ACTION_COLUMN = "recommended_action";
  public static final String ANALYSIS_ID_COLUMN = "analysis_id";
  public static final String ALERT_ID_COLUMN = "alert_id";
  public static final String CREATED_AT_COLUMN = "created_at";
  public static final String COMMENT_COLUMN = "comment";
  public static final String MATCH_COMMENTS_COLUMN = "match_comments";
  public static final String ALERT_LABELS_COLUMN = "alert_labels";
  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private final JdbcTemplate jdbcTemplate;

  InsertAlertRecommendationsQuery(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
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

  private static Map<String, String> readMap(String value) {
    if (StringUtils.isBlank(value)) {
      debug("Value in reading recommendation is empty!");
      return Map.of("", "");
    }

    try {
      var typeRef = new TypeReference<HashMap<String, String>>() {};
      return MAPPER.readValue(value, typeRef);
    } catch (JsonProcessingException e) {
      throw new MatchContextsJsonNodeReadException(e);
    }
  }

  @SuppressWarnings("FeatureEnvy")
  private static RecommendationResponse buildRecommendationResponse(Map<String, Object> it) {
    var analysisId = (long) it.get(ANALYSIS_ID_COLUMN);
    var recommendationId = (long) it.get("recommendation_id");
    var alertId = (long) it.get(ALERT_ID_COLUMN);
    var matchIds = readMatchIds((Array) it.get(MATCH_IDS_COLUMN));
    var objectNodes = readMatchContext((String) it.get(MATCH_CONTEXTS_COLUMN));
    var createdAt = (Timestamp) it.get(CREATED_AT_COLUMN);
    var recommendedAction = (String) it.get(RECOMMENDED_ACTION_COLUMN);
    var comment = (String) it.get(COMMENT_COLUMN);
    var matchComments = readMap(it.get(MATCH_COMMENTS_COLUMN).toString());
    var alertLabels = readMap(it.get(ALERT_LABELS_COLUMN).toString());
    var recommendationMetadata =
        transferToRecommendationMetaData(
            new AnalysisRecommendationContext(Arrays.asList(objectNodes), analysisId,
                recommendationId,
                alertId, matchIds, matchComments, alertLabels));
    var recommendation = Recommendation
        .newBuilder()
        .setAlert("alerts/" + alertId)
        .setName("analysis/" + alertId + "/recommendations/" + recommendationId)
        .setCreateTime(fromSqlTimestamp(createdAt))
        .setRecommendedAction(recommendedAction)
        .setRecommendationComment(comment)
        .build();

    return RecommendationResponse
        .builder()
        .recommendationId(recommendationId)
        .analysisId(analysisId)
        .alertId(alertId)
        .metaData(recommendationMetadata)
        .recommendation(recommendation)
        .build();
  }

  private static long[] readMatchIds(Array o) {
    if (o == null) {
      debug("Match Ids array is empty");
      return new long[0];
    }
    try {
      return ArrayUtils.toPrimitive((Long[]) o.getArray());
    } catch (SQLException e) {
      log.error("Could not get matchIds:", e);
      throw new MatchIdsArrayReadException(e);
    }
  }

  private static String writeMatchContext(ObjectNode[] matchContexts) {
    try {
      return MAPPER.writeValueAsString(matchContexts);
    } catch (JsonProcessingException e) {
      throw new MatchContextsJsonNodeWriteException(e);
    }
  }

  private static String writeAlertLabels(ObjectNode alertLabels) {
    try {
      return MAPPER.writeValueAsString(alertLabels);
    } catch (JsonProcessingException e) {
      throw new MatchContextsJsonNodeWriteException(e);
    }
  }

  private static String writeMatchComments(Map<String, String> comments) {
    try {
      return MAPPER.writeValueAsString(comments);
    } catch (JsonProcessingException e) {
      throw new MatchCommentsJsonNodeWriteException(e);
    }
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<RecommendationResponse> execute(Collection<InsertRecommendationRequest> requests) {
    List<RecommendationResponse> responses = new ArrayList<>();
    var sql = createQuery();
    requests.forEach(r -> update(r, responses::add, sql));
    sql.flush();
    return responses;
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(
      InsertRecommendationRequest alertRecommendation,
      Consumer<RecommendationResponse> consumer, BatchSqlUpdate sql) {

    var keyHolder = new GeneratedKeyHolder();
    var paramMap =
        Map.of(ALERT_ID_COLUMN, alertRecommendation.getAlertId(),
            ANALYSIS_ID_COLUMN, alertRecommendation.getAnalysisId(),
            RECOMMENDED_ACTION_COLUMN, alertRecommendation.getRecommendedAction(),
            MATCH_IDS_COLUMN, alertRecommendation.getMatchIds(),
            MATCH_CONTEXTS_COLUMN, writeMatchContext(alertRecommendation.getMatchContexts()),
            COMMENT_COLUMN, alertRecommendation.getComment(),
            MATCH_COMMENTS_COLUMN, writeMatchComments(alertRecommendation.getMatchComments()),
            ALERT_LABELS_COLUMN, writeAlertLabels(alertRecommendation.getAlertLabels()));
    sql.updateByNamedParam(paramMap, keyHolder);

    ofNullable(keyHolder.getKeys())
        .map(InsertAlertRecommendationsQuery::buildRecommendationResponse)
        .ifPresent(consumer);
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(
        "INSERT INTO ae_recommendation ("
            + "analysis_id, alert_id, created_at, recommended_action, match_ids,\n"
            + "                               "
            + "match_contexts, comment, match_comments, alert_labels"
            + ")\n"
            + "VALUES ("
            + ":analysis_id, :alert_id, now(), :recommended_action, :match_ids, :match_contexts, "
            + ":comment,\n"
            + "        :match_comments, :alert_labels)\n"
            + "ON CONFLICT DO NOTHING\n"
            + "RETURNING recommendation_id, analysis_id, alert_id, created_at, "
            + "recommended_action,match_ids,match_contexts::text, comment, \n"
            + "    match_comments, alert_labels;");
    sql.declareParameter(new SqlParameter(ALERT_ID_COLUMN, Types.BIGINT));
    sql.declareParameter(new SqlParameter(ANALYSIS_ID_COLUMN, Types.BIGINT));
    sql.declareParameter(new SqlParameter(RECOMMENDED_ACTION_COLUMN, Types.VARCHAR));
    sql.declareParameter(new SqlParameter(MATCH_IDS_COLUMN, Types.ARRAY));
    sql.declareParameter(new SqlParameter(MATCH_CONTEXTS_COLUMN, Types.OTHER));
    sql.declareParameter(new SqlParameter(COMMENT_COLUMN, Types.VARCHAR));
    sql.declareParameter(new SqlParameter(MATCH_COMMENTS_COLUMN, Types.OTHER));
    sql.declareParameter(new SqlParameter(ALERT_LABELS_COLUMN, Types.OTHER));
    sql.setReturnGeneratedKeys(true);

    sql.compile();

    return sql;
  }

  static class MatchContextsJsonNodeWriteException extends RuntimeException {

    private static final long serialVersionUID = 2343279004526563617L;

    MatchContextsJsonNodeWriteException(Throwable cause) {
      super(cause);
    }
  }

  static class MatchCommentsJsonNodeWriteException extends RuntimeException {

    private static final long serialVersionUID = -478919687305591932L;

    MatchCommentsJsonNodeWriteException(Throwable cause) {
      super(cause);
    }
  }
}
