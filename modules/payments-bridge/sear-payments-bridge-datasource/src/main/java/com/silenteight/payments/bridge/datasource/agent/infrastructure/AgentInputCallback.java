package com.silenteight.payments.bridge.datasource.agent.infrastructure;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;

import io.grpc.stub.StreamObserver;

import static java.util.stream.Collectors.toList;

@Slf4j
class AgentInputCallback implements StreamObserver<BatchCreateAgentInputsResponse> {

  @Override
  public void onNext(BatchCreateAgentInputsResponse response) {
    logResponse(response);
  }

  @Override
  public void onError(Throwable cause) {
    log.error(
        "Request with agent inputs to the datasource service failed, cause {}",
        cause.getMessage());
  }

  @Override
  public void onCompleted() {
    log.debug("Agent input stream to datasource completed");
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
