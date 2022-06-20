/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.decisiongroups;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class JdbcDecisionGroupsReader implements DecisionGroupsReader {

  static final String DEFAULT_RELATION_NAME = "SENS_V_FFF_RECORDS";

  private static final String DEFAULT_QUERY = "SELECT DISTINCT unit FROM %s";

  @NonNull
  private final JdbcTemplate template;

  private final DecisionGroupRowMapper rowMapper = new DecisionGroupRowMapper();

  @NonNull
  @Setter
  private String relationName = DEFAULT_RELATION_NAME;

  @Setter
  private String query;

  @Override
  public Collection<String> readAll() {
    return template.query(getQuery(), rowMapper);
  }

  private String getQuery() {
    if (StringUtils.isNotEmpty(query))
      return query;

    if (StringUtils.isBlank(relationName))
      throw new IllegalStateException("Relation name must not be blank");

    return String.format(DEFAULT_QUERY, relationName);
  }

  private static class DecisionGroupRowMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(1);
    }
  }
}
