package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import com.silenteight.adjudication.engine.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.adjudication.engine.analysis.matchsolution.FeaturesFixtures.makeSaveMatchSolutionRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcMatchSolutionDataAccess.class,
    SelectMatchSolutionQuery.class,
    InsertMatchSolutionQuery.class
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

  @Test
  void shouldInsertMatchSolution() {
    var request = makeSaveMatchSolutionRequest();
    matchSolutionDataAccess.save(List.of(request));
    assertThat(matchSolutionDataAccess.getMatchSolution(1).getMatch()).isEqualTo(
        "alerts/1/matches/1");
  }

  @Test
  void shouldNotInsertSecondMatchSolution() {
    var request = makeSaveMatchSolutionRequest();
    matchSolutionDataAccess.save(List.of(request, request));
    assertThat(matchSolutionDataAccess.getMatchSolution(1).getMatch()).isEqualTo(
        "alerts/1/matches/1");
  }
}
