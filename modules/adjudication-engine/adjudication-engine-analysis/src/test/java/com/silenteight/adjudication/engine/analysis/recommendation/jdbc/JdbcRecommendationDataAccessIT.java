package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

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
    JdbcRecommendationDataAccess.class,
    JdbcRecommendationConfiguration.class
})
class JdbcRecommendationDataAccessIT extends BaseJdbcTest {

  @Autowired
  JdbcRecommendationDataAccess recommendationDataAccess;

  @Test
  void shouldSelectAllReadyRecommendations() {
    var result = recommendationDataAccess.selectPendingAlerts(1);
    assertThat(result.size()).isEqualTo(1);
  }
}
