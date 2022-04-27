package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.GovernanceMatchResponseProcess;
import com.silenteight.adjudication.engine.solving.application.process.dto.MatchSolutionResponse;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.SolutionResponse;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class ReadyMatchFeatureVectorPublisher {

  private final IQueue<MatchSolutionRequest> sendQueue;
  private final GovernanceFacade governanceFacade;
  private final GovernanceMatchResponseProcess governanceMatchResponseProcess;
  private final ProtoMessageToObjectNodeConverter converter;

  public ReadyMatchFeatureVectorPublisher(
      final GovernanceFacade governanceFacade,
      final HazelcastInstance hazelcastInstance,
      final ExecutorService executorService,
      final GovernanceMatchResponseProcess governanceMatchResponseProcess,
      ProtoMessageToObjectNodeConverter converter) {
    this.governanceFacade = governanceFacade;
    this.governanceMatchResponseProcess = governanceMatchResponseProcess;
    this.sendQueue = hazelcastInstance.getQueue("ae.governance.match.to.send");
    this.converter = converter;
    for (int i = 0; i < 15; i++) {
      executorService.submit(this::consume);
    }
  }

  public void send(final MatchSolutionRequest matchSolutionRequest) {
    // Send for solving alert solution to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
    log.info("Sending match for solving to Governance");
    this.sendQueue.add(matchSolutionRequest);
  }

  void consume() {
    do {
      final MatchSolutionRequest poll = this.sendQueue.poll();
      if (poll != null) {
        this.sendRequestToGovernance(poll);
      }
    } while (true);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  private void sendRequestToGovernance(
      MatchSolutionRequest matchSolutionRequest) {

    long alertId = matchSolutionRequest.getAlertId();
    long matchId = matchSolutionRequest.getMatchId();
    var batchSolveFeaturesRequest = matchSolutionRequest.mapToBatchSolveFeaturesRequest();

    log.debug(
        "Sending request to governance for solving match for alert: {}, match: {}", alertId,
        matchId);

    final BatchSolveFeaturesResponse batchSolveFeaturesResponse =
        governanceFacade.batchSolveFeatures(batchSolveFeaturesRequest);

    log.debug(
        "Received match solution response from governance for alert: {}, Match: {}", alertId,
        matchId);

    sendForMatchSolutionProcess(alertId, matchId, batchSolveFeaturesResponse);
  }

  private void sendForMatchSolutionProcess(
      long alertId, long matchId, BatchSolveFeaturesResponse batchSolveFeaturesResponse) {

    List<SolutionResponse> solutionResponses = batchSolveFeaturesResponse.getSolutionsList();
    if (!solutionResponses.isEmpty()) {
      //TODO: We are only requesting for a solution for one alert at a time so we should receive
      //ony one solution. Verify it.
      SolutionResponse solutionResponse = solutionResponses.get(0);
      FeatureVectorSolution featureVectorSolution = solutionResponse.getFeatureVectorSolution();
      String solution = featureVectorSolution.toString();

      var reason = converter.convertToJsonString(solutionResponse
          .getReason()).orElse("");

      MatchSolutionResponse matchSolutionResponse =
          new MatchSolutionResponse(alertId, matchId, solution, reason);

      governanceMatchResponseProcess.processAlert(matchSolutionResponse);
    }

    // TODO: send to gov if all matches are solved for an alert. Dunno if it would be better to use
    //  an internal queue for now or just call a process (GovernanceMatchResponseProcess)
  }
}
