package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.alert;
import static com.silenteight.serp.governance.strategy.solve.AlertTestUtils.obsoleteMatch;
import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.ACTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.strategy.solve.SolvingStrategyType.ALL_MATCHES;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class SolveAlertUseCaseTest {

  private static final String STRATEGY_RESOURCE_PREFIX = "strategies/";
  private static final String STRATEGY = STRATEGY_RESOURCE_PREFIX + ALL_MATCHES.toString();
  private static final String ALERT_NAME_1 = "alerts/123";
  private static final String ALERT_NAME_2 = "alerts/456";

  private SolveAlertUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new SolveAlertUseCase();
  }

  @Test
  void throwsIllegalArgumentExceptionWhenUnknownStrategyIsUsed() {
    // given
    BatchSolveAlertsRequest request = solveAlertsRequest(
        STRATEGY_RESOURCE_PREFIX + "UNKNOWN_STRATEGY", emptyList());

    // when, then
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> underTest.solve(request));
  }

  @Test
  void getSolutionsForAlerts() {
    // given
    BatchSolveAlertsRequest request = solveAlertsRequest(
        STRATEGY,
        asList(
            alert(
                ALERT_NAME_1,
                obsoleteMatch(SOLUTION_FALSE_POSITIVE),
                obsoleteMatch(SOLUTION_POTENTIAL_TRUE_POSITIVE)),
            alert(
                ALERT_NAME_2,
                obsoleteMatch(SOLUTION_NO_DECISION))));

    // when
    BatchSolveAlertsResponse response = underTest.solve(request);

    // then
    SolveAlertsResponseAssert.assertThat(response)
        .hasSolutionsCount(2)
        .solution(0)
        .hasAlertSolution(ACTION_FALSE_POSITIVE.toString())
        .hasAlertName(ALERT_NAME_1)
        .and()
        .solution(1)
        .hasAlertSolution(ACTION_FALSE_POSITIVE.toString())
        .hasAlertName(ALERT_NAME_2);
  }

  private static BatchSolveAlertsRequest solveAlertsRequest(String strategy, List<Alert> alerts) {
    return BatchSolveAlertsRequest.newBuilder()
        .setStrategy(strategy)
        .addAllAlerts(alerts)
        .build();
  }
}
