package com.silenteight.warehouse.report.sql;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;
import com.silenteight.warehouse.report.sql.exception.SqlExecutorException;

import org.postgresql.copy.CopyManager;
import org.postgresql.copy.CopyOut;
import org.postgresql.copy.PGCopyInputStream;
import org.postgresql.core.BaseConnection;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;
import javax.sql.DataSource;
import javax.validation.Valid;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class SqlExecutorService implements SqlExecutor {

  @Valid
  @NonNull
  private final SqlExecutorProperties properties;
  @NonNull
  private final DataSource dataSource;

  @Transactional
  @Override
  public void execute(SqlExecutorDto sqlDto, Consumer<InputStream> consumer) {
    try (
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {

      log.debug("Started processing sql");
      InputStream inputStream = doExecute(connection, statement, sqlDto);
      consumer.accept(inputStream);
      log.debug("Processing Sql Executor ended");
    } catch (SQLException e) {
      throw new SqlExecutorException(e);
    }
  }

  private InputStream doExecute(Connection connection, Statement statement, SqlExecutorDto sqlDto)
      throws SQLException {

    log.debug("PrepareDataSqlStatements {}", sqlDto.getPrepareDataSqlStatements());
    prepareData(statement, sqlDto.getPrepareDataSqlStatements());
    String copyOutSql = prepareCopyOutSql(sqlDto.getSelectSqlStatement());
    log.debug("copyOutSql {}", copyOutSql);
    return copyOutToInputStream(connection, copyOutSql);
  }

  private static void prepareData(Statement statement, List<String> sqlStatements) throws
      SQLException {

    for (String sql : sqlStatements) {
      statement.addBatch(sql);
    }
    statement.executeBatch();
  }

  private String prepareCopyOutSql(String sql) {
    String copyCsvPattern = properties.getCopyCsvPattern();
    return format(copyCsvPattern, sql);
  }

  private static PGCopyInputStream copyOutToInputStream(Connection connection, String copyOutSql)
      throws SQLException {

    CopyManager copyManager = new CopyManager(connection.unwrap(BaseConnection.class));
    CopyOut copyOut = copyManager.copyOut(copyOutSql);
    return new PGCopyInputStream(copyOut);
  }
}
