/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
class DatabaseJobsConsistencyHealthIndicator {

  final DataSource dataSource;

  private static final String TABLE_VIEW_QUERY = "SELECT tname FROM tab WHERE tname = '%s'";
  static final String PRESENT = "present";
  static final String NOT_PRESENT = "not-present";
  static final String DB_ERROR = "db-error";

  Map<String, String> verifyIfTableExists(String descriptionPrefix, String tableName) {
    String readableDescription = String.format("%s - %s", descriptionPrefix, tableName);

    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = prepareStatement(tableName, connection);
        ResultSet rs = preparedStatement.executeQuery()) {

      if (rs.next()) {
        return Map.of(readableDescription, PRESENT);
      }
      log.warn("HealthIndicator - {} table doesn't exist in a database", tableName);
      return Map.of(readableDescription, NOT_PRESENT);
    } catch (SQLException e) {
      log.debug("Database error while checking if table exists: {}", tableName, e);
      return Map.of(readableDescription, DB_ERROR);
    }
  }

  @SuppressFBWarnings(
      value = "SQL_INJECTION_JDBC",
      justification = "Validation is handled in OracleRelationName")
  private PreparedStatement prepareStatement(String tableName, Connection connection) throws
      SQLException {
    return connection.prepareStatement(String.format(TABLE_VIEW_QUERY, tableName));
  }
}
