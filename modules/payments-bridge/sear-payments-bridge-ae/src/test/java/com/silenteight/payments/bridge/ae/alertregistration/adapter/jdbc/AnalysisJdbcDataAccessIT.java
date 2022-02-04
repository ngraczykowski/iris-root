package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql
@Import({
    AnalysisJdbcDataAccess.class,
    FindTodayAnalysisQuery.class,
    InsertAnalysisQuery.class,
    ExistsAnalysisQuery.class
})
@EnableConfigurationProperties(CurrentAnalysisQueryProperties.class)
class AnalysisJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private AnalysisJdbcDataAccess analysisJdbcDataAccess;

  @Test
  void shouldSelectTodayAnalysis() {
    var analysis = analysisJdbcDataAccess.findCurrentAnalysis();
    assertTrue(analysis.isPresent());
    assertThat(analysis.get()).isEqualTo("analysis/2");
  }

  @Test
  void shouldInsertAnalysis() {
    analysisJdbcDataAccess.save("analysis/4");
    var count = jdbcTemplate.queryForObject("SELECT count(*) FROM pb_analysis", Integer.class);
    assertThat(count).isEqualTo(4);

    var analysis = analysisJdbcDataAccess.findCurrentAnalysis();
    assertTrue(analysis.isPresent());
    assertThat(analysis.get()).isEqualTo("analysis/4");
  }
}
