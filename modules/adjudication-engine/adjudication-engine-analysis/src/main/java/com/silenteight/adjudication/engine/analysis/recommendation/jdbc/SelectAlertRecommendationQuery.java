package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
class SelectAlertRecommendationQuery {

  private static final AlertRecommendationMapper ROW_MAPPER = new AlertRecommendationMapper();

  private final JdbcTemplate jdbcTemplate;

  List<AlertRecommendation> execute(long analysisId) {
    return jdbcTemplate.query(
        "SELECT * FROM ae_comments_context acc WHERE acc.analysis_id = ?",
        ROW_MAPPER, analysisId);
  }

  List<AlertRecommendation> execute(long analysisId, long datasetId) {
    return jdbcTemplate.query(
        "SELECT *\n"
            + "FROM ae_comments_context acc WHERE acc.analysis_id = ?\n"
            + "  AND acc.alert_id "
            + "  IN (SELECT alert_id FROM ae_dataset_alert WHERE dataset_id = ?)\n",
        ROW_MAPPER, analysisId, datasetId);
  }

  private static final class AlertRecommendationMapper implements RowMapper<AlertRecommendation> {

    private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

    public AlertRecommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
      MatchContext[] matches;
      HashMap<String, Object> categories;

      try {
        matches = OBJECT_MAPPER.readValue(rs.getString(8), MatchContext[].class);
        categories = OBJECT_MAPPER.readValue(rs.getString(6), HashMap.class);
      } catch (JsonProcessingException e) {
        throw new DataRetrievalFailureException("Failed to parse JSON values", e);
      }

      var alertContext = new AlertContext(
          rs.getString(5), categories, rs.getString(7),
          List.of(matches));

      return new AlertRecommendation(rs.getLong(1),
          rs.getLong(2), rs.getLong(3), rs.getTimestamp(4), alertContext);
    }
  }
}
