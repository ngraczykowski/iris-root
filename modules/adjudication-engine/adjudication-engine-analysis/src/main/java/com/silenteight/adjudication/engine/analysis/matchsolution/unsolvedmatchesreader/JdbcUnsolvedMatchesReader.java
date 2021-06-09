package com.silenteight.adjudication.engine.analysis.matchsolution.unsolvedmatchesreader;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader;

import com.google.common.base.Preconditions;

import javax.sql.DataSource;

public class JdbcUnsolvedMatchesReader implements UnsolvedMatchesReader {

  private final SelectUnsolvedMatchesQuery query;

  JdbcUnsolvedMatchesReader(DataSource dataSource, int chunkSize, int limit) {
    Preconditions.checkArgument(
        chunkSize <= limit, "Chunk size %s must be less than or equal to limit %s", chunkSize,
        limit);

    query = new SelectUnsolvedMatchesQuery(dataSource, chunkSize, limit);
  }

  @Override
  public int readInChunks(long analysisId, ChunkHandler chunkHandler) {
    return query.execute(analysisId, chunkHandler);
  }
}
