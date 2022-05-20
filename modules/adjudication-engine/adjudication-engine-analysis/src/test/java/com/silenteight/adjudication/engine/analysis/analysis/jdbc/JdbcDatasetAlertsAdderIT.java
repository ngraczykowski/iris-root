package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdder;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;
import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcDatasetAlertsAdderConfiguration.class,
})
@Sql
class JdbcDatasetAlertsAdderIT extends BaseJdbcTest {

  @Autowired
  JdbcDatasetAlertsAdder reader;

  @Mock
  DatasetAlertsAdder.ChunkHandler chunkHandler;

  @Captor
  ArgumentCaptor<AnalysisAlertChunk> analysisAlertChunkArgumentCaptor;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  void shouldSelectInChunks() {
    var readMatches = reader.addAlertsFromDataset(1, 1, chunkHandler);
    assertThat(readMatches).isEqualTo(2);

    verify(chunkHandler).handle(analysisAlertChunkArgumentCaptor.capture());

    assertThat(analysisAlertChunkArgumentCaptor.getAllValues()).hasSize(1);

    var analysisAlertChunk = analysisAlertChunkArgumentCaptor.getValue();

    var event = analysisAlertChunk.toAnalysisAlertsAdded();

    assertThat(event).hasValueSatisfying(r ->
        assertThat(r.getAnalysisAlertsList())
            .containsExactly("analysis/1/alerts/2", "analysis/1/alerts/3"));

    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_analysis_alert",
        Integer.class)).isEqualTo(3);
  }
}
