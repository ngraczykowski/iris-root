package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.ResolvedAlertProcess;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.ExecutorService;

@Slf4j
public class GovernanceAlertPublisher {

  private final IQueue<AlertSolutionRequest> sendQueue;
  private final GovernanceFacade governanceFacade;

  private final ResolvedAlertProcess resolvedAlertProcess;

  public GovernanceAlertPublisher(
      final GovernanceFacade governanceFacade,
      final HazelcastInstance hazelcastInstance,
      final ExecutorService executorService,
      final ResolvedAlertProcess resolvedAlertProcess
  ) {
    this.governanceFacade = governanceFacade;
    this.resolvedAlertProcess = resolvedAlertProcess;
    this.sendQueue = hazelcastInstance.getQueue("ae.governance.alert.to.send");
    for (int i = 0; i < 15; i++) {
      executorService.submit(this::consume);
    }
  }

  public void send(final AlertSolutionRequest alertSolutionRequest) {
    log.info("Sending alert for solving to Governance");
    this.sendQueue.add(alertSolutionRequest);
  }

  void consume() {
    do {
      final AlertSolutionRequest poll = this.sendQueue.poll();
      if (poll != null) {
        this.sendRequestToGovernance(poll);
      }
    } while (true);
  }

  private void sendRequestToGovernance(
      AlertSolutionRequest alertSolutionRequest) {
    var batchSolveAlertsRequest = alertSolutionRequest.mapToBatchSolveAlertsRequest();
    var batchSolveAlertsResponse =
        governanceFacade.batchSolveAlerts(batchSolveAlertsRequest);
    log.debug(
        "Received {} alert solution from governance", batchSolveAlertsResponse.getSolutionsCount());
    resolvedAlertProcess.generateRecommendation(
        alertSolutionRequest.getAlertId(), batchSolveAlertsResponse);
  }
}
