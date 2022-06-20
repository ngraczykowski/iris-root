/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Slf4j
class ExternalIdsReaderFactory {

  private final String query;
  private final DataSource dataSource;
  private final int chunkSize;
  private final int queryTimeout;

  ExternalIdsReaderFactory(
      DataSource dataSource, String dbRelationName, int chunkSize, int queryTimeout) {
    this.dataSource = dataSource;
    this.chunkSize = chunkSize;
    this.query = buildQuery(dbRelationName);
    this.queryTimeout = queryTimeout;
  }

  private static String buildQuery(String dbRelationName) {
    return String.format(EcmQueryTemplates.EXTERNAL_IDS_QUERY, dbRelationName);
  }

  JdbcCursorItemReader<ExternalId> get() {
    return new JdbcCursorItemReaderBuilder<ExternalId>()
        .name("externalSystemIdsReader")
        .dataSource(dataSource)
        .fetchSize(chunkSize)
        .rowMapper(new ExternalIdRowMapper())
        .sql(query)
        .queryTimeout(queryTimeout)
        .build();
  }

  private static final class ExternalIdRowMapper implements RowMapper<ExternalId> {

    @Override
    public ExternalId mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ExternalId(
          rs.getString(ExternalId.SYSTEM_ID),
          ExternalId.tryToExtractWatchlistIdFromHitUniqueId(
              rs.getString(ExternalId.HIT_UNIQUE_ID)));
    }
  }
}
