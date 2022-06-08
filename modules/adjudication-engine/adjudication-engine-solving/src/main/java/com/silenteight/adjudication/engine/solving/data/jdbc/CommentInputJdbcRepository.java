/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;


import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
class CommentInputJdbcRepository {
  private final JdbcTemplate jdbcTemplate;

  private static final String SQL = """
      INSERT INTO ae_alert_comment_input(alert_id, created_at, value)
      VALUES (?, ?, ?::jsonb)
      ON CONFLICT DO NOTHING
      """;

  public void store(CommentInput commentInput) {
    jdbcTemplate.update(SQL,
        new Object[]{ commentInput.alertId(),
                      Timestamp.valueOf(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime()),
                      commentInput.value()},
        new int[]{Types.BIGINT,Types.TIMESTAMP,Types.VARCHAR});
  }
}
