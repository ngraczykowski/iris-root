package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.sql.DataSource;

@Slf4j
class SelectPendingAlertsQuery {

  private static final PendingAlertMapper ROW_MAPPER = new PendingAlertMapper();

  private final JdbcTemplate jdbcTemplate;

  SelectPendingAlertsQuery(@NonNull DataSource datasource, int fetchSize) {
    Preconditions.checkArgument(fetchSize > 0, "Fetch size %s must be greater than 0", fetchSize);

    jdbcTemplate = new JdbcTemplate(datasource, true);
    jdbcTemplate.setFetchSize(fetchSize);
    jdbcTemplate.setMaxRows(fetchSize);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  PendingAlerts execute(long analysisId) {
    var pendingAlerts = jdbcTemplate.query(
        "SELECT aamsq.alert_id,\n"
            + "       aamsq.match_solutions,\n"
            + "       aamsq.match_ids,\n"
            + "       aamsq.match_contexts::text,\n"
            + "       json_object_agg(coalesce(aal.name, ''), coalesce(aal.value, ''))\n"
            + "FROM ae_alert_match_solutions_query aamsq\n"
            + "         JOIN ae_pending_alert_matches_query apamq\n"
            + "              ON aamsq.alert_id = apamq.alert_id\n"
            + "                  AND aamsq.match_ids = apamq.match_ids\n"
            + "                  AND aamsq.analysis_id = apamq.analysis_id\n"
            + "         JOIN ae_alert_comment_input aaci ON apamq.alert_id = aaci.alert_id\n"
            + "         LEFT JOIN ae_alert_labels aal ON aamsq.alert_id = aal.alert_id\n"
            +
            "WHERE aamsq.analysis_id = ?\n"
            + "  AND NOT exists(\n"
            + "        SELECT 1\n"
            + "        FROM ae_recommendation ar\n"
            + "        WHERE aamsq.alert_id = ar.alert_id\n"
            + "          AND ar.analysis_id = ?)\n"
            + "GROUP BY 1, 2, 3, 4",
        ROW_MAPPER, analysisId, analysisId);

    log.info("Pending alerts size: {}", pendingAlerts.size());
    return new PendingAlerts(pendingAlerts);
  }

  private static final class PendingAlertMapper implements RowMapper<PendingAlert> {

    private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

    @Override
    public PendingAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
      var alertId = rs.getObject(1, Long.class);
      var matchSolutions = rs.getArray(2);
      var matchIds = rs.getArray(3);
      var matchContexts = rs.getString(4);
      var alertLabels = rs.getString(5);

      var solutions = Arrays
          .stream((String[]) matchSolutions.getArray())
          .map(FeatureVectorSolution::valueOf)
          .collect(Collectors.toList());

      return PendingAlert.builder()
          .alertId(alertId)
          .matchSolutions(solutions)
          .matchIds((long[]) ArrayUtils.toPrimitive(matchIds.getArray()))
          .matchContexts(readMatchContext(matchContexts))
          .alertLabels(readAlertLabels(alertLabels))
          .build();
    }

    private static ObjectNode[] readMatchContext(String matchContexts) {
      try {
        return MAPPER.readValue(matchContexts, ObjectNode[].class);
      } catch (JsonProcessingException e) {
        throw new MatchContextsJsonNodeReadException(e);
      }
    }

    private static ObjectNode readAlertLabels(String alertLabels) {
      try {
        return MAPPER.readValue(alertLabels, ObjectNode.class);
      } catch (JsonProcessingException e) {
        throw new MatchContextsJsonNodeReadException(e);
      }
    }
  }

  static class MatchIdsArrayReadException extends RuntimeException {

    private static final long serialVersionUID = 1116976327222398L;

    MatchIdsArrayReadException(Throwable cause) {
      super(cause);
    }
  }

  static class MatchContextsJsonNodeReadException extends RuntimeException {

    private static final long serialVersionUID = 3876426976327990398L;

    MatchContextsJsonNodeReadException(Throwable cause) {
      super(cause);
    }
  }
}
