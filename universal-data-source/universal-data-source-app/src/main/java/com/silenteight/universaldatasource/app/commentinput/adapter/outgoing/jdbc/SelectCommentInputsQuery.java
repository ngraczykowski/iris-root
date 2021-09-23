package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
class SelectCommentInputsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT comment_input_id, alert, alert_comment_input, match_comment_inputs\n"
          + " FROM uds_comment_input\n"
          + " WHERE alert IN (:alerts)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  List<AlertCommentInput> execute(Collection<String> alerts) {

    var parameters = new MapSqlParameterSource("alerts", alerts);
    return jdbcTemplate.query(SQL, parameters, new CommentInputRowMapper());

  }

  private static final class CommentInputRowMapper implements RowMapper<AlertCommentInput> {

    @Override
    public AlertCommentInput mapRow(ResultSet rs, int rowNum) throws SQLException {
      return AlertCommentInput.builder()
          .commentInputId(rs.getString(1))
          .alert(rs.getString(2))
          .commentInput(rs.getString(3))
          .matchCommentInputs(rs.getString(4))
          .build();
    }
  }
}
