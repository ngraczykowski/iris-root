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

import java.util.List;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Slf4j
class SelectUnsolvedMatchesQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT *\n"
          + "FROM ae_select_unsolved_matches(?, ?)\n"
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
            ps.setInt(3, limit);
          });

      total += count;

      if (count < limit) {
        break;
      }

    } while (true);

    return total;
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
