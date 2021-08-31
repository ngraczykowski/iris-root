package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;
import com.silenteight.adjudication.engine.common.jdbc.ChunkHandler;
import com.silenteight.adjudication.engine.common.jdbc.JdbcCursorQueryTemplate;
import com.silenteight.adjudication.engine.common.jdbc.PostgresAdvisoryLock;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import static com.google.common.base.Strings.nullToEmpty;

@Slf4j
class SelectMissingMatchFeatureQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT alert_id,\n"
          + "       match_id,\n"
          + "       agent_config_feature_id,\n"
          + "       agent_config,\n"
          + "       feature,\n"
          + "       priority\n"
          + "FROM ae_missing_match_feature_query\n"
          + "WHERE analysis_id = ?\n"
          + "LIMIT ?";

  private static final MissingMatchFeatureMapper ROW_MAPPER = new MissingMatchFeatureMapper();

  private final JdbcCursorQueryTemplate<MissingMatchFeature> queryTemplate;
  private final JdbcTemplate lockJdbcTemplate;
  private final int limit;

  @SuppressWarnings("FeatureEnvy")
  SelectMissingMatchFeatureQuery(@NonNull DataSource dataSource, int chunkSize, int limit) {
    this.limit = limit;

    queryTemplate = JdbcCursorQueryTemplate
        .<MissingMatchFeature>builder()
        .dataSource(dataSource)
        .chunkSize(chunkSize)
        .maxRows(limit)
        .sql(SQL)
        .build();

    lockJdbcTemplate = new JdbcTemplate(dataSource, true);
  }

  int execute(long analysisId, MissingMatchFeatureReader.ChunkHandler chunkHandler) {
    if (log.isDebugEnabled()) {
      log.debug("Finding missing match feature values: analysisId={}, limit={}",
          analysisId, limit);
    }

    var total = 0;

    do {
      int count = executeQueryInLock(analysisId, chunkHandler);

      total += count;

      if (count < limit) {
        break;
      }
    } while (true);

    return total;
  }

  private int executeQueryInLock(
      long analysisId, MissingMatchFeatureReader.ChunkHandler chunkHandler) {

    Integer result = lockJdbcTemplate.execute(
        (Connection con) -> doExecuteQueryInLock(analysisId, chunkHandler, con));

    return result == null ? 0 : result;
  }

  private int doExecuteQueryInLock(
      long analysisId, MissingMatchFeatureReader.ChunkHandler chunkHandler, Connection con)
      throws SQLException {

    var key = SQL.hashCode() * analysisId;
    var lock = new PostgresAdvisoryLock(con, key);

    var maybeAcquiredLock = lock.acquire();
    if (maybeAcquiredLock.isEmpty()) {
      log.warn("Failed to acquire lock for analysis: analysisId={}", analysisId);
      return 0;
    }

    try (var acquiredLock = maybeAcquiredLock.get()) {
      return queryTemplate.execute(ROW_MAPPER, new InternalChunkHandler(chunkHandler), ps -> {
        ps.setLong(1, analysisId);
        ps.setInt(2, limit);
      });
    }
  }

  private static final class MissingMatchFeatureMapper implements RowMapper<MissingMatchFeature> {

    @SuppressWarnings("FeatureEnvy")
    @Override
    public MissingMatchFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
      var row = MissingMatchFeature.builder()
          .alertId(rs.getLong(1))
          .matchId(rs.getLong(2))
          .agentConfigFeatureId(rs.getLong(3))
          .agentConfig(nullToEmpty(rs.getString(4)))
          .feature(nullToEmpty(rs.getString(5)))
          .priority(rs.getInt(6))
          .build();

      if (!row.isValid()) {
        log.warn("Invalid row read: row={}", row);
        return null;
      }

      return row;
    }
  }

  @RequiredArgsConstructor
  private static final class InternalChunkHandler implements ChunkHandler<MissingMatchFeature> {

    private final MissingMatchFeatureReader.ChunkHandler delegate;

    @Override
    public void handle(List<? extends MissingMatchFeature> chunk) {
      delegate.handle(new MissingMatchFeatureChunk(chunk));
    }

    @Override
    public void finished() {
      delegate.finished();
    }
  }
}
