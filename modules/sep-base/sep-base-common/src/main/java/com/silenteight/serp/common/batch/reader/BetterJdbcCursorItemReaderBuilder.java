package com.silenteight.serp.common.batch.reader;

import lombok.Setter;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.AbstractCursorItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.jdbc.core.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import javax.sql.DataSource;

@Setter
public class BetterJdbcCursorItemReaderBuilder<T> {

  private DataSource dataSource;

  private int fetchSize = AbstractCursorItemReader.VALUE_NOT_SET;

  private int maxRows = AbstractCursorItemReader.VALUE_NOT_SET;

  private int queryTimeout = AbstractCursorItemReader.VALUE_NOT_SET;

  private boolean ignoreWarnings;

  private boolean verifyCursorPosition;

  private boolean driverSupportsAbsolute;

  private boolean useSharedExtendedConnection;

  private PreparedStatementSetter preparedStatementSetter;

  private String sql;

  private RowMapper<T> rowMapper;

  private boolean saveState = true;

  private String name;

  private int maxItemCount = Integer.MAX_VALUE;

  private int currentItemCount;

  /**
   * Configure if the state of the {@link org.springframework.batch.item.ItemStreamSupport} should
   * be persisted within the {@link ExecutionContext} for restart purposes.
   *
   * @param saveState
   *     defaults to true
   *
   * @return The current instance of the builder.
   */
  public BetterJdbcCursorItemReaderBuilder<T> saveState(boolean saveState) {
    this.saveState = saveState;

    return this;
  }

  /**
   * The name used to calculate the key within the {@link ExecutionContext}. Required if {@link
   * #saveState(boolean)} is set to true.
   *
   * @param name
   *     name of the reader instance
   *
   * @return The current instance of the builder.
   *
   * @see org.springframework.batch.item.ItemStreamSupport#setName(String)
   */
  public BetterJdbcCursorItemReaderBuilder<T> name(String name) {
    this.name = name;

    return this;
  }

  /**
   * Configure the max number of items to be read.
   *
   * @param maxItemCount
   *     the max items to be read
   *
   * @return The current instance of the builder.
   *
   * @see AbstractItemCountingItemStreamItemReader#setMaxItemCount(int)
   */
  public BetterJdbcCursorItemReaderBuilder<T> maxItemCount(int maxItemCount) {
    this.maxItemCount = maxItemCount;

    return this;
  }

  /**
   * Index for the current item. Used on restarts to indicate where to start from.
   *
   * @param currentItemCount
   *     current index
   *
   * @return this instance for method chaining
   *
   * @see AbstractItemCountingItemStreamItemReader#setCurrentItemCount(int)
   */
  public BetterJdbcCursorItemReaderBuilder<T> currentItemCount(int currentItemCount) {
    this.currentItemCount = currentItemCount;

    return this;
  }

  /**
   * The {@link DataSource} to read from
   *
   * @param dataSource
   *     a relational data base
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setDataSource(DataSource)
   */
  public BetterJdbcCursorItemReaderBuilder<T> dataSource(DataSource dataSource) {
    this.dataSource = dataSource;

    return this;
  }

  /**
   * A hint to the driver as to how many rows to return with each fetch.
   *
   * @param fetchSize
   *     the hint
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setFetchSize(int)
   */
  public BetterJdbcCursorItemReaderBuilder<T> fetchSize(int fetchSize) {
    this.fetchSize = fetchSize;

    return this;
  }

  /**
   * The max number of rows the {@link java.sql.ResultSet} can contain
   *
   * @param maxRows
   *     the max
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setMaxRows(int)
   */
  public BetterJdbcCursorItemReaderBuilder<T> maxRows(int maxRows) {
    this.maxRows = maxRows;

    return this;
  }

  /**
   * The time in milliseconds for the query to timeout
   *
   * @param queryTimeout
   *     timeout
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setQueryTimeout(int)
   */
  public BetterJdbcCursorItemReaderBuilder<T> queryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;

    return this;
  }

  public BetterJdbcCursorItemReaderBuilder<T> ignoreWarnings(boolean ignoreWarnings) {
    this.ignoreWarnings = ignoreWarnings;

    return this;
  }

  /**
   * Indicates if the reader should verify the current position of the {@link java.sql.ResultSet}
   * after being passed to the {@link RowMapper}.  Defaults to true.
   *
   * @param verifyCursorPosition
   *     indicator
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setVerifyCursorPosition(boolean)
   */
  public BetterJdbcCursorItemReaderBuilder<T> verifyCursorPosition(boolean verifyCursorPosition) {
    this.verifyCursorPosition = verifyCursorPosition;

    return this;
  }

  /**
   * Indicates if the JDBC driver supports setting the absolute row on the {@link
   * java.sql.ResultSet}.
   *
   * @param driverSupportsAbsolute
   *     indicator
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setDriverSupportsAbsolute(boolean)
   */
  public BetterJdbcCursorItemReaderBuilder<T> driverSupportsAbsolute(
      boolean driverSupportsAbsolute) {
    this.driverSupportsAbsolute = driverSupportsAbsolute;

    return this;
  }

  /**
   * Indicates that the connection used for the cursor is being used by all other processing,
   * therefor part of the same transaction.
   *
   * @param useSharedExtendedConnection
   *     indicator
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setUseSharedExtendedConnection(boolean)
   */
  public BetterJdbcCursorItemReaderBuilder<T> useSharedExtendedConnection(
      boolean useSharedExtendedConnection) {
    this.useSharedExtendedConnection = useSharedExtendedConnection;

    return this;
  }

  /**
   * Configures the provided {@link PreparedStatementSetter} to be used to populate any arguments in
   * the SQL query to be executed for the reader.
   *
   * @param preparedStatementSetter
   *     setter
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setPreparedStatementSetter(PreparedStatementSetter)
   */
  public BetterJdbcCursorItemReaderBuilder<T> preparedStatementSetter(
      PreparedStatementSetter preparedStatementSetter) {
    this.preparedStatementSetter = preparedStatementSetter;

    return this;
  }

  /**
   * Configures a {@link PreparedStatementSetter} that will use the array as the values to be set on
   * the query to be executed for this reader.
   *
   * @param args
   *     values to set on the reader query
   *
   * @return this instance for method chaining
   */
  public BetterJdbcCursorItemReaderBuilder<T> queryArguments(Object[] args) {
    this.preparedStatementSetter = new ArgumentPreparedStatementSetter(args);

    return this;
  }

  /**
   * Configures a {@link PreparedStatementSetter} that will use the Object [] as the values to be
   * set on the query to be executed for this reader.  The int[] will provide the types ({@link
   * java.sql.Types}) for each of the values provided.
   *
   * @param args
   *     values to set on the query
   * @param types
   *     the type for each value in the args array
   *
   * @return this instance for method chaining
   */
  public BetterJdbcCursorItemReaderBuilder<T> queryArguments(Object[] args, int[] types) {
    this.preparedStatementSetter = new ArgumentTypePreparedStatementSetter(args, types);

    return this;
  }

  /**
   * Configures a {@link PreparedStatementSetter} that will use the List as the values to be set on
   * the query to be executed for this reader.
   *
   * @param args
   *     values to set on the query
   *
   * @return this instance for method chaining
   */
  public BetterJdbcCursorItemReaderBuilder<T> queryArguments(List<?> args) {
    Assert.notNull(args, "The list of arguments must not be null");
    this.preparedStatementSetter = new ArgumentPreparedStatementSetter(args.toArray());

    return this;
  }

  /**
   * The query to be executed for this reader
   *
   * @param sql
   *     query
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setSql(String)
   */
  public BetterJdbcCursorItemReaderBuilder<T> sql(String sql) {
    this.sql = sql;

    return this;
  }

  /**
   * The {@link RowMapper} used to map the results of the cursor to each item.
   *
   * @param rowMapper
   *     {@link RowMapper}
   *
   * @return this instance for method chaining
   *
   * @see BetterJdbcCursorItemReader#setRowMapper(RowMapper)
   */
  public BetterJdbcCursorItemReaderBuilder<T> rowMapper(RowMapper<T> rowMapper) {
    this.rowMapper = rowMapper;

    return this;
  }

  /**
   * Creates a {@link BeanPropertyRowMapper} to be used as your {@link RowMapper}.
   *
   * @param mappedClass
   *     the class for the row mapper
   *
   * @return this instance for method chaining
   *
   * @see BeanPropertyRowMapper
   */
  public BetterJdbcCursorItemReaderBuilder<T> beanRowMapper(Class<T> mappedClass) {
    this.rowMapper = new BeanPropertyRowMapper<>(mappedClass);

    return this;
  }

  /**
   * Validates configuration and builds a new reader instance.
   *
   * @return a fully constructed {@link JdbcCursorItemReader}
   */
  public BetterJdbcCursorItemReader<T> build() {
    if (this.saveState)
      Assert.hasText(this.name, "A name is required when saveSate is set to true");

    Assert.hasText(this.sql, "A query is required");
    Assert.notNull(this.dataSource, "A datasource is required");
    Assert.notNull(this.rowMapper, "A rowmapper is required");

    BetterJdbcCursorItemReader<T> reader = new BetterJdbcCursorItemReader<>();

    if (StringUtils.hasText(this.name))
      reader.setName(this.name);

    reader.setSaveState(this.saveState);
    reader.setPreparedStatementSetter(this.preparedStatementSetter);
    reader.setRowMapper(this.rowMapper);
    reader.setSql(this.sql);
    reader.setCurrentItemCount(this.currentItemCount);
    reader.setDataSource(this.dataSource);
    reader.setDriverSupportsAbsolute(this.driverSupportsAbsolute);
    reader.setFetchSize(this.fetchSize);
    reader.setIgnoreWarnings(this.ignoreWarnings);
    reader.setMaxItemCount(this.maxItemCount);
    reader.setMaxRows(this.maxRows);
    reader.setQueryTimeout(this.queryTimeout);
    reader.setUseSharedExtendedConnection(this.useSharedExtendedConnection);
    reader.setVerifyCursorPosition(this.verifyCursorPosition);

    return reader;
  }
}
