package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.dto.MatchSolutionResponse;
import com.silenteight.adjudication.engine.solving.application.publisher.GovernanceAlertPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;

@Slf4j
@RequiredArgsConstructor
public class GovernanceMatchResponseProcess {

  private final GovernanceAlertPublisher governanceAlertPublisher;
  private final AlertSolvingRepository alertSolvingRepository;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void processAlert(final MatchSolutionResponse matchSolutionResponse) {

    log.debug("Processing match solution from governance = {}", matchSolutionResponse);

    long alertId = matchSolutionResponse.getAlertId();
    long matchId = matchSolutionResponse.getMatchId();
    var solution = matchSolutionResponse.getSolution();
    var reason = matchSolutionResponse.getReason();

    final var alertSolvingModel =
        alertSolvingRepository.updateMatchSolution(alertId, matchId, solution, reason);

    if (alertSolvingModel.isEmpty() || !alertSolvingModel.isAlertReadyForSolving()) {
      log.debug("Alert {} is not ready for solving", alertSolvingModel.getAlertId());
      return;
    }

    AlertSolutionRequest alertSolutionRequest =
        createAlertSolutionRequest(alertId, alertSolvingModel);

    log.debug("Sending alert solution request for alert = {}", alertSolutionRequest.getAlertId());
    // TODO it may happen that two requests could be send to governance
    this.governanceAlertPublisher.send(alertSolutionRequest);
  }

  private static AlertSolutionRequest createAlertSolutionRequest(
      long alertId, AlertSolving alertSolvingModel) {
    return new AlertSolutionRequest(
        alertSolvingModel.getAlertName(),
        alertSolvingModel.getMatchesSolution(), alertId);
  }
}
