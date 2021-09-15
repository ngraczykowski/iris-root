package com.silenteight.payments.bridge.mock.datasource;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceImplBase;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Profile("mockdatasource")
@GrpcService
@RequiredArgsConstructor
class CreateAgentInputGrpc extends AgentInputServiceImplBase {

  @Override
  public void batchCreateAgentInputs(
      BatchCreateAgentInputsRequest request,
      StreamObserver<BatchCreateAgentInputsResponse> responseObserver) {
    super.batchCreateAgentInputs(request, responseObserver);
  }
}
