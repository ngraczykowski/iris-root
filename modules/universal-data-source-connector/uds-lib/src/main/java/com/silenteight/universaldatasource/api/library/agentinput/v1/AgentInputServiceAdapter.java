package com.silenteight.universaldatasource.api.library.agentinput.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import io.vavr.control.Try;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class AgentInputServiceAdapter implements AgentInputServiceClient {

  public static final String COULD_NOT_CREATE_AGENT_INPUTS_ERR_MSG = "Couldn't create agent inputs";

  private final AgentInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public <T extends Feature> BatchCreateAgentInputsOut createBatchCreateAgentInputs(
      BatchCreateAgentInputsIn<T> request) {
    return Try
        .of(() -> getStub().batchCreateAgentInputs(request.toBatchCreateAgentInputsRequest()))
        .map(BatchCreateAgentInputsOut::createFrom)
        .onSuccess(
            response -> log.info("Received {} objects", response.getCreatedAgentInputs().size()))
        .onFailure(e -> log.error(COULD_NOT_CREATE_AGENT_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_CREATE_AGENT_INPUTS_ERR_MSG, e));
  }


  private AgentInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
