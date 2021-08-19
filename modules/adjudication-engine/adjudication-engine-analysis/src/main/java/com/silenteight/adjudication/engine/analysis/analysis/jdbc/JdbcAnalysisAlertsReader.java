package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsReader;

import com.google.common.base.Preconditions;

import javax.sql.DataSource;

class JdbcAnalysisAlertsReader implements DatasetAlertsReader {

  private final InsertDatasetAlertsQuery query;

  JdbcAnalysisAlertsReader(DataSource dataSource, int chunkSize, int limit) {
    Preconditions.checkArgument(
        chunkSize <= limit, "Chunk size %s must be less than or equal to limit %s", chunkSize,
        limit);

    query = new InsertDatasetAlertsQuery(dataSource, chunkSize, limit);
  }

  @Override
  public int read(long analysisId, long datasetId, ChunkHandler chunkHandler) {
    return query.execute(analysisId, datasetId, chunkHandler);
  }
}
