package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import java.util.List;

import static com.silenteight.serp.governance.strategy.solve.SolvingStrategyType.valueOf;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substringAfter;

public class SolveAlertUseCase {

  private static final String STRATEGY_RESOURCE_PREFIX = "strategies/";

  public BatchSolveAlertsResponse solve(BatchSolveAlertsRequest request) {
    SolvingStrategy strategy = retrieveSolvingStrategy(request.getStrategy());
    List<SolveAlertSolutionResponse> solutionResponses = request
        .getAlertsList()
        .stream()
        .map(alert -> solveSingle(strategy, alert))
        .collect(toList());

    return BatchSolveAlertsResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private static SolvingStrategy retrieveSolvingStrategy(String strategy) {
    return valueOf(substringAfter(strategy, STRATEGY_RESOURCE_PREFIX))
        .getStrategy();
  }

  private SolveAlertSolutionResponse solveSingle(SolvingStrategy strategy, Alert alert) {
    SolveRequest solveRequest = new SolveRequestFactory(alert).create();
    RecommendedAction recommendedAction = strategy.solve(solveRequest).getRecommendedAction();

    return SolveAlertSolutionResponse.newBuilder()
        .setAlertName(alert.getName())
        .setAlertSolution(recommendedAction.toString())
        .build();
  }
}
