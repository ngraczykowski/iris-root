package com.silenteight.sep.base.common.batch.reader;

import lombok.NonNull;

import org.springframework.batch.item.database.AbstractCursorItemReader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nonnull;

public class BetterJdbcCursorItemReader<T> extends AbstractCursorItemReader<T> {

  private static final String OPEN_CURSOR_TASK = "Executing query";

  private PreparedStatement preparedStatement;

  private PreparedStatementSetter preparedStatementSetter;

  private String sql;

  private RowMapper<T> rowMapper;

  public BetterJdbcCursorItemReader() {
    super();
    setName(ClassUtils.getShortName(BetterJdbcCursorItemReader.class));
  }

  // NOTE(ahaczewski): Required to make sure no subclass will override setName() as it is used
  //  from the constructor.
  @Override
  public final void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the RowMapper to be used for all calls to read().
   *
   * @param rowMapper
   *     the mapper used to map each item
   */
  public void setRowMapper(@NonNull RowMapper<T> rowMapper) {
    this.rowMapper = rowMapper;
  }

  /**
   * Set the SQL statement to be used when creating the cursor. This statement should be a complete
   * and valid SQL statement, as it will be run directly without any modification.
   *
   * @param sql
   *     SQL statement
   */
  public void setSql(@NonNull String sql) {
    this.sql = sql;
  }

  /**
   * Set the PreparedStatementSetter to use if any parameter values that need to be set in the
   * supplied query.
   *
   * @param preparedStatementSetter
   *     PreparedStatementSetter responsible for filling out the statement
   */
  public void setPreparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
    this.preparedStatementSetter = preparedStatementSetter;
  }

  /**
   * Assert that mandatory properties are set.
   *
   * @throws IllegalArgumentException
   *     if either data source or SQL properties not set.
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    Assert.notNull(sql, "The SQL query must be provided");
    Assert.notNull(rowMapper, "RowMapper must be provided");
  }

  @Override
  protected void openCursor(Connection con) {
    try {
      if (isUseSharedExtendedConnection()) {
        preparedStatement =
            con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
      } else {
        preparedStatement =
            con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      }
      applyStatementSettings(preparedStatement);
      if (preparedStatementSetter != null)
        preparedStatementSetter.setValues(preparedStatement);
      rs = preparedStatement.executeQuery();
      handleWarnings(preparedStatement);
    } catch (SQLException se) {
      close();

      DataAccessException translatedException =
          getExceptionTranslator().translate(OPEN_CURSOR_TASK, getSql(), se);

      if (translatedException != null)
        throw translatedException;
      else
        throw new UncategorizedSQLException(OPEN_CURSOR_TASK, getSql(), se);
    }

  }

  @Override
  protected void doClose() throws Exception {
    try {
      super.doClose();
    } catch (SQLException se) {
      // NOTE(ahaczewski): SQLException might get thrown when closing, as the doClose() call
      //  might come from the fact that the SQLConnection was broken. Ignoring SQLException is the
      //  best thing to do in such a case, to allow for propagating correct exception out the
      //  call chain.
      if (log.isTraceEnabled())
        log.trace("SQL exception on closing reader Connection", se);
    }
  }

  @Override
  protected T readCursor(ResultSet rs, int currentRow) throws SQLException {
    return rowMapper.mapRow(rs, currentRow);
  }

  /**
   * Close the cursor and database connection.
   *
   * @deprecated This method is deprecated in favor of
   * {@link BetterJdbcCursorItemReader#cleanupOnClose(java.sql.Connection)}
   *     and will be removed in a future release
   */
  @Override
  @Deprecated(since = "1.11.0", forRemoval = true)
  protected void cleanupOnClose() throws Exception {
    JdbcUtils.closeStatement(this.preparedStatement);
  }

  /**
   * Close the cursor and database connection.
   * @param connection to the database
   */
  @Override
  protected void cleanupOnClose(Connection connection) throws Exception {
    JdbcUtils.closeStatement(this.preparedStatement);
    JdbcUtils.closeConnection(connection);
  }

  @Nonnull
  @Override
  public String getSql() {
    return sql;
  }
}
