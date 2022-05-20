package com.silenteight.adjudication.engine.comments.commentinput.jdbc;

import com.silenteight.adjudication.engine.comments.commentinput.domain.InsertCommentInputRequest;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.Types;

class InsertCommentInputQuery {

  private final SqlUpdate sql;

  InsertCommentInputQuery(JdbcTemplate jdbcTemplate) {
    sql = new SqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_alert_comment_input(alert_id, created_at, value)\n"
        + "VALUES (?, now(), ?::jsonb)\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("alert_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("value", Types.VARCHAR));

    sql.compile();
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  void insert(InsertCommentInputRequest request) {
    sql.update(request.getAlertId(), request.getValue().toString());
  }
}
