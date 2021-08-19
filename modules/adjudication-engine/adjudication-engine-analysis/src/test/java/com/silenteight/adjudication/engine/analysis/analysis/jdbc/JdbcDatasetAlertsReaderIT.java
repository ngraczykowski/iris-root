package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsReader;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcAnalysisAlertsReaderConfiguration.class,
})
@Sql
class JdbcDatasetAlertsReaderIT extends BaseJdbcTest {

  @Autowired
  JdbcAnalysisAlertsReader reader;

  @Mock
  DatasetAlertsReader.ChunkHandler chunkHandler;

  @Captor
  ArgumentCaptor<AnalysisAlertChunk> analysisAlertChunkArgumentCaptor;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  void shouldSelectInChunks() {
    var readMatches = reader.read(1, 1, chunkHandler);
    assertThat(readMatches).isEqualTo(2);

    verify(chunkHandler).handle(analysisAlertChunkArgumentCaptor.capture());

    assertThat(analysisAlertChunkArgumentCaptor.getAllValues()).hasSize(1);

    var analysisAlertChunk = analysisAlertChunkArgumentCaptor.getValue();

    var alerts = new ArrayList<Long>();
    analysisAlertChunk.forEach(aac -> alerts.add(aac.getAlertId()));

    assertThat(1).isNotIn(alerts);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM ae_analysis_alert",
        Integer.class)).isEqualTo(3);
  }
}
