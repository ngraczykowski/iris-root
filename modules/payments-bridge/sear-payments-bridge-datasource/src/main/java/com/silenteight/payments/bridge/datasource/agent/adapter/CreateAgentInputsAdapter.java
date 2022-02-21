package com.silenteight.payments.bridge.datasource.agent.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.*;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class CreateAgentInputsAdapter implements CreateAgentInputsClient {

  private final AgentInputServiceBlockingStub blockingStub;

  private final Duration timeout;

  public void createAgentInputs(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    if (!batchCreateAgentInputsRequest.getAgentInputsList().isEmpty()) {
      sendToDatasource(batchCreateAgentInputsRequest);
    } else {
      log.debug(
          "Batch category value request is empty. Data won't be send to datasource service");
    }
  }

  private void sendToDatasource(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    logRequest(batchCreateAgentInputsRequest);

    try {
      var response = blockingStub
          .withDeadline(deadline)
          .batchCreateAgentInputs(batchCreateAgentInputsRequest);

      logResponse(response);

    } catch (StatusRuntimeException status) {
      log.warn("Request with agent inputs to the datasource service failed", status);
    }
  }

  private static void logRequest(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    if (log.isDebugEnabled()) {
      var agentInputs = batchCreateAgentInputsRequest.getAgentInputsList();
      var features = agentInputs.stream()
          .flatMap(CreateAgentInputsAdapter::getFeatures)
          .collect(toList());

      var matches = agentInputs.stream()
          .map(AgentInput::getMatch)
          .distinct()
          .collect(toList());

      log.debug(
          "Sending create agent inputs request for features={}, featuresCount={} "
              + "and matchesCount={}, firstTenMatches={}",
          features, features.size(), matches.size(),
          matches.subList(0, Math.min(10, matches.size())));
    }
  }

  private static Stream<String> getFeatures(AgentInput agentInput) {
    return agentInput.getFeatureInputsList().stream()
        .map(FeatureInput::getFeature);
  }

  private static void logResponse(BatchCreateAgentInputsResponse response) {
    if (log.isDebugEnabled()) {

      var matchesSaved = response.getCreatedAgentInputsList().stream()
          .map(CreatedAgentInput::getMatch)
          .distinct()
          .collect(toList());

      log.debug(
          "Agent inputs saved for matches, matchCount={}, firstTenMatches={}",
          matchesSaved.size(),
          matchesSaved.subList(0, Math.min(10, matchesSaved.size())));
    }
  }
}
