package com.silenteight.adjudication.engine.common.jdbc;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class JdbcCursorQueryTemplate<T> {

  private final JdbcTemplate jdbcTemplate;
  private final CursorHoldingPreparedStatementCreator statementCreator;
  private final ChunkedResultSetExtractorFactory<T> extractorFactory;

  @Builder
  private JdbcCursorQueryTemplate(
      @NonNull DataSource dataSource, @NonNull String sql, int chunkSize, int maxRows) {

    Preconditions.checkArgument(
        chunkSize <= maxRows, "Chunk size %s must be less than or equal to max rows %s", chunkSize,
        maxRows);

    jdbcTemplate = new JdbcTemplate(dataSource, true);
    jdbcTemplate.setFetchSize(chunkSize);
    jdbcTemplate.setMaxRows(maxRows);

    statementCreator = new CursorHoldingPreparedStatementCreator(sql);
    extractorFactory = new ChunkedResultSetExtractorFactory<>(chunkSize);
  }

  public String getSql() {
    return statementCreator.getSql();
  }

  public int getChunkSize() {
    return jdbcTemplate.getFetchSize();
  }

  public int getMaxRows() {
    return jdbcTemplate.getMaxRows();
  }

  public int execute(
      @NonNull RowMapper<? extends T> rowMapper, @NonNull ChunkHandler<? super T> chunkHandler,
      @NonNull PreparedStatementSetter pss) {

    var result =
        jdbcTemplate.query(statementCreator, pss, extractorFactory.create(rowMapper, chunkHandler));
    return result != null ? result : 0;
  }
}
