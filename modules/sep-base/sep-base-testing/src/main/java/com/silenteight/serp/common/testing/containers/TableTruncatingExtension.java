package com.silenteight.serp.common.testing.containers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import static java.lang.String.format;
import static java.lang.String.join;

@RequiredArgsConstructor
@Slf4j
public class TableTruncatingExtension implements AfterEachCallback {

  private static final String TABLE_NAME_COLUMN = "TABLE_NAME";

  private final String tableNamePrefix;
  private final DataSource dataSource;

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    truncatePrefixedTables();
  }

  private void truncatePrefixedTables() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      List<String> tablesToTruncate = fetchTableNames(conn);
      executeTruncateQuery(conn, tablesToTruncate);
      conn.commit();
    }
  }

  private static void executeTruncateQuery(Connection conn, List<String> tables)
      throws SQLException {
    try (PreparedStatement statement = conn.prepareStatement(buildQuery(tables))) {
      statement.execute();
      log.info("Truncating tables {}", tables);
    }
  }

  private static String buildQuery(List<String> tables) {
    return format("TRUNCATE %s RESTART IDENTITY CASCADE", join(", ", tables));
  }

  private List<String> fetchTableNames(Connection connection) throws SQLException {
    DatabaseMetaData dbMetaData = connection.getMetaData();
    ResultSet tablesInDb = queryDbForTables(connection, dbMetaData);

    List<String> tableNames = new ArrayList<>();
    while (tablesInDb.next()) {
      String tableName = tablesInDb.getString(TABLE_NAME_COLUMN);

      if (tableName.startsWith(tableNamePrefix)) {
        tableNames.add(tableName);
      }
    }

    return tableNames;
  }

  private static ResultSet queryDbForTables(Connection connection, DatabaseMetaData dbMetaData)
      throws SQLException {
    return dbMetaData.getTables(
        connection.getCatalog(), null, null, new String[]{ "TABLE" });
  }
}
