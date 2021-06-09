package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;
import com.silenteight.adjudication.engine.common.jdbc.ChunkHandler;
import com.silenteight.adjudication.engine.common.jdbc.JdbcCursorQueryTemplate;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;

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
          + "       agent_config,\n"
          + "       feature,\n"
          + "       priority\n"
          + "FROM ae_missing_match_feature_query\n"
          + "WHERE analysis_id = ?\n"
          + "LIMIT ?";

  private static final MissingMatchFeatureMapper ROW_MAPPER = new MissingMatchFeatureMapper();

  private final JdbcCursorQueryTemplate<MissingMatchFeature> queryTemplate;
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
  }

  int execute(long analysisId, MissingMatchFeatureReader.ChunkHandler chunkHandler) {
    if (log.isDebugEnabled()) {
      log.debug("Selecting missing match feature values: analysisId={}, limit={}",
          analysisId, limit);
    }

    return queryTemplate.execute(ROW_MAPPER, new InternalChunkHandler(chunkHandler), ps -> {
      ps.setLong(1, analysisId);
      ps.setInt(2, limit);
    });
  }

  private static final class MissingMatchFeatureMapper implements RowMapper<MissingMatchFeature> {

    @SuppressWarnings("FeatureEnvy")
    @Override
    public MissingMatchFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
      var row = MissingMatchFeature.builder()
          .alertId(rs.getLong(1))
          .matchId(rs.getLong(2))
          .agentConfig(nullToEmpty(rs.getString(3)))
          .feature(nullToEmpty(rs.getString(4)))
          .priority(rs.getInt(5))
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
