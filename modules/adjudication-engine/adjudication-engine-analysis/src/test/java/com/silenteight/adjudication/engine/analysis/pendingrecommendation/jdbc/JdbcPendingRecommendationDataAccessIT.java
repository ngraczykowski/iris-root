package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcPendingRecommendationDataAccess.class,
    CreatePendingRecommendationsQuery.class,
    RemoveSolvedPendingRecommendationsQuery.class,
    RemovePendingRecommendationByAnalysisIdsQuery.class
})
class JdbcPendingRecommendationDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcPendingRecommendationDataAccess jdbcPendingRecommendationDataAccess;

  @Test
  void shouldDeleteAllPendingRecommendations() {
    var rowsAffected = jdbcPendingRecommendationDataAccess.removeSolvedPendingRecommendations();
    assertThat(rowsAffected).isEqualTo(2);
  }
}
