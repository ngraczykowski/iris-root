package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.Alert;

import org.junit.jupiter.api.Test;

import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.alert;
import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.newMatch;
import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.obsoleteMatch;
import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.solvedMatch;
import static com.silenteight.serp.governance.strategy.solve.SolveRequestAssert.assertThat;
import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_SOLUTION_UNSPECIFIED;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;

class SolveRequestFactoryTest {

  private static final String ALERT_NAME = "alert-name";

  @Test
  void shouldMapSingleSolutionsToBuckets() {
    Alert alert = alert(
        ALERT_NAME,
        obsoleteMatch(SOLUTION_FALSE_POSITIVE),
        solvedMatch(SOLUTION_POTENTIAL_TRUE_POSITIVE),
        newMatch(SOLUTION_NO_DECISION));

    SolveRequest solveRequest = new SolveRequestFactory(alert).create();

    // TODO(mmastylo): for now Alert does not expose analyst solution
    assertThat(solveRequest)
        .hasPreviousAnalystSolution(ANALYST_SOLUTION_UNSPECIFIED)
        .hasSolvedMatchesFeatureVectorSolutions(SOLUTION_POTENTIAL_TRUE_POSITIVE)
        .hasUnsolvedMatchesFeatureVectorSolutions(SOLUTION_NO_DECISION)
        .hasObsoleteMatchesFeatureVectorSolutions(SOLUTION_FALSE_POSITIVE);
  }

  @Test
  void shouldMapMultipleSolutionsToBuckets() {
    Alert alert = alert(
        ALERT_NAME,
        obsoleteMatch(SOLUTION_NO_DECISION),
        obsoleteMatch(SOLUTION_FALSE_POSITIVE),
        solvedMatch(SOLUTION_NO_DECISION),
        solvedMatch(SOLUTION_FALSE_POSITIVE),
        newMatch(SOLUTION_NO_DECISION));

    SolveRequest solveRequest = new SolveRequestFactory(alert).create();

    // TODO(mmastylo): for now Alert does not expose analyst solution
    assertThat(solveRequest)
        .hasPreviousAnalystSolution(ANALYST_SOLUTION_UNSPECIFIED)
        .hasSolvedMatchesFeatureVectorSolutions(SOLUTION_NO_DECISION, SOLUTION_FALSE_POSITIVE)
        .hasUnsolvedMatchesFeatureVectorSolutions(SOLUTION_NO_DECISION)
        .hasObsoleteMatchesFeatureVectorSolutions(SOLUTION_NO_DECISION, SOLUTION_FALSE_POSITIVE);
  }
}
