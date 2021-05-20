package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

// TODO(ahaczewski): Test JdbcMissingMatchFeatureReader class (integration test with DB).
@Slf4j
class JdbcMissingMatchFeatureReader implements MissingMatchFeatureReader {

  private final JdbcTemplate jdbcTemplate;
  private final SelectMissingMatchFeatureQuery query;

  JdbcMissingMatchFeatureReader(DataSource dataSource, int chunkSize, int maxRows) {
    Preconditions.checkArgument(
        chunkSize <= maxRows, "Chunk size %s must be less than or equal to max rows %s", chunkSize,
        maxRows);

    jdbcTemplate = new JdbcTemplate(dataSource, true);
    jdbcTemplate.setFetchSize(chunkSize);

    query = new SelectMissingMatchFeatureQuery(chunkSize, maxRows);
  }

  @Transactional(propagation = Propagation.NEVER)
  @Override
  public int read(long analysisId, ChunkHandler chunkHandler) {
    var result = jdbcTemplate.execute(query.getCallback(analysisId, chunkHandler));
    return result != null ? result : -1;
  }
}
