package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.google.common.base.Strings.nullToEmpty;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

@RequiredArgsConstructor
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

  private final int chunkSize;
  private final int maxRows;

  public ConnectionCallback<Integer> getCallback(long analysisId, ChunkHandler chunkHandler) {
    return con -> execute(con, analysisId, chunkHandler);
  }

  int execute(Connection con, long analysisId, ChunkHandler chunkHandler) throws SQLException {
    if (log.isDebugEnabled()) {
      log.debug("Selecting missing match feature values: analysisId={}, chunkSize={}, maxRows={}",
          analysisId, chunkSize, maxRows);
    }

    // NOTE(ahaczewski): Holding cursor over commits allows for chunk handler to commit
    //  transactions, without affecting the ResultSet iteration.
    var previousHoldability = con.getHoldability();
    con.setHoldability(HOLD_CURSORS_OVER_COMMIT);

    try (var statement = con.prepareStatement(SQL, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
      statement.setFetchSize(chunkSize);
      statement.setLong(1, analysisId);
      statement.setInt(2, maxRows);
      statement.setMaxRows(maxRows);

      try (var fetcher = createFetcher(statement, chunkHandler)) {
        return fetcher.readInChunks();
      }
    } finally {
      con.commit();
      con.setHoldability(previousHoldability);
    }
  }

  private MissingMatchFeatureChunkFetcher createFetcher(
      PreparedStatement statement, ChunkHandler chunkHandler)
      throws SQLException {

    var resultSet = statement.executeQuery();
    return new MissingMatchFeatureChunkFetcher(resultSet, chunkSize, ROW_MAPPER, chunkHandler);
  }

  private static final class MissingMatchFeatureMapper implements RowMapper<MissingMatchFeature> {

    @SuppressWarnings("FeatureEnvy")
    @Override
    public MissingMatchFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
      return MissingMatchFeature.builder()
          .alertId(rs.getLong(1))
          .matchId(rs.getLong(2))
          .agentConfig(nullToEmpty(rs.getString(3)))
          .feature(nullToEmpty(rs.getString(4)))
          .priority(rs.getInt(5))
          .build();
    }
  }
}
