/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Clock;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
class MatchCategoryJdbcRepository {
  private static final String INSERT_QUERY = """
    INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)
    VALUES (?, (select category_id from ae_category where category = ?), ?, ?)
    ON CONFLICT DO NOTHING""";
  private final JdbcTemplate jdbcTemplate;

  void save(MatchCategory matchCategory) {
    jdbcTemplate.update(
        INSERT_QUERY,
        new Object[]{ matchCategory.getMatchId(),
                      matchCategory.getCategory(),
                      Timestamp.valueOf(OffsetDateTime.now(Clock.systemUTC()).toLocalDateTime()),
                      matchCategory.getCategoryValue()},
        new int[]{ Types.BIGINT,Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR});
  }
}
