package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcMatchSolutionDataAccess.class,
    SelectMatchSolutionQuery.class
})
@Sql
class JdbcMatchSolutionDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcMatchSolutionDataAccess matchSolutionDataAccess;

  @Test
  void shouldSelectMatchSolution() {
    var matchSolution = matchSolutionDataAccess.getMatchSolution(1L);
    assertThat(matchSolution.getMatch()).isEqualTo("alerts/1/matches/1");
    assertThat(matchSolution.getName()).isEqualTo("analysis/1/match-solutions/1");
  }
}
