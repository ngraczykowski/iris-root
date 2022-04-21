package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class AlertRecommendationMapper implements RowMapper<AlertRecommendation> {

  static final AlertRecommendationMapper INSTANCE = new AlertRecommendationMapper();

  private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private static final MapType MAP_TYPE = JsonConversionHelper.INSTANCE
      .objectMapper()
      .getTypeFactory()
      .constructMapType(LinkedHashMap.class, String.class, Object.class);
  private static final MapType STRING_MAP_TYPE = JsonConversionHelper.INSTANCE
      .objectMapper()
      .getTypeFactory()
      .constructMapType(LinkedHashMap.class, String.class, Object.class);

  @SuppressWarnings("FeatureEnvy")
  @Override
  public AlertRecommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
    MatchContext[] matches;
    Map<String, String> matchComments;

    try {
      matches = OBJECT_MAPPER.readValue(rs.getString("match_contexts"), MatchContext[].class);
      matchComments = OBJECT_MAPPER.readValue(rs.getString("match_comments"), STRING_MAP_TYPE);
    } catch (JsonProcessingException e) {
      throw new DataRetrievalFailureException("Failed to parse JSON values", e);
    }

    var alertContext = AlertContext.builder()
        .alertId(rs.getString("client_alert_identifier"))
        .recommendedAction(rs.getString("recommended_action"))
        .matches(List.of(matches))
        .build();

    return AlertRecommendation.builder()
        .alertId(rs.getLong("alert_id"))
        .analysisId(rs.getLong("analysis_id"))
        .recommendationId(rs.getLong("recommendation_id"))
        .createdTime(rs.getTimestamp("created_at"))
        .alertContext(alertContext)
        .matchIds((long[]) ArrayUtils.toPrimitive(rs.getArray("match_ids").getArray()))
        .comment(rs.getString("comment"))
        .matchComments(matchComments)
        .build();
  }
}
