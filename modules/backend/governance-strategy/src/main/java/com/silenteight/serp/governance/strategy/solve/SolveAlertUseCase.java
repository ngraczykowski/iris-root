package com.silenteight.serp.governance.strategy.solve;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import java.util.List;

import static com.silenteight.serp.governance.strategy.solve.SolvingStrategyType.valueOf;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@Slf4j
public class SolveAlertUseCase {

  private static final String STRATEGY_RESOURCE_PREFIX = "strategies/";

  public BatchSolveAlertsResponse solve(BatchSolveAlertsRequest request) {
    log.info("Solving {} alerts using strategy '{}'.",
             request.getAlertsCount(), request.getStrategy());
    SolvingStrategy strategy = retrieveSolvingStrategy(request.getStrategy());
    List<SolveAlertSolutionResponse> solutionResponses = request
        .getAlertsList()
        .stream()
        .map(alert -> solveSingle(strategy, alert))
        .collect(toList());

    log.info("Solved {} alerts using strategy '{}'.",
             (long) request.getAlertsList().size(), request.getStrategy());
    return BatchSolveAlertsResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private static SolvingStrategy retrieveSolvingStrategy(String strategy) {
    return valueOf(substringAfter(strategy, STRATEGY_RESOURCE_PREFIX))
        .getStrategy();
  }

  private SolveAlertSolutionResponse solveSingle(SolvingStrategy strategy, Alert alert) {
    log.info("Solving alert with {} matches using strategy '{}'.",
              alert.getMatchesCount(), strategy.getClass().getSimpleName());

    SolveRequest solveRequest = new SolveRequestFactory(alert).create();
    RecommendedAction recommendedAction = strategy.solve(solveRequest).getRecommendedAction();

    log.info("Solved alert with {} matches using strategy '{}' as '{}'.",
              alert.getMatchesCount(), strategy.getClass().getSimpleName(), recommendedAction);
    return SolveAlertSolutionResponse.newBuilder()
        .setAlertName(alert.getName())
        .setAlertSolution(recommendedAction.toString())
        .build();
  }
}
