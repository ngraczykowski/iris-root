package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvedAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.GovernanceAlertPort;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class GovernanceAlertPublisher implements GovernanceAlertPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final GovernanceFacade governanceFacade;
  private final SolvedAlertPort solvedAlertProcess;

  public void send(final AlertSolutionRequest alertSolutionRequest) {
    log.info("Sending alert for solving to Governance");
    inMemorySolvingExecutor.execute(() -> sendRequestToGovernance(alertSolutionRequest));
  }

  private void sendRequestToGovernance(AlertSolutionRequest alertSolutionRequest) {
    var batchSolveAlertsRequest = alertSolutionRequest.mapToBatchSolveAlertsRequest();
    var batchSolveAlertsResponse = governanceFacade.batchSolveAlerts(batchSolveAlertsRequest);
    log.debug(
        "Received {} alert solution from governance", batchSolveAlertsResponse.getSolutionsCount());
    solvedAlertProcess.generateRecommendation(
        alertSolutionRequest.getAlertId(), batchSolveAlertsResponse);
  }
}
