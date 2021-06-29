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

  @SuppressWarnings("FeatureEnvy")
  @Override
  public AlertRecommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
    MatchContext[] matches;
    Map<String, Object> commentInput;

    try {
      commentInput = OBJECT_MAPPER.readValue(rs.getString(6), MAP_TYPE);
      matches = OBJECT_MAPPER.readValue(rs.getString(8), MatchContext[].class);
    } catch (JsonProcessingException e) {
      throw new DataRetrievalFailureException("Failed to parse JSON values", e);
    }

    var alertContext = AlertContext.builder()
        .alertId(rs.getString(5))
        .commentInput(commentInput)
        .recommendedAction(rs.getString(7))
        .matches(List.of(matches))
        .build();

    return AlertRecommendation.builder()
        .alertId(rs.getLong(1))
        .analysisId(rs.getLong(2))
        .recommendationId(rs.getLong(3))
        .createdTime(rs.getTimestamp(4))
        .alertContext(alertContext)
        .matchIds((long[]) ArrayUtils.toPrimitive(rs.getArray(9).getArray()))
        .build();
  }
}
