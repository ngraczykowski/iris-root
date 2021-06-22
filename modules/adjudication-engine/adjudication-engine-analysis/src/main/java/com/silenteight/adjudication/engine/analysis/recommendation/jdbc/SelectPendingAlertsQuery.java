package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.NonNull;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.sql.DataSource;

class SelectPendingAlertsQuery {

  private static final PendingAlertMapper ROW_MAPPER = new PendingAlertMapper();

  private final JdbcTemplate jdbcTemplate;

  SelectPendingAlertsQuery(@NonNull DataSource datasource, int fetchSize) {
    Preconditions.checkArgument(fetchSize > 0, "Fetch size %s must be greater than 0", fetchSize);

    jdbcTemplate = new JdbcTemplate(datasource, true);
    jdbcTemplate.setFetchSize(fetchSize);
    jdbcTemplate.setMaxRows(fetchSize);
  }

  PendingAlerts execute(long analysisId) {
    var pendingAlerts = jdbcTemplate.query(
        "SELECT aamsq.alert_id, aamsq.match_solution\n"
            + "FROM ae_alert_match_solutions_query aamsq\n"
            + "         JOIN ae_alert_matches_query aamq\n"
            + "              ON aamsq.alert_id = aamq.alert_id\n"
            + "                  AND aamsq.match_ids = aamq.match_ids\n"
            + "         JOIN ae_alert_comment_input aaci ON aamq.alert_id = aaci.alert_id\n"
            + "         LEFT JOIN ae_recommendation ar\n"
            + "                   on aamsq.alert_id = ar.alert_id"
            + " AND aamsq.analysis_id = ar.analysis_id\n"
            + "WHERE aamsq.analysis_id = ?\n"
            + "  AND ar.recommendation_id IS NULL",
        ROW_MAPPER, analysisId);

    return new PendingAlerts(pendingAlerts);
  }

  private static final class PendingAlertMapper implements RowMapper<PendingAlert> {

    @Override
    public PendingAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
      Long alertId = rs.getObject(1, Long.class);
      Array matchSolutions = rs.getArray(2);
      // TODO: map column 3 (JSONB) to ObjectNode or Map<>.

      var solutions = Arrays
          .stream((String[]) matchSolutions.getArray())
          .map(FeatureVectorSolution::valueOf)
          .collect(Collectors.toList());

      return new PendingAlert(alertId, solutions);
    }
  }
}
