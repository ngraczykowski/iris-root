/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.MatchFeatureValue;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
class MatchFeatureJdbcRepository {

  private static final String INSERT_QUERY = """
    INSERT INTO ae_match_feature_value(match_id, agent_config_feature_id, created_at, value, reason)
    VALUES (?, ?, ?, ?, ?::jsonb) ON CONFLICT DO NOTHING""";
  private final JdbcTemplate jdbcTemplate;

  void save(MatchFeatureValue matchFeature) {
    jdbcTemplate.update(
        INSERT_QUERY,
        new Object[]{ matchFeature.matchId(),
                      matchFeature.agentConfigFeatureId(),
                      Timestamp.valueOf(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime()),
                      matchFeature.solution(),
                      matchFeature.reason()},
        new int[]{ Types.BIGINT,Types.BIGINT, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR});
  }
}
