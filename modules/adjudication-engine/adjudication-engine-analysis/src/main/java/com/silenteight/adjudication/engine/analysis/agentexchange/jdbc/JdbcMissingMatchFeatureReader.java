package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader;

import com.google.common.base.Preconditions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

// TODO(ahaczewski): Test JdbcMissingMatchFeatureReader class (integration test with DB).
@Slf4j
class JdbcMissingMatchFeatureReader implements MissingMatchFeatureReader {

  private final SelectMissingMatchFeatureQuery query;

  JdbcMissingMatchFeatureReader(DataSource dataSource, int chunkSize, int limit) {
    Preconditions.checkArgument(
        chunkSize <= limit, "Chunk size %s must be less than or equal to limit %s", chunkSize,
        limit);

    query = new SelectMissingMatchFeatureQuery(dataSource, chunkSize, limit);
  }

  @Transactional(propagation = Propagation.NEVER)
  @Override
  public int read(long analysisId, ChunkHandler chunkHandler) {
    return query.execute(analysisId, chunkHandler);
  }
}
