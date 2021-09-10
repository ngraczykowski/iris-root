package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUnsolvedMatchesReader implements UnsolvedMatchesReader {

  private final SelectUnsolvedMatchesQuery query;
  private final AnalyzeLargeTablesQuery analyzeLargeTablesQuery;

  JdbcUnsolvedMatchesReader(DataSource dataSource, int chunkSize, int limit) {
    Preconditions.checkArgument(
        chunkSize <= limit, "Chunk size %s must be less than or equal to limit %s", chunkSize,
        limit);

    query = new SelectUnsolvedMatchesQuery(dataSource, chunkSize, limit);
    analyzeLargeTablesQuery = new AnalyzeLargeTablesQuery(new JdbcTemplate(dataSource, true));
  }

  @Override
  public int readInChunks(long analysisId, ChunkHandler chunkHandler) {
    analyzeLargeTablesQuery.execute();
    return query.execute(analysisId, chunkHandler);
  }
}
