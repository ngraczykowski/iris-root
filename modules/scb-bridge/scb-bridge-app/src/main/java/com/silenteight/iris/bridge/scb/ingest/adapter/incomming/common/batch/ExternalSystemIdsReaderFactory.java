/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.QueryStatementHelper;
import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;
import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReaderBuilder;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Slf4j
class ExternalSystemIdsReaderFactory {

  private final String query;
  private final DataSource dataSource;
  private final int chunkSize;
  private final int queryTimeout;

  ExternalSystemIdsReaderFactory(
      DataSource dataSource, String dbRelationName, int chunkSize, int queryTimeout) {
    this.dataSource = dataSource;
    this.chunkSize = chunkSize;
    this.query = buildQuery(dbRelationName);
    this.queryTimeout = queryTimeout;
  }

  private static String buildQuery(String dbRelationName) {
    return QueryTemplates.SYSTEM_IDS_QUERY
        .replace(QueryStatementHelper.DB_RELATION_NAME_PARAM, dbRelationName);
  }

  BetterJdbcCursorItemReader<String> get() {
    return new BetterJdbcCursorItemReaderBuilder<String>()
        .name("externalSystemIdsReader")
        .dataSource(dataSource)
        .fetchSize(chunkSize)
        .rowMapper(new SystemIdRowMapper())
        .sql(query)
        .queryTimeout(queryTimeout)
        .build();
  }

  private static final class SystemIdRowMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString("SYSTEM_ID");
    }
  }
}
