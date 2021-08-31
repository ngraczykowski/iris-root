package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdder;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlert;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;
import com.silenteight.adjudication.engine.common.jdbc.ChunkHandler;
import com.silenteight.adjudication.engine.common.jdbc.JdbcCursorQueryTemplate;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

class InsertAnalysisAlertsFromDatasetQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO ae_analysis_alert\n"
          + "    (SELECT ?, ada.alert_id, NULL, now()\n"
          + "     FROM ae_dataset_alert ada\n"
          + "              LEFT JOIN ae_analysis_alert aaa ON ada.alert_id = aaa.alert_id"
          + " AND aaa.analysis_id = ?\n"
          + "     WHERE ada.dataset_id = ?\n"
          + "       AND aaa.alert_id IS NULL\n"
          + "     LIMIT ?)\n"
          + "ON CONFLICT DO NOTHING\n"
          + "RETURNING analysis_id, alert_id";

  private static final AnalysisAlertMapper ROW_MAPPER = new AnalysisAlertMapper();

  private final JdbcCursorQueryTemplate<AnalysisAlert> queryTemplate;
  private final int limit;

  @SuppressWarnings("FeatureEnvy")
  InsertAnalysisAlertsFromDatasetQuery(@NonNull DataSource dataSource, int chunkSize, int limit) {
    this.limit = limit;

    queryTemplate = JdbcCursorQueryTemplate
        .<AnalysisAlert>builder()
        .dataSource(dataSource)
        .chunkSize(chunkSize)
        .maxRows(limit)
        .sql(SQL)
        .build();
  }

  @Transactional
  int execute(long fromDatasetId, long toAnalysisId, DatasetAlertsAdder.ChunkHandler chunkHandler) {
    return queryTemplate.execute(ROW_MAPPER, new InternalChunkHandler(chunkHandler), ps -> {
      ps.setLong(1, toAnalysisId);
      ps.setLong(2, toAnalysisId);
      ps.setLong(3, fromDatasetId);
      ps.setInt(4, limit);
    });
  }

  private static final class AnalysisAlertMapper implements RowMapper<AnalysisAlert> {

    @Override
    public AnalysisAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new AnalysisAlert(rs.getLong(1), rs.getLong(2));
    }
  }

  @RequiredArgsConstructor
  private static final class InternalChunkHandler implements ChunkHandler<AnalysisAlert> {

    private final DatasetAlertsAdder.ChunkHandler delegate;

    @Override
    public void handle(List<? extends AnalysisAlert> chunk) {
      if (!chunk.isEmpty())
        delegate.handle(new AnalysisAlertChunk(chunk));
    }
  }
}
