package com.silenteight.payments.bridge.datasource.agent.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class CreateAgentInputsClient {

  private final AgentInputServiceStub stub;

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

    stub.withDeadline(deadline)
        .batchCreateAgentInputs(batchCreateAgentInputsRequest, new AgentInputCallback());
  }

  private static void logRequest(BatchCreateAgentInputsRequest batchCreateAgentInputsRequest) {
    if (log.isDebugEnabled()) {
      var agentInputs = batchCreateAgentInputsRequest.getAgentInputsList();
      var features = agentInputs.stream()
          .flatMap(CreateAgentInputsClient::getFeatures)
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
}
