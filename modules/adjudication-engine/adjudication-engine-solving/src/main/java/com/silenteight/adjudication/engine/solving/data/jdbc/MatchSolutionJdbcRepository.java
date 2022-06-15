/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchSolutionEntity;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
class MatchSolutionJdbcRepository {

  private static final String INSERT_QUERY = """
    INSERT INTO ae_match_solution
    (analysis_id, match_id, created_at, solution, reason, match_context)
      VALUES (?, ?, ?, ?, ?::jsonb, ?::jsonb) ON CONFLICT DO NOTHING;""";
  private final JdbcTemplate jdbcTemplate;

  public void save(MatchSolutionEntity entity) {
    jdbcTemplate.update(
        INSERT_QUERY,
        new Object[]{
            entity.analysisId(),
            entity.matchId(),
            Timestamp.valueOf(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime()),
            entity.solution(),
            entity.reason(),
            entity.matchContext()},
        new int[]{
            Types.BIGINT,
            Types.BIGINT,
            Types.TIMESTAMP,
            Types.VARCHAR,
            Types.OTHER,
            Types.OTHER});
  }
}
