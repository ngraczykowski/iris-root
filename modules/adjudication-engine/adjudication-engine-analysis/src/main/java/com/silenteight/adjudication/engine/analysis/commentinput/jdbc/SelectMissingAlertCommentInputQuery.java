package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.domain.MissingCommentInputsResult;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class SelectMissingAlertCommentInputQuery {

  private static final String MISSING_COMMENT_INPUTS =
      "SELECT alert_id"
          + " FROM ae_missing_alert_comment_input_query a"
          + " WHERE a.analysis_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final int maxRows;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  Optional<MissingCommentInputsResult> execute(long analysisId) {
    this.jdbcTemplate.setMaxRows(maxRows);

    return Optional.ofNullable(jdbcTemplate.query(
        MISSING_COMMENT_INPUTS, new SqlMissingCommentInputExtractor(), analysisId));
  }

  static class SqlMissingCommentInputExtractor implements
      ResultSetExtractor<MissingCommentInputsResult> {

    @Override
    public MissingCommentInputsResult extractData(ResultSet rs) throws SQLException {
      var alerts = new ArrayList<String>();

      while (rs.next()) {
        var alertId = rs.getObject(1, Long.class);
        alerts.add("alerts/" + alertId);
      }

      if (log.isTraceEnabled()) {
        log.trace("Missing comment inputs: alerts={}", alerts);
      }

      return new MissingCommentInputsResult(alerts);
    }
  }
}
