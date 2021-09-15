package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatch;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;
import com.silenteight.adjudication.engine.common.jdbc.ChunkHandler;
import com.silenteight.adjudication.engine.common.jdbc.JdbcCursorQueryTemplate;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.annotation.Nullable;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Slf4j
class SelectUnsolvedMatchesQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT *\n"
          + "FROM select_unsolved_matches(?)\n"
          + "LIMIT ?";

  private static final UnsolvedMatchMapper ROW_MAPPER = new UnsolvedMatchMapper();

  private final JdbcCursorQueryTemplate<UnsolvedMatch> queryTemplate;
  private final int limit;

  @SuppressWarnings("FeatureEnvy")
  SelectUnsolvedMatchesQuery(@NonNull DataSource dataSource, int chunkSize, int limit) {
    this.limit = limit;

    queryTemplate = JdbcCursorQueryTemplate
        .<UnsolvedMatch>builder()
        .dataSource(dataSource)
        .chunkSize(chunkSize)
        .maxRows(limit)
        .sql(SQL)
        .build();
  }

  int execute(long analysisId, UnsolvedMatchesReader.ChunkHandler chunkHandler) {
    if (log.isDebugEnabled()) {
      log.debug("Finding unsolved matches with ready feature vectors: analysisId={}, limit={}",
          analysisId, limit);
    }

    var total = 0;

    do {
      var count =
          queryTemplate.execute(ROW_MAPPER, new UnsolvedMatchChunkHandler(chunkHandler), ps -> {
            ps.setLong(1, analysisId);
            ps.setInt(2, limit);
          });

      total += count;

      if (count < limit) {
        break;
      }

    } while (true);

    return total;
  }

  private static final class UnsolvedMatchMapper implements RowMapper<UnsolvedMatch> {

    @Nullable
    @Override
    public UnsolvedMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
      Long alertId = rs.getObject(1, Long.class);
      Long matchId = rs.getObject(2, Long.class);
      Array categoryValues = rs.getArray(3);
      Array featureValues = rs.getArray(4);

      if (alertId == null) {
        log.warn("Row with NULL alert_id: rowNum={}", rowNum);
        return null;
      }

      if (matchId == null) {
        log.warn("Row with NULL match_id: rowNum={}", rowNum);
        return null;
      }

      if (!isExpectedArrayBaseType(categoryValues, "category_values", rowNum)) {
        return null;
      }

      if (!isExpectedArrayBaseType(featureValues, "feature_values", rowNum)) {
        return null;
      }

      return new UnsolvedMatch(
          alertId, matchId, (String[]) categoryValues.getArray(),
          (String[]) featureValues.getArray());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isExpectedArrayBaseType(
        @Nullable Array arrayColumn, String columnName, int rowNum) throws SQLException {

      if (arrayColumn == null) {
        log.warn("Row with NULL {}: rowNum={}", columnName, rowNum);
        return false;
      }

      if (arrayColumn.getBaseType() != Types.VARCHAR) {
        log.warn("Column {} has invalid array base type: baseType={}, rowNum={}",
            columnName, arrayColumn.getBaseTypeName(), rowNum);
        return false;
      }

      return true;
    }
  }

  private static class UnsolvedMatchChunkHandler implements ChunkHandler<UnsolvedMatch> {

    private final UnsolvedMatchesReader.ChunkHandler chunkHandler;

    public UnsolvedMatchChunkHandler(UnsolvedMatchesReader.ChunkHandler chunkHandler) {
      this.chunkHandler = chunkHandler;
    }

    @Override
    public void handle(List<? extends UnsolvedMatch> chunk) {
      chunkHandler.handle(new UnsolvedMatchesChunk(chunk));
    }

    @Override
    public void finished() {
      chunkHandler.finished();
    }
  }
}
