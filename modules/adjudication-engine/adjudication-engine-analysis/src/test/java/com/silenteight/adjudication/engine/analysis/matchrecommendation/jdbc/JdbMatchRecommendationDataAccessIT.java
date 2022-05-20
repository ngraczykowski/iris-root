package com.silenteight.adjudication.engine.analysis.matchrecommendation.jdbc;

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
    JdbcMatchRecommendationConfiguration.class,
    JdbMatchRecommendationDataAccess.class
})
class JdbMatchRecommendationDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbMatchRecommendationDataAccess dataAccess;

  @Test
  void shouldSelectOnePendingMatch() {
    var matches = dataAccess.selectPendingMatches(2);
    assertThat(matches.size()).isEqualTo(1);

    var match = matches.get(0);
    assertThat(match.getMatchId()).isEqualTo(321);
  }
}
