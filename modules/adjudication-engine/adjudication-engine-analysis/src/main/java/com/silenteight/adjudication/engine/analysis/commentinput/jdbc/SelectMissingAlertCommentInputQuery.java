package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.MissingCommentInputsResult;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class SelectMissingAlertCommentInputQuery {

  private static final String MISSING_COMMENT_INPUTS = "SELECT alert_id"
      + " FROM ae_missing_alert_comment_input_query a"
      + " WHERE a.analysis_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final int maxRows;

  Optional<MissingCommentInputsResult> execute(long analysisId) {
    this.jdbcTemplate.setMaxRows(maxRows);

    return Optional.ofNullable(
        jdbcTemplate.query(MISSING_COMMENT_INPUTS, new Object[] { analysisId },
            new SqlMissingCommentInputExtractor()));
  }

  static class SqlMissingCommentInputExtractor implements
      ResultSetExtractor<MissingCommentInputsResult> {

    @Override
    public MissingCommentInputsResult extractData(ResultSet rs) throws SQLException {
      MissingCommentInputsResult result = new MissingCommentInputsResult(new ArrayList<>());

      while (rs.next()) {
        var alertId = rs.getLong("alert_id");
        var resultResource = "alerts/" + alertId;
        result.addAlert(resultResource);
      }
      log.debug("Missing match category:{}", result);
      return result;
    }
  }
}
