package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    AnalysisJdbcDataAccess.class,
    FindTodayAnalysisQuery.class,
    InsertAnalysisQuery.class
})
class AnalysisJdbcDataAccessIT extends BaseJdbcTest {

  @Autowired
  private AnalysisJdbcDataAccess analysisJdbcDataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldSelectTodayAnalysis() {
    var analysis = analysisJdbcDataAccess.findCurrentAnalysis();
    assertTrue(analysis.isPresent());
    assertThat(analysis.get()).isEqualTo(2);
  }

  @Test
  void shouldInsertAnalysis() {
    analysisJdbcDataAccess.save(4);
    var count = jdbcTemplate.queryForObject("SELECT count(*) FROM pb_analysis", Integer.class);
    assertThat(count).isEqualTo(4);

    var analysis = analysisJdbcDataAccess.findCurrentAnalysis();
    assertTrue(analysis.isPresent());
    assertThat(analysis.get()).isEqualTo(4);
  }
}
